package games.rednblack.h2d.extension.spine;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.esotericsoftware.spine.SkeletonRenderer;
import games.rednblack.editor.renderer.components.TintComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic;
import games.rednblack.editor.renderer.utils.value.DynamicValue;

public class SpineDrawableLogic implements DrawableLogic, DynamicValue<Boolean> {
    protected ComponentMapper<SpineComponent> spineObjectComponentMapper;
    protected ComponentMapper<TransformComponent> transformComponentMapper;
    protected ComponentMapper<SpineComponent> spineMapper;
    protected ComponentMapper<TintComponent> tintComponentMapper;

    private final SkeletonRenderer skeletonRenderer;

    private boolean normalMap = false;

    public SpineDrawableLogic() {
        skeletonRenderer = new SkeletonRenderer();
    }

    @Override
    public void draw(Batch batch, int entity, float parentAlpha, RenderingType renderingType) {
        normalMap = renderingType == RenderingType.NORMAL_MAP;

        SpineComponent spineObjectComponent = spineMapper.get(entity);

        TintComponent tint = tintComponentMapper.get(entity);

        spineObjectComponent.skeleton.getColor().set(tint.color);

        Color color = spineObjectComponent.skeleton.getColor();
        float oldAlpha = color.a;
        spineObjectComponent.skeleton.getColor().a *= parentAlpha;

        computeTransform(entity).mulLeft(batch.getTransformMatrix());
        applyTransform(entity, batch);
        skeletonRenderer.draw(batch, spineObjectComponent.skeleton);
        resetTransform(entity, batch);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        color.a = oldAlpha;

        normalMap = false;
    }

    protected Matrix4 computeTransform (int rootEntity) {
        SpineComponent spineObjectComponent = spineObjectComponentMapper.get(rootEntity);
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

    protected void applyTransform (int rootEntity, Batch batch) {
        TransformComponent curTransform = transformComponentMapper.get(rootEntity);
        curTransform.oldTransform.set(batch.getTransformMatrix());
        batch.setTransformMatrix(curTransform.computedTransform);
    }

    protected void resetTransform (int rootEntity, Batch batch) {
        TransformComponent curTransform = transformComponentMapper.get(rootEntity);
        batch.setTransformMatrix(curTransform.oldTransform);
    }

    @Override
    public Boolean get() {
        return normalMap ? Boolean.TRUE : Boolean.FALSE;
    }
}