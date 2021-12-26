package games.rednblack.h2d.extension.spine;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;

@All({SpineComponent.class})
public class SpineSystem extends IteratingSystem {

    protected ComponentMapper<SpineComponent> spineObjectComponentMapper;

    @Override
    protected void process(int entity) {
        SpineComponent spineObjectComponent = spineObjectComponentMapper.get(entity);

        spineObjectComponent.state.update(world.getDelta()); // Update the animation time.
        spineObjectComponent.state.apply(spineObjectComponent.skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.

        spineObjectComponent.skeleton.updateWorldTransform(); //
    }
}
