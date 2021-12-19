package games.rednblack.h2d.extension.spine;

import com.artemis.systems.IteratingSystem;
import games.rednblack.editor.renderer.commons.IExternalItemType;
import games.rednblack.editor.renderer.components.SpineDataComponent;
import games.rednblack.editor.renderer.data.SpineVO;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.editor.renderer.factory.component.ComponentFactory;
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.Version;

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
