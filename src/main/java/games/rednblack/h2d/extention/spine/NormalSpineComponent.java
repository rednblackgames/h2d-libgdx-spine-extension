package games.rednblack.h2d.extention.spine;

import com.esotericsoftware.spine.Skin;
import games.rednblack.editor.renderer.components.BaseComponent;

public class NormalSpineComponent implements BaseComponent {
    public Skin normalSkin = null;

    @Override
    public void reset() {
        normalSkin = null;
    }
}
