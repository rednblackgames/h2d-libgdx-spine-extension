package games.rednblack.h2d.extension.spine;

import com.artemis.ComponentMapper;
import com.artemis.EntityTransmuter;
import com.artemis.EntityTransmuterFactory;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import games.rednblack.editor.renderer.box2dLight.RayHandler;
import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.normal.NormalMapRendering;
import games.rednblack.editor.renderer.data.MainItemVO;
import games.rednblack.editor.renderer.data.ProjectInfoVO;
import games.rednblack.editor.renderer.factory.component.ComponentFactory;
import games.rednblack.editor.renderer.resources.IResourceRetriever;

public class SpineComponentFactory extends ComponentFactory {

    protected ComponentMapper<SpineComponent> spineCM;
    protected ComponentMapper<NormalMapRendering> normalMapRenderingCM;

    private EntityTransmuter transmuter;

    public SpineComponentFactory() {

    }

    @Override
    public void injectDependencies(com.artemis.World engine, RayHandler rayHandler, World world, IResourceRetriever rm) {
        super.injectDependencies(engine, rayHandler, world, rm);

        transmuter = new EntityTransmuterFactory(engine)
                .add(SpineComponent.class)
                .add(NormalMapRendering.class)
                .build();
    }


    @Override
    public void transmuteEntity(int entity) {
        transmuter.transmute(entity);
    }

    @Override
    public int getEntityType() {
        return SpineItemType.SPINE_TYPE;
    }

    @Override
    public void setInitialData(int entity, Object data) {
        spineCM.get(entity).animationName = (String) data;
    }

    @Override
    public Class<SpineVO> getVOType() {
        return SpineVO.class;
    }

    @Override
    public void initializeSpecialComponentsFromVO(int entity, MainItemVO voG) {
        SpineVO vo = (SpineVO) voG;
        SpineComponent spineComponent = spineCM.get(entity);
        spineComponent.animationName = vo.animationName;
        spineComponent.currentAnimationName = vo.currentAnimationName;
        spineComponent.currentSkinName = vo.currentSkinName;
    }

    @Override
    protected void initializeTransientComponents(int entity) {
        super.initializeTransientComponents(entity);
        ProjectInfoVO projectInfoVO = rm.getProjectVO();

        SpineComponent component = spineCM.get(entity);
        SpineDataObject spineDataObject = (SpineDataObject) rm.getExternalItemType(getEntityType(), component.animationName);
        component.skeletonJson = spineDataObject.skeletonJson;
        component.skeletonData = spineDataObject.skeletonData;
        component.skeleton = new Skeleton(component.skeletonData);
        if (component.skeletonData.findSkin("normalMap") == null) {
            normalMapRenderingCM.remove(entity);
        }

        component.worldMultiplier = 1f / projectInfoVO.pixelToWorld;

        component.currentAnimationName = component.currentAnimationName.isEmpty() ? component.skeletonData.getAnimations().get(0).getName() : component.currentAnimationName;

        AnimationStateData stateData = new AnimationStateData(component.skeletonData);
        component.state = new AnimationState(stateData);
        component.setAnimation(component.currentAnimationName);
        component.setSkin(component.currentSkinName);
    }

    @Override
    protected void initializeDimensionsComponent(int entity) {
        SpineComponent component = spineCM.get(entity);
        DimensionsComponent dimensionsComponent = dimensionsCM.get(entity);
        component.computeBoundBox(dimensionsComponent);
        dimensionsComponent.width *= component.worldMultiplier;
        dimensionsComponent.height *= component.worldMultiplier;
    }
}
