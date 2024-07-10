package games.rednblack.h2d.extension.spine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import games.rednblack.editor.renderer.components.TintComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic;
import games.rednblack.editor.renderer.utils.value.DynamicValue;

public class SpineDrawableLogic implements DrawableLogic, DynamicValue<Boolean> {
    protected ComponentMapper<TransformComponent> transformComponentMapper;
    protected ComponentMapper<SpineComponent> spineMapper;
    protected ComponentMapper<TintComponent> tintComponentMapper;
    protected final SkeletonRenderSeparator skeletonRenderer;

    protected com.artemis.World engine;
    private EntitySubscription spineEntities;

    protected boolean normalMap = false;

    public SpineDrawableLogic() {
        skeletonRenderer = new SkeletonRenderSeparator();
    }

    @Override
    public void beginPipeline() {
        if (spineEntities == null) {
            spineEntities = engine.getAspectSubscriptionManager().get(Aspect.all(SpineComponent.class));
        }
    }

    @Override
    public void endPipeline() {
        IntBag actives = spineEntities.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); s > i; i++) {
            SpineComponent spineObjectComponent = spineMapper.get(ids[i]);
            spineObjectComponent.splitRenderingRangeIndex = 0;
        }
    }

    @Override
    public void draw(Batch batch, int entity, float parentAlpha, RenderingType renderingType) {
        batch.setBlendFunctionSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_ONE);
        normalMap = renderingType == RenderingType.NORMAL_MAP;

        SpineComponent spineObjectComponent = spineMapper.get(entity);
        TransformComponent curTransform = transformComponentMapper.get(entity);

        TintComponent tint = tintComponentMapper.get(entity);

        spineObjectComponent.skeleton.getColor().set(tint.color);

        Color color = spineObjectComponent.skeleton.getColor();
        float oldAlpha = color.a;
        spineObjectComponent.skeleton.getColor().a *= parentAlpha;

        computeTransform(spineObjectComponent, curTransform).mulLeft(batch.getTransformMatrix());
        curTransform.oldTransform.set(batch.getTransformMatrix());
        batch.setTransformMatrix(curTransform.computedTransform);

        if (spineObjectComponent.splitRenderingRangeIndex < spineObjectComponent.splitRenderingRange.size) {
            SlotRange slotRange = spineObjectComponent.splitRenderingRange.get(spineObjectComponent.splitRenderingRangeIndex);
            skeletonRenderer.draw(batch, spineObjectComponent.skeleton, slotRange);
            spineObjectComponent.splitRenderingRangeIndex++;
        }

        batch.setTransformMatrix(curTransform.oldTransform);

        color.a = oldAlpha;

        normalMap = false;
    }

    protected Matrix4 computeTransform (SpineComponent spineObjectComponent, TransformComponent curTransform) {
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

    @Override
    public Boolean get() {
        return normalMap ? Boolean.TRUE : Boolean.FALSE;
    }
}