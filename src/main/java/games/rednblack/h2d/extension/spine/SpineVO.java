package games.rednblack.h2d.extension.spine;

import com.artemis.World;
import games.rednblack.editor.renderer.data.MainItemVO;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class SpineVO extends MainItemVO {

    public String animationName = "";
    public String currentAnimationName = "";

    public SpineVO() {

    }

    public SpineVO(SpineVO vo) {
        super(vo);
        animationName = vo.animationName;
        currentAnimationName = vo.currentAnimationName;
    }

    @Override
    public void loadFromEntity(int entity, World engine, EntityFactory entityFactory) {
        super.loadFromEntity(entity, engine, entityFactory);

        SpineComponent spineComponent = ComponentRetriever.get(entity, SpineComponent.class, engine);
        animationName = spineComponent.animationName;
        currentAnimationName = spineComponent.currentAnimationName;
    }

    @Override
    public String getResourceName() {
        return animationName;
    }
}