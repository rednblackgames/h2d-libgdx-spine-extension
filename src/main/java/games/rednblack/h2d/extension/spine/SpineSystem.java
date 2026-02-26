package games.rednblack.h2d.extension.spine;

import games.rednblack.editor.renderer.ecs.ComponentMapper;
import games.rednblack.editor.renderer.ecs.annotations.All;
import games.rednblack.editor.renderer.ecs.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.Slot;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.components.ParentNodeComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.utils.TmpFloatArray;
import games.rednblack.editor.renderer.utils.TransformMathUtils;
import games.rednblack.editor.renderer.utils.poly.PolygonRuntimeUtils;

@All({SpineComponent.class})
public class SpineSystem extends IteratingSystem {
    protected ComponentMapper<SpineComponent> spineObjectComponentMapper;
    protected ComponentMapper<TransformComponent> transformComponentMapper;
    protected ComponentMapper<ParentNodeComponent> parentMapper;

    protected SceneLoader sceneLoader;

    private final TmpFloatArray tmpFloatArray = new TmpFloatArray();
    private final Vector2 tmp = new Vector2();

    private final BodyDef boxBodyDef = new BodyDef();
    private final FixtureDef tmpFixtureDef = new FixtureDef();
    private PolygonShape tmpPolygonShape; //TODO dispose those stuff at the end

    @Override
    protected void process(int entity) {
        TransformComponent transformComponent = transformComponentMapper.get(entity);
        SpineComponent spineObjectComponent = spineObjectComponentMapper.get(entity);

        if (transformComponent.x != spineObjectComponent.lastX || transformComponent.y != spineObjectComponent.lastY) {
            float scaleX = transformComponent.scaleX * (transformComponent.flipX ? -1 : 1);
            float scaleY = transformComponent.scaleY * (transformComponent.flipY ? -1 : 1);
            int directionX = scaleX > 0 ? 1 : -1;
            int directionY = scaleY > 0 ? 1 : -1;

            spineObjectComponent.skeleton.physicsTranslate(
                    directionX * (transformComponent.x - spineObjectComponent.lastX) / spineObjectComponent.worldMultiplier,
                    directionY * (transformComponent.y - spineObjectComponent.lastY) / spineObjectComponent.worldMultiplier
            );

            spineObjectComponent.lastX = transformComponent.x;
            spineObjectComponent.lastY = transformComponent.y;
        }

        spineObjectComponent.state.update(engine.getDelta()); // Update the animation time.
        spineObjectComponent.state.apply(spineObjectComponent.skeleton);
        spineObjectComponent.skeleton.update(engine.getDelta());
        spineObjectComponent.skeleton.updateWorldTransform(Skeleton.Physics.update);

        if (sceneLoader.getWorld() == null) return;

        for (Slot slot : spineObjectComponent.skeleton.getSlots()) {
            if (!(slot.getAttachment() instanceof Box2DBoundingBoxAttachment)) {
                if (spineObjectComponent.bodies.get(slot.toString()) != null) {
                    //The slot had previously a bounding box attachment, body should be ignored by the physics too
                    Body body = spineObjectComponent.bodies.get(slot.toString()).body;
                    if (body.isActive()) body.setActive(false);
                }
                continue;
            }

            Box2DBoundingBoxAttachment attachment = (Box2DBoundingBoxAttachment) slot.getAttachment();
            Body body;

            if (spineObjectComponent.bodies.get(slot.toString()) == null) {
                //Slot has a BB attachment but no body exists in the world
                boxBodyDef.type = BodyDef.BodyType.KinematicBody;
                boxBodyDef.awake = true;
                body = sceneLoader.getWorld().createBody(boxBodyDef);
                body.setUserData(entity);
                SlotBody slotBody = new SlotBody();
                slotBody.body = body;
                spineObjectComponent.bodies.put(slot.toString(), slotBody);

                if (tmpPolygonShape == null) tmpPolygonShape = new PolygonShape();

                Vector2[][] minPolygonData = attachment.polygonizedVertices;

                slotBody.checksum = 0;
                for (int i = 0; i < minPolygonData.length; i++) {
                    Vector2[] minPolygonDatum = minPolygonData[i];
                    float[] verts = tmpFloatArray.getTemporaryArray(minPolygonDatum.length * 2);
                    for (int j = 0; j < verts.length; j += 2) {
                        verts[j] = minPolygonDatum[j / 2].x;
                        verts[j + 1] = minPolygonDatum[j / 2].y;

                        //Spine, why scaleY and scaleX are inverted???
                        verts[j] *= transformComponent.scaleY * (transformComponent.flipX ? -1 : 1);
                        verts[j + 1] *= transformComponent.scaleX * (transformComponent.flipY ? -1 : 1);

                        slotBody.checksum += Float.floatToRawIntBits(verts[j]) * 31
                                + Float.floatToRawIntBits(verts[j + 1]) * 37;
                    }

                    tmpPolygonShape.set(verts);

                    tmpFixtureDef.shape = tmpPolygonShape;
                    tmpFixtureDef.isSensor = true;
                    //other custom fixture def here

                    body.createFixture(tmpFixtureDef);
                }
            } else {
                //Slot has a BB attachment and body is in the world
                SlotBody slotBody = spineObjectComponent.bodies.get(slot.toString());
                body = slotBody.body;
                if (!body.isActive()) body.setActive(true);

                //Calc a vertex checksum to understand if vertices are changed, if so update box2d fixture
                int checksum = 0;
                //TODO attachment vertices are always the same, Slot#getDeform should be used properly to get the list of deformed vertices
                float[] verts = attachment.getVertices();
                for (int j = 0; j < verts.length; j += 2) {
                    float scaledX = verts[j] * transformComponent.scaleY * (transformComponent.flipX ? -1 : 1);
                    float scaledY = verts[j + 1] * transformComponent.scaleX * (transformComponent.flipY ? -1 : 1);
                    checksum += Float.floatToRawIntBits(scaledX) * 31
                            + Float.floatToRawIntBits(scaledY) * 37;
                }

                if (checksum != slotBody.checksum) {
                    for (int i = 0; i < attachment.getVertices().length; i += 2) {
                        attachment.vertices[i / 2].x = attachment.getVertices()[i] * spineObjectComponent.worldMultiplier;
                        attachment.vertices[i / 2].y = attachment.getVertices()[i + 1] * spineObjectComponent.worldMultiplier;
                    }

                    //TODO polygonize create a lot of garbage
                    attachment.polygonizedVertices = PolygonRuntimeUtils.polygonize(attachment.vertices);
                    attachment.updateBoundingBox();

                    slotBody.checksum = checksum;

                    Vector2[][] minPolygonData = attachment.polygonizedVertices;
                    Array<Fixture> fixtures = body.getFixtureList();
                    //Try to recycle fixtures
                    for (int i = 0; i < minPolygonData.length; i++) {
                        if (i == fixtures.size) {
                            //create fixture
                            tmpFixtureDef.shape = tmpPolygonShape;
                            tmpFixtureDef.isSensor = true;
                            //other custom fixture def here

                            body.createFixture(tmpFixtureDef);
                        }
                        PolygonShape p = (PolygonShape) fixtures.get(i).getShape();
                        Vector2[] minPolygonDatum = minPolygonData[i];
                        float[] tmpVerts = tmpFloatArray.getTemporaryArray(minPolygonDatum.length * 2);
                        for (int j = 0; j < tmpVerts.length; j += 2) {
                            tmpVerts[j] = minPolygonDatum[j / 2].x;
                            tmpVerts[j + 1] = minPolygonDatum[j / 2].y;

                            //Spine, why scaleY and scaleX are inverted???
                            tmpVerts[j] *= transformComponent.scaleY * (transformComponent.flipX ? -1 : 1);
                            tmpVerts[j + 1] *= transformComponent.scaleX * (transformComponent.flipY ? -1 : 1);
                        }
                        p.set(tmpVerts);
                    }

                    //Clear all unused fixtures
                    for (int i = minPolygonData.length; i < fixtures.size; i++) {
                        Fixture fixture = fixtures.get(i);
                        body.destroyFixture(fixture);
                    }
                }
            }

            //Sync body and bone
            Bone bone = slot.getBone();

            tmp.set(bone.getWorldX() - spineObjectComponent.minX, bone.getWorldY() - spineObjectComponent.minY)
                    .scl(spineObjectComponent.worldMultiplier);
            TransformMathUtils.localToSceneCoordinates(entity, tmp, transformComponentMapper, parentMapper);

            float x = tmp.x;
            float y = tmp.y;
            int flip = (transformComponent.flipX ? -1 : 1) * (transformComponent.flipY ? -1 : 1);
            float rotation = flip * TransformMathUtils.localToAscendantRotation(-1, entity, bone.getWorldRotationX(), transformComponentMapper, parentMapper);

            body.setTransform(x, y, rotation * MathUtils.degreesToRadians);
        }
    }
}
