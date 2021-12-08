package games.rednblack.h2d.extension.spine;

import com.artemis.ComponentMapper;
import com.artemis.EntityTransmuter;
import com.artemis.EntityTransmuterFactory;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonJson;
import games.rednblack.editor.renderer.box2dLight.RayHandler;
import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.SpineDataComponent;
import games.rednblack.editor.renderer.components.normal.NormalMapRendering;
import games.rednblack.editor.renderer.data.ProjectInfoVO;
import games.rednblack.editor.renderer.factory.v2.ComponentFactoryV2;
import games.rednblack.editor.renderer.factory.v2.EntityFactoryV2;
import games.rednblack.editor.renderer.resources.IResourceRetriever;

public class SpineComponentFactoryV2 extends ComponentFactoryV2 {

    protected ComponentMapper<SpineObjectComponent> spineObjectCM;
    protected ComponentMapper<SpineDataComponent> spineDataCM;
    protected ComponentMapper<NormalMapRendering> normalMapRenderingCM;

    private EntityTransmuter transmuter;

    public SpineComponentFactoryV2() {

    }

    @Override
    public void injectDependencies(com.artemis.World engine, RayHandler rayHandler, World world, IResourceRetriever rm) {
        super.injectDependencies(engine, rayHandler, world, rm);

        transmuter = new EntityTransmuterFactory(engine)
                .add(SpineObjectComponent.class)
                .add(SpineDataComponent.class)
                .add(NormalMapRendering.class)
                .build();
    }


    @Override
    public void transmuteEntity(int entity) {
        transmuter.transmute(entity);
    }

    @Override
    public int getEntityType() {
        return EntityFactoryV2.SPINE_TYPE;
    }

    @Override
    public void setInitialData(int entity, Object data) {
        spineDataCM.get(entity).animationName = (String) data;
    }

    @Override
    protected void initializeTransientComponents(int entity) {
        super.initializeTransientComponents(entity);
        SpineDataComponent data = spineDataCM.get(entity);
        ProjectInfoVO projectInfoVO = rm.getProjectVO();

        SpineObjectComponent component = spineObjectCM.get(entity);
        NormalMapRendering normalMapRendering = normalMapRenderingCM.get(entity);
        component.skeletonJson = new SkeletonJson(new ResourceRetrieverAttachmentLoader(data.animationName, rm, normalMapRendering));
        component.skeletonData = component.skeletonJson.readSkeletonData((rm.getSkeletonJSON(data.animationName)));
        component.skeleton = new Skeleton(component.skeletonData);
        if (component.skeletonData.findSkin("normalMap") == null) {
            normalMapRenderingCM.remove(entity);
        }

        component.worldMultiplier = 1f / projectInfoVO.pixelToWorld;

        data.currentAnimationName = data.currentAnimationName.isEmpty() ? component.skeletonData.getAnimations().get(0).getName() : data.currentAnimationName;

        AnimationStateData stateData = new AnimationStateData(component.skeletonData);
        component.state = new AnimationState(stateData);
        component.setAnimation(data.currentAnimationName);
    }

    @Override
    protected void initializeDimensionsComponent(int entity) {
        SpineObjectComponent component = spineObjectCM.get(entity);
        DimensionsComponent dimensionsComponent = dimensionsCM.get(entity);
        component.computeBoundBox(dimensionsComponent);
        dimensionsComponent.width *= component.worldMultiplier;
        dimensionsComponent.height *= component.worldMultiplier;
    }
}
