package games.rednblack.h2d.extention.spine;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.*;

public class PrefixAtlasAttachmentLoader implements AttachmentLoader {
    private TextureAtlas atlas;
    private String prefix;

    public PrefixAtlasAttachmentLoader (String prefix, TextureAtlas atlas) {
        if (atlas == null) throw new IllegalArgumentException("atlas cannot be null.");
        if (prefix == null) throw new IllegalArgumentException("prefix cannot be null.");
        this.atlas = atlas;
        this.prefix = prefix;
    }

    public RegionAttachment newRegionAttachment (Skin skin, String name, String path) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(prefix + path);
        if (region == null) throw new RuntimeException("Region not found in atlas: " + path + " (region attachment: " + name + ")");
        RegionAttachment attachment = new RegionAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    public MeshAttachment newMeshAttachment (Skin skin, String name, String path) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(prefix + path);
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
