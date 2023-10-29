package games.rednblack.h2d.extension.spine;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;

public class Box2DBoundingBoxAttachment extends BoundingBoxAttachment {

    public Vector2[] vertices;
    public Vector2[][] polygonizedVertices;

    public float minX, minY, maxX, maxY, width, height;

    public Box2DBoundingBoxAttachment(String name) {
        super(name);
    }

    protected Box2DBoundingBoxAttachment(BoundingBoxAttachment other) {
        super(other);
    }

    public void updateBoundingBox() {
        minX = getVertices()[0];
        minY = getVertices()[1];
        maxX = getVertices()[0];
        maxY = getVertices()[1];

        for (int i = 2; i < getVertices().length; i += 2) {
            float x = getVertices()[i];
            float y = getVertices()[i + 1];

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        width = maxX - minX;
        height = maxY - minY;
    }
}
