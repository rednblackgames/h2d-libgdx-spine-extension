package games.rednblack.h2d.extention.spine;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import games.rednblack.editor.renderer.components.SpineDataComponent;

@All({SpineDataComponent.class, SpineObjectComponent.class})
public class SpineSystem extends IteratingSystem {

    protected ComponentMapper<SpineObjectComponent> spineObjectComponentMapper;

    @Override
    protected void process(int entity) {
        SpineObjectComponent spineObjectComponent = spineObjectComponentMapper.get(entity);

        spineObjectComponent.state.update(world.getDelta()); // Update the animation time.
        spineObjectComponent.state.apply(spineObjectComponent.skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.

        spineObjectComponent.skeleton.updateWorldTransform(); //
    }
}
