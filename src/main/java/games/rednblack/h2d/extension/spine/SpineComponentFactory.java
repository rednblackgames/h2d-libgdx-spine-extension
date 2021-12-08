/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package games.rednblack.h2d.extension.spine;

import com.artemis.ComponentMapper;
import com.artemis.EntityTransmuter;
import com.artemis.EntityTransmuterFactory;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.*;
import games.rednblack.editor.renderer.box2dLight.RayHandler;
import games.rednblack.editor.renderer.components.*;
import games.rednblack.editor.renderer.components.normal.NormalMapRendering;
import games.rednblack.editor.renderer.data.MainItemVO;
import games.rednblack.editor.renderer.data.ProjectInfoVO;
import games.rednblack.editor.renderer.data.SpineVO;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.editor.renderer.factory.component.ComponentFactory;
import games.rednblack.editor.renderer.resources.IResourceRetriever;

public class SpineComponentFactory extends ComponentFactory {

    protected ComponentMapper<SpineObjectComponent> spineObjectCM;
    protected ComponentMapper<SpineDataComponent> spineDataCM;
    protected ComponentMapper<NormalMapRendering> normalMapRenderingCM;
    protected ComponentMapper<DimensionsComponent> dimensionsCM;
    protected ComponentMapper<TransformComponent> transformCM;

    private EntityTransmuter transmuter;

    public SpineComponentFactory() {

    }

    @Override
    public void injectDependencies(com.artemis.World engine, RayHandler rayHandler, World world, IResourceRetriever rm) {
        super.injectDependencies(engine, rayHandler, world, rm);

        transmuter = new EntityTransmuterFactory(engine)
                .add(ParentNodeComponent.class)
                .add(SpineObjectComponent.class)
                .add(SpineDataComponent.class)
                .add(NormalMapRendering.class)
                .build();
    }

    @Override
    public int createSpecialisedEntity(int root, MainItemVO vo) {
        int entity = createGeneralEntity(vo, EntityFactory.SPINE_TYPE);
        transmuter.transmute(entity);

        adjustNodeHierarchy(root, entity);

        createSpineObjectComponent(entity, (SpineVO) vo);
        createSpineDataComponent(entity, (SpineVO) vo);
        return entity;
    }

    @Override
    protected void initializeDimensionsComponent(int entity, DimensionsComponent component, MainItemVO vo) {

    }

    protected SpineObjectComponent createSpineObjectComponent(int entity, SpineVO vo) {
        ProjectInfoVO projectInfoVO = rm.getProjectVO();

        SpineObjectComponent component = spineObjectCM.get(entity);
        NormalMapRendering normalMapRendering = normalMapRenderingCM.get(entity);
        component.skeletonJson = new SkeletonJson(new ResourceRetrieverAttachmentLoader(vo.animationName, rm, normalMapRendering));
        component.skeletonData = component.skeletonJson.readSkeletonData((rm.getSkeletonJSON(vo.animationName)));
        component.skeleton = new Skeleton(component.skeletonData);
        if (component.skeletonData.findSkin("normalMap") == null) {
            normalMapRenderingCM.remove(entity);
        }

        component.worldMultiplier = 1f / projectInfoVO.pixelToWorld;

        AnimationStateData stateData = new AnimationStateData(component.skeletonData);
        component.state = new AnimationState(stateData);

        DimensionsComponent dimensionsComponent = dimensionsCM.get(entity);
        component.computeBoundBox(dimensionsComponent);
        dimensionsComponent.width *= component.worldMultiplier;
        dimensionsComponent.height *= component.worldMultiplier;

        TransformComponent transformComponent = transformCM.get(entity);
        if (Float.isNaN(vo.originX)) transformComponent.originX = dimensionsComponent.width / 2f;
        else transformComponent.originX = vo.originX;

        if (Float.isNaN(vo.originY)) transformComponent.originY = dimensionsComponent.height / 2f;
        else transformComponent.originY = vo.originY;

        component.setAnimation(vo.currentAnimationName.isEmpty() ? component.skeletonData.getAnimations().get(0).getName() : vo.currentAnimationName);

        return component;
    }

    protected SpineDataComponent createSpineDataComponent(int entity, SpineVO vo) {
        SpineDataComponent component = spineDataCM.get(entity);
        SpineObjectComponent spineObjectComponent = spineObjectCM.get(entity);
        component.animationName = vo.animationName;

        component.currentAnimationName = vo.currentAnimationName.isEmpty() ? spineObjectComponent.skeletonData.getAnimations().get(0).getName() : vo.currentAnimationName;

        return component;
    }

    @Override
    protected void initializeTransformComponent(TransformComponent component, MainItemVO vo, DimensionsComponent dimensionsComponent) {
        component.rotation = vo.rotation;
        component.scaleX = vo.scaleX;
        component.scaleY = vo.scaleY;
        component.x = vo.x;
        component.y = vo.y;

        component.flipX = vo.flipX;
        component.flipY = vo.flipY;
    }
}
