package games.rednblack.h2d.extention.spine;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import games.rednblack.editor.renderer.components.SpineDataComponent;

public class SpineSystem extends IteratingSystem {

    private ComponentMapper<SpineObjectComponent> spineObjectComponentMapper = ComponentMapper.getFor(SpineObjectComponent.class);

    public SpineSystem() {
        super(Family.all(SpineDataComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpineObjectComponent spineObjectComponent = spineObjectComponentMapper.get(entity);

        spineObjectComponent.state.update(deltaTime); // Update the animation time.
        spineObjectComponent.state.apply(spineObjectComponent.skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.

        spineObjectComponent.skeleton.updateWorldTransform(); //
    }
}
