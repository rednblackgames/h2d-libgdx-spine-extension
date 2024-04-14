package games.rednblack.h2d.extension.spine;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Null;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.*;
import games.rednblack.editor.renderer.resources.IResourceRetriever;
import games.rednblack.editor.renderer.utils.value.DynamicValue;

import java.util.regex.Matcher;

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

    public RegionAttachment newRegionAttachment (Skin skin, String name, String path, @Null Sequence sequence) {
        path = path.replaceAll("/", Matcher.quoteReplacement("$"));
        TextureRegion region = resourceRetriever.getTextureRegion(prefix + path);
        if (region == null) throw new RuntimeException("Region not found in atlas: " + path + " (region attachment: " + name + ")");
        RegionAttachment attachment;
        if (useNormalMap != null && resourceRetriever.hasTextureRegion(prefix + path + ".normal")) {
            RegionAttachment normalAttachment = new RegionAttachment(name);
            if (sequence != null) {
                Sequence normalSequence = new CopySequence(sequence);
                loadSequence(path, normalSequence, true);
                normalAttachment.setSequence(normalSequence);
            } else {
                normalAttachment.setRegion(resourceRetriever.getTextureRegion(prefix + path + ".normal"));
            }

            attachment = new ABRegionAttachment(normalAttachment, name);
            if (sequence != null) {
                loadSequence(path, sequence, false);
            } else {
                attachment.setRegion(region);
            }
            ((ABRegionAttachment) attachment).setUseAttachmentB(useNormalMap);
        } else {
            attachment = new RegionAttachment(name);
            if (sequence != null) {
                loadSequence(path, sequence, false);
            } else {
                attachment.setRegion(region);
            }
            attachment.setRegion(region);
        }
        return attachment;
    }

    public MeshAttachment newMeshAttachment (Skin skin, String name, String path, @Null Sequence sequence) {
        path = path.replaceAll("/", Matcher.quoteReplacement("$"));
        TextureRegion region = resourceRetriever.getTextureRegion(prefix + path);
        if (region == null) throw new RuntimeException("Region not found in atlas: " + path + " (mesh attachment: " + name + ")");
        MeshAttachment attachment;
        if (useNormalMap != null && (resourceRetriever.hasTextureRegion(prefix + path + ".normal") || sequence != null)) {
            MeshAttachment normalAttachment = new MeshAttachment(name);
            if (sequence != null) {
                Sequence normalSequence = new CopySequence(sequence);
                loadSequence(path, normalSequence, true);
                normalAttachment.setSequence(normalSequence);
            } else {
                normalAttachment.setRegion(resourceRetriever.getTextureRegion(prefix + path + ".normal"));
            }

            attachment = new ABMeshAttachment(normalAttachment, name);
            if (sequence != null) {
                loadSequence(path, sequence, false);
            } else {
                attachment.setRegion(region);
            }
            ((ABMeshAttachment) attachment).setUseAttachmentB(useNormalMap);
        } else {
            attachment = new MeshAttachment(name);
            if (sequence != null) {
                loadSequence(path, sequence, false);
            } else {
                attachment.setRegion(region);
            }
        }
        return attachment;
    }

    public BoundingBoxAttachment newBoundingBoxAttachment (Skin skin, String name) {
        return new Box2DBoundingBoxAttachment(name);
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

    private boolean loadSequence (String basePath, Sequence sequence, boolean normal) {
        TextureRegion[] regions = sequence.getRegions();
        for (int i = 0, n = regions.length; i < n; i++) {
            String path = sequence.getPath(basePath, i);
            regions[i] = resourceRetriever.getTextureRegion(prefix + path + (normal ? ".normal" : ""));
            if (regions[i] == null) return false;
        }
        return true;
    }
}
