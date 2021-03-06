package games.rednblack.h2d.extention.spine;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Skin;
import games.rednblack.editor.renderer.components.TintComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.systems.render.logic.Drawable;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class SpineDrawableLogic implements Drawable {
    private final ComponentMapper<SpineObjectComponent> spineObjectComponentMapper = ComponentMapper.getFor(SpineObjectComponent.class);
    private final ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<SpineObjectComponent> spineMapper = ComponentMapper.getFor(SpineObjectComponent.class);
    private final ComponentMapper<TintComponent> tintComponentMapper = ComponentMapper.getFor(TintComponent.class);

    private final SkeletonRenderer skeletonRenderer;

    public SpineDrawableLogic() {
        skeletonRenderer = new SkeletonRenderer();
    }

    @Override
    public void draw(Batch batch, Entity entity, float parentAlpha, RenderingType renderingType) {
        SpineObjectComponent spineObjectComponent = spineMapper.get(entity);
        NormalSpineComponent normalComponent = ComponentRetriever.get(entity, NormalSpineComponent.class);
        if (renderingType == RenderingType.NORMAL_MAP && normalComponent == null) return;

        TintComponent tint = tintComponentMapper.get(entity);

        spineObjectComponent.skeleton.getColor().set(tint.color);

        Color color = spineObjectComponent.skeleton.getColor();
        float oldAlpha = color.a;
        spineObjectComponent.skeleton.getColor().a *= parentAlpha;

        computeTransform(entity).mulLeft(batch.getTransformMatrix());
        applyTransform(entity, batch);
        if (renderingType == RenderingType.NORMAL_MAP) {
            Skin oldSkin = spineObjectComponent.skeleton.getSkin();
            spineObjectComponent.skeleton.setSkin(normalComponent.normalSkin);
            skeletonRenderer.draw(batch, spineObjectComponent.skeleton);
            spineObjectComponent.skeleton.setSkin(oldSkin);
        } else {
            skeletonRenderer.draw(batch, spineObjectComponent.skeleton);
        }
        resetTransform(entity, batch);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        color.a = oldAlpha;
    }

    protected Matrix4 computeTransform (Entity rootEntity) {
        SpineObjectComponent spineObjectComponent = spineObjectComponentMapper.get(rootEntity);
        TransformComponent curTransform = transformComponentMapper.get(rootEntity);

        Affine2 worldTransform = curTransform.worldTransform;

        float originX = curTransform.originX;
        float originY = curTransform.originY;
        float x = curTransform.x;
        float y = curTransform.y;
        float rotation = curTransform.rotation;
        float scaleX = curTransform.scaleX * spineObjectComponent.worldMultiplier * (curTransform.flipX ? -1 : 1);
        float scaleY = curTransform.scaleY * spineObjectComponent.worldMultiplier * (curTransform.flipY ? -1 : 1);

        worldTransform.setToTrnRotScl(x + originX , y + originY, rotation, scaleX, scaleY);
        if (originX != 0 || originY != 0) worldTransform.translate(-originX / spineObjectComponent.worldMultiplier, -originY / spineObjectComponent.worldMultiplier);
        worldTransform.translate(-spineObjectComponent.minX, -spineObjectComponent.minY);

        curTransform.computedTransform.set(worldTransform);

        return curTransform.computedTransform;
    }

    protected void applyTransform (Entity rootEntity, Batch batch) {
        TransformComponent curTransform = transformComponentMapper.get(rootEntity);
        curTransform.oldTransform.set(batch.getTransformMatrix());
        batch.setTransformMatrix(curTransform.computedTransform);
    }

    protected void resetTransform (Entity rootEntity, Batch batch) {
        TransformComponent curTransform = transformComponentMapper.get(rootEntity);
        batch.setTransformMatrix(curTransform.oldTransform);
    }
}