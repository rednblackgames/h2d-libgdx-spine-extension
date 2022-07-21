package games.rednblack.h2d.extension.spine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.Sequence;
import games.rednblack.editor.renderer.utils.value.DynamicValue;

public class ABMeshAttachment extends MeshAttachment {

    private final MeshAttachment attachmentB;
    private DynamicValue<Boolean> useAttachmentB;

    public ABMeshAttachment(MeshAttachment attachmentB, String name) {
        super(name);
        this.attachmentB = attachmentB;
    }

    public void setUseAttachmentB(DynamicValue<Boolean> useAttachmentB) {
        this.useAttachmentB = useAttachmentB;
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
    public void updateRegion() {
        attachmentB.updateRegion(); super.updateRegion();
    }

    @Override
    public void computeWorldVertices(Slot slot, int start, int count, float[] worldVertices, int offset, int stride) {
        attachmentB.computeWorldVertices(slot, start, count, worldVertices, offset, stride);
        super.computeWorldVertices(slot, start, count, worldVertices, offset, stride);
    }

    @Override
    public short[] getTriangles() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getTriangles(); else return super.getTriangles();
    }

    @Override
    public void setTriangles(short[] triangles) {
        attachmentB.setTriangles(triangles); super.setTriangles(triangles);
    }

    @Override
    public float[] getRegionUVs() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getRegionUVs(); else return super.getRegionUVs();
    }

    @Override
    public void setRegionUVs(float[] regionUVs) {
        attachmentB.setRegionUVs(regionUVs); super.setRegionUVs(regionUVs);
    }

    @Override
    public float[] getUVs() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getUVs(); else return super.getUVs();
    }

    @Override
    public void setUVs(float[] uvs) {
        attachmentB.setUVs(uvs); super.setUVs(uvs);
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
        attachmentB.setPath(path); super.setPath(path);
    }

    @Override
    public int getHullLength() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getHullLength(); else return super.getHullLength();
    }

    @Override
    public void setHullLength(int hullLength) {
        attachmentB.setHullLength(hullLength); super.setHullLength(hullLength);
    }

    @Override
    public void setEdges(short[] edges) {
        attachmentB.setEdges(edges); super.setEdges(edges);
    }

    @Override
    public short[] getEdges() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getEdges(); else return super.getEdges();
    }

    @Override
    public float getWidth() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getWidth(); else return super.getWidth();
    }

    @Override
    public void setWidth(float width) {
        attachmentB.setWidth(width); super.setWidth(width);
    }

    @Override
    public float getHeight() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getHeight(); else return super.getHeight();
    }

    @Override
    public void setHeight(float height) {
        attachmentB.setHeight(height); super.setHeight(height);
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
    public MeshAttachment getParentMesh() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getParentMesh(); else return super.getParentMesh();
    }

    @Override
    public void setParentMesh(MeshAttachment parentMesh) {
        attachmentB.setParentMesh(parentMesh); super.setParentMesh(parentMesh);
    }

    @Override
    public MeshAttachment copy() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.copy(); else return super.copy();
    }

    @Override
    public MeshAttachment newLinkedMesh() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.newLinkedMesh(); else return super.newLinkedMesh();
    }

    @Override
    public int[] getBones() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getBones(); else return super.getBones();
    }

    @Override
    public void setBones(int[] bones) {
        attachmentB.setBones(bones); super.setBones(bones);
    }

    @Override
    public float[] getVertices() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getVertices(); else return super.getVertices();
    }

    @Override
    public void setVertices(float[] vertices) {
        attachmentB.setVertices(vertices); super.setVertices(vertices);
    }

    @Override
    public int getWorldVerticesLength() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getWorldVerticesLength(); else return super.getWorldVerticesLength();
    }

    @Override
    public void setWorldVerticesLength(int worldVerticesLength) {
        attachmentB.setWorldVerticesLength(worldVerticesLength); super.setWorldVerticesLength(worldVerticesLength);
    }

    @Override
    public Attachment getTimelineAttachment() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getTimelineAttachment(); else return super.getTimelineAttachment();
    }

    @Override
    public void setTimelineAttachment(Attachment timelineAttachment) {
        attachmentB.setTimelineAttachment(timelineAttachment); super.setTimelineAttachment(timelineAttachment);
    }

    @Override
    public int getId() {
        if (useAttachmentB != null && useAttachmentB.get()) return attachmentB.getId(); else return super.getId();
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
