package games.rednblack.h2d.extention.spine;/*
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

import com.artemis.systems.IteratingSystem;
import games.rednblack.editor.renderer.commons.IExternalItemType;
import games.rednblack.editor.renderer.components.SpineDataComponent;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.editor.renderer.factory.component.ComponentFactory;
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.Version;

/**
 * Created by azakhary on 7/20/2015.
 */
public class SpineItemType implements IExternalItemType {

    public static final Version SUPPORTED_SPINE_VERSION = new Version("4.0");

    private ComponentFactory factory;
    private IteratingSystem system;
    private DrawableLogic drawableLogic;

    public SpineItemType() {
        factory = new SpineComponentFactory();
        system = new SpineSystem();
        drawableLogic = new SpineDrawableLogic();
    }

    @Override
    public int getTypeId() {
        return EntityFactory.SPINE_TYPE;
    }

    @Override
    public DrawableLogic getDrawable() {
        return drawableLogic;
    }

    @Override
    public IteratingSystem getSystem() {
        return system;
    }

    @Override
    public ComponentFactory getComponentFactory() {
        return factory;
    }

    @Override
    public void injectMappers() {
        ComponentRetriever.addMapper(SpineDataComponent.class);
        ComponentRetriever.addMapper(SpineObjectComponent.class);
    }
}
