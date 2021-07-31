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

package games.rednblack.h2d.extention.spine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.*;
import games.rednblack.editor.renderer.box2dLight.RayHandler;
import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.SpineDataComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.normal.NormalMapRendering;
import games.rednblack.editor.renderer.data.MainItemVO;
import games.rednblack.editor.renderer.data.ProjectInfoVO;
import games.rednblack.editor.renderer.data.SpineVO;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.editor.renderer.factory.component.ComponentFactory;
import games.rednblack.editor.renderer.resources.IResourceRetriever;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class SpineComponentFactory extends ComponentFactory {

    private SpineObjectComponent spineObjectComponent;

    public SpineComponentFactory() {
        super();
    }

    public SpineComponentFactory(PooledEngine engine, RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(engine, rayHandler, world, rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createCommonComponents(entity, vo, EntityFactory.SPINE_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createPhysicsComponents(entity, vo);
        createLightComponents(entity, vo);
        spineObjectComponent = createSpineObjectComponent(entity, (SpineVO) vo);
        createSpineDataComponent(entity, (SpineVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = engine.createComponent(DimensionsComponent.class);

        entity.add(component);
        return component;
    }

    protected SpineObjectComponent createSpineObjectComponent(Entity entity, SpineVO vo) {
        ProjectInfoVO projectInfoVO = rm.getProjectVO();

        SpineObjectComponent component = engine.createComponent(SpineObjectComponent.class);
        NormalMapRendering normalMapRendering = engine.createComponent(NormalMapRendering.class);
        component.skeletonJson = new SkeletonJson(new ResourceRetrieverAttachmentLoader(vo.animationName, rm, normalMapRendering));
        component.skeletonData = component.skeletonJson.readSkeletonData((rm.getSkeletonJSON(vo.animationName)));
        component.skeleton = new Skeleton(component.skeletonData);
        if (component.skeletonData.findSkin("normalMap") != null) {
            entity.add(normalMapRendering);
        }

        component.worldMultiplier = 1f / projectInfoVO.pixelToWorld;

        AnimationStateData stateData = new AnimationStateData(component.skeletonData);
        component.state = new AnimationState(stateData);

        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        component.computeBoundBox(dimensionsComponent);
        dimensionsComponent.width *= component.worldMultiplier;
        dimensionsComponent.height *= component.worldMultiplier;

        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        if (Float.isNaN(vo.originX)) transformComponent.originX = dimensionsComponent.width / 2f;
        else transformComponent.originX = vo.originX;

        if (Float.isNaN(vo.originY)) transformComponent.originY = dimensionsComponent.height / 2f;
        else transformComponent.originY = vo.originY;

        component.setAnimation(vo.currentAnimationName.isEmpty() ? component.skeletonData.getAnimations().get(0).getName() : vo.currentAnimationName);

        entity.add(component);

        return component;
    }

    protected SpineDataComponent createSpineDataComponent(Entity entity, SpineVO vo) {
        SpineDataComponent component = new SpineDataComponent();
        component.animationName = vo.animationName;

        component.currentAnimationName = vo.currentAnimationName.isEmpty() ? spineObjectComponent.skeletonData.getAnimations().get(0).getName() : vo.currentAnimationName;

        entity.add(component);

        return component;
    }

    @Override
    protected TransformComponent createTransformComponent(Entity entity, MainItemVO vo, DimensionsComponent dimensionsComponent) {
        TransformComponent component = engine.createComponent(TransformComponent.class);
        component.rotation = vo.rotation;
        component.scaleX = vo.scaleX;
        component.scaleY = vo.scaleY;
        component.x = vo.x;
        component.y = vo.y;

        component.flipX = vo.flipX;
        component.flipY = vo.flipY;

        entity.add(component);

        return component;
    }
}
