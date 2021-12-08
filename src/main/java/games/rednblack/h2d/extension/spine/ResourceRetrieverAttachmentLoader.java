package games.rednblack.h2d.extension.spine;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.*;
import games.rednblack.editor.renderer.resources.IResourceRetriever;
import games.rednblack.editor.renderer.utils.value.DynamicValue;

public class ResourceRetrieverAttachmentLoader implements AttachmentLoader {
    private IResourceRetriever resourceRetriever;
    private DynamicValue<Boolean> useNormalMap;
    private String prefix;

    public ResourceRetrieverAttachmentLoader(String prefix, IResourceRetriever resourceRetriever, DynamicValue<Boolean> useNormalMap) {
        if (resourceRetriever == null) throw new IllegalArgumentException("resourceRetriever cannot be null.");
        if (prefix == null) throw new IllegalArgumentException("prefix cannot be null.");
        this.resourceRetriever = resourceRetriever;
        this.prefix = prefix;
        this.useNormalMap = useNormalMap;
    }

    public ResourceRetrieverAttachmentLoader(String prefix, IResourceRetriever resourceRetriever) {
        this(prefix, resourceRetriever, null);
    }

    public RegionAttachment newRegionAttachment (Skin skin, String name, String path) {
        TextureRegion region = resourceRetriever.getTextureRegion(prefix + path);
        if (region == null) throw new RuntimeException("Region not found in atlas: " + path + " (region attachment: " + name + ")");
        RegionAttachment attachment;
        if (useNormalMap != null && resourceRetriever.hasTextureRegion(prefix + path + ".normal")) {
            RegionAttachment normalAttachment = new RegionAttachment(name);
            normalAttachment.setRegion(resourceRetriever.getTextureRegion(prefix + path + ".normal"));
            attachment = new ABRegionAttachment(normalAttachment, name);
            attachment.setRegion(region);
            ((ABRegionAttachment) attachment).setUseAttachmentB(useNormalMap);
        } else {
            attachment = new RegionAttachment(name);
            attachment.setRegion(region);
        }
        return attachment;
    }

    public MeshAttachment newMeshAttachment (Skin skin, String name, String path) {
        TextureRegion region = resourceRetriever.getTextureRegion(prefix + path);
        if (region == null) throw new RuntimeException("Region not found in atlas: " + path + " (mesh attachment: " + name + ")");
        MeshAttachment attachment;
        if (useNormalMap != null && resourceRetriever.hasTextureRegion(prefix + path + ".normal")) {
            MeshAttachment normalAttachment = new MeshAttachment(name);
            normalAttachment.setRegion(resourceRetriever.getTextureRegion(prefix + path + ".normal"));
            attachment = new ABMeshAttachment(normalAttachment, name);
            attachment.setRegion(region);
            ((ABMeshAttachment) attachment).setUseAttachmentB(useNormalMap);
        } else {
            attachment = new MeshAttachment(name);
            attachment.setRegion(region);
        }
        return attachment;
    }

    public BoundingBoxAttachment newBoundingBoxAttachment (Skin skin, String name) {
        return new BoundingBoxAttachment(name);
    }

    public ClippingAttachment newClippingAttachment (Skin skin, String name) {
        return new ClippingAttachment(name);
    }

    public PathAttachment newPathAttachment (Skin skin, String name) {
        return new PathAttachment(name);
    }

    public PointAttachment newPointAttachment (Skin skin, String name) {
        return new PointAttachment(name);
    }
}
