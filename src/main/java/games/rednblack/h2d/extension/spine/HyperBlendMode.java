package games.rednblack.h2d.extension.spine;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.esotericsoftware.spine.BlendMode;

public class HyperBlendMode {
    public static void apply(BlendMode blendMode, Batch batch, boolean premultipliedAlpha) {
        if (blendMode == BlendMode.normal) {
            batch.setBlendFunctionSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_ONE);
        } else {
            blendMode.apply(batch, premultipliedAlpha);
        }
    }
}
