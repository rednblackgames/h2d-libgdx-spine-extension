package games.rednblack.h2d.extension.spine;

import com.artemis.systems.IteratingSystem;
import games.rednblack.editor.renderer.commons.IExternalItemType;
import games.rednblack.editor.renderer.factory.component.ComponentFactory;
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.HyperJson;
import games.rednblack.editor.renderer.utils.Version;

import java.io.File;

public class SpineItemType implements IExternalItemType {
    public static final int SPINE_TYPE = 9;

    public String spineAnimationsPath = "spine_animations";
    public static final Version SUPPORTED_SPINE_VERSION = new Version("4.0");

    private ComponentFactory factory;
    private IteratingSystem system;
    private DrawableLogic drawableLogic;

    public SpineItemType() {
        factory = new SpineComponentFactory();
        system = new SpineSystem();
        drawableLogic = new SpineDrawableLogic();

        HyperJson.getJson().addClassTag(SpineVO.class.getSimpleName(), SpineVO.class);
    }

    @Override
    public int getTypeId() {
        return SPINE_TYPE;
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
        ComponentRetriever.addMapper(SpineComponent.class);
    }

    @Override
    public boolean hasResources() {
        return true;
    }

    @Override
    public String formatResourcePath(String resName) {
        return "orig" + File.separator + spineAnimationsPath + File.separator + resName + File.separator + resName + ".json";
    }
}
