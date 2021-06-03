package games.rednblack.h2d.extention.spine;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.*;
import games.rednblack.editor.renderer.resources.IResourceRetriever;

public class ResourceRetrieverAttachmentLoader implements AttachmentLoader {
    private IResourceRetriever resourceRetriever;
    private String prefix;

    public ResourceRetrieverAttachmentLoader(String prefix, IResourceRetriever resourceRetriever) {
        if (resourceRetriever == null) throw new IllegalArgumentException("resourceRetriever cannot be null.");
        if (prefix == null) throw new IllegalArgumentException("prefix cannot be null.");
        this.resourceRetriever = resourceRetriever;
        this.prefix = prefix;
    }

    public RegionAttachment newRegionAttachment (Skin skin, String name, String path) {
        TextureRegion region = resourceRetriever.getTextureRegion(prefix + path);
        if (region == null) throw new RuntimeException("Region not found in atlas: " + path + " (region attachment: " + name + ")");
        RegionAttachment attachment = new RegionAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    public MeshAttachment newMeshAttachment (Skin skin, String name, String path) {
        TextureRegion region = resourceRetriever.getTextureRegion(prefix + path);
        if (region == null) throw new RuntimeException("Region not found in atlas: " + path + " (mesh attachment: " + name + ")");
        MeshAttachment attachment = new MeshAttachment(name);
        attachment.setRegion(region);
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
