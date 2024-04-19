package games.rednblack.h2d.extension.spine;

import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectSet;
import com.esotericsoftware.spine.SkeletonJson;
import games.rednblack.editor.renderer.commons.IExternalItemType;
import games.rednblack.editor.renderer.factory.component.ComponentFactory;
import games.rednblack.editor.renderer.resources.IResourceRetriever;
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.HyperJson;
import games.rednblack.editor.renderer.utils.Version;

import java.io.File;
import java.util.HashMap;

public class SpineItemType implements IExternalItemType {
    public static final int SPINE_TYPE = 9;

    public String spineAnimationsPath = "spine-animations";
    public static final Version SUPPORTED_SPINE_VERSION = new Version("4.2");


    private ComponentFactory factory;
    private IteratingSystem system;
    private SpineDrawableLogic drawableLogic;

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
    public void loadExternalTypesAsync(IResourceRetriever rm, ObjectSet<String> assetsToLoad, HashMap<String, Object> assets) {
        // empty existing ones that are not scheduled to load
        for (String key : assets.keySet()) {
            if (!assetsToLoad.contains(key)) {
                assets.remove(key);
            }
        }

        // load scheduled
        for (String name : assetsToLoad) {
            SpineDataObject spineDataObject = new SpineDataObject();
            spineDataObject.skeletonJson = new SkeletonJson(new ResourceRetrieverAttachmentLoader(name, rm, drawableLogic));
            spineDataObject.skeletonData = spineDataObject.skeletonJson.readSkeletonData(Gdx.files.internal(formatResourcePath(name)));

            assets.put(name, spineDataObject);
        }
    }

    @Override
    public void loadExternalTypesSync(IResourceRetriever rm, ObjectSet<String> assetsToLoad, HashMap<String, Object> assets) {

    }

    protected String formatResourcePath(String resName) {
        return spineAnimationsPath + File.separator + resName + File.separator + resName + ".json";
    }
}
