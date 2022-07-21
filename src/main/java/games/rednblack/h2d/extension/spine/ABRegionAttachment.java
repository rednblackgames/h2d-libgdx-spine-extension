package games.rednblack.h2d.extension.spine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.esotericsoftware.spine.attachments.Sequence;
import games.rednblack.editor.renderer.utils.value.DynamicValue;

public class ABRegionAttachment extends RegionAttachment {
    private final RegionAttachment attachmentB;
    private DynamicValue<Boolean> useAttachmentB;

    public ABRegionAttachment(RegionAttachment attachmentB, String name) {
        super(name);
        this.attachmentB = attachmentB;
    }

    public void setUseAttachmentB(DynamicValue<Boolean> useAttachmentB) {
        this.useAttachmentB = useAttachmentB;
    }

    @Override
    public void updateRegion() {
        super.updateRegion(); attachmentB.updateRegion();
    }

    @Override
    public void setRegion(TextureRegion region) {
        //Region to attachmentB is set manually
        super.setRegion(region);
    }

    @Override
    public TextureRegion getRegion() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getRegion(); else return super.getRegion();
    }

    @Override
    public void computeWorldVertices(Slot slot, float[] worldVertices, int offset, int stride) {
        super.computeWorldVertices(slot, worldVertices, offset, stride); attachmentB.computeWorldVertices(slot, worldVertices, offset, stride);
    }

    @Override
    public float[] getOffset() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getOffset(); else return super.getOffset();
    }

    @Override
    public float[] getUVs() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getUVs(); else return super.getUVs();
    }

    @Override
    public float getX() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getX(); else return super.getX();
    }

    @Override
    public void setX(float x) {
        super.setX(x); attachmentB.setX(x);
    }

    @Override
    public float getY() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getY(); else return super.getY();
    }

    @Override
    public void setY(float y) {
        super.setY(y); attachmentB.setY(y);
    }

    @Override
    public float getScaleX() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getScaleX(); else return super.getScaleX();
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX); attachmentB.setScaleX(scaleX);
    }

    @Override
    public float getScaleY() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getScaleY(); else return super.getScaleY();
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY); attachmentB.setScaleY(scaleY);
    }

    @Override
    public float getRotation() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getRotation(); else return super.getRotation();
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation); attachmentB.setRotation(rotation);
    }

    @Override
    public float getWidth() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getWidth(); else return super.getWidth();
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width); attachmentB.setWidth(width);
    }

    @Override
    public float getHeight() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getHeight(); else return super.getHeight();
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height); attachmentB.setHeight(height);
    }

    @Override
    public Color getColor() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getColor(); else return super.getColor();
    }

    @Override
    public String getPath() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getPath(); else return super.getPath();
    }

    @Override
    public void setPath(String path) {
        super.setPath(path); attachmentB.setPath(path);
    }

    @Override
    public Sequence getSequence() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getSequence(); else return super.getSequence();
    }

    @Override
    public void setSequence(Sequence sequence) {
        //Sequence to attachmentB is set manually
        super.setSequence(sequence);
    }

    @Override
    public RegionAttachment copy() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.copy(); else return super.copy();
    }

    @Override
    public String getName() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getName(); else return super.getName();
    }

    @Override
    public String toString() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.toString(); else return super.toString();
    }

    @Override
    public int hashCode() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.hashCode(); else return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.equals(obj); else return super.equals(obj);
    }
}
