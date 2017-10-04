package net.lemonrs.lemonpicker.identifier.impl.scene.object;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.renderable.RenderableIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.scene.TileIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;

/**
 * @author : const_
 */
public class GameObjectIdentifier extends AbstractClassIdentifier {

    public GameObjectIdentifier() {
        add(new Renderable());
    }

    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        ClassElement tile = Main.get(TileIdentifier.class).identified();
        return query.branchSize(1).classHasField(tile, "[", true).first();
    }


    public class Renderable extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement renderable = Main.get(RenderableIdentifier.class).identified();
            return new FieldQuery(GameObjectIdentifier.this.identified()).desc("L" + renderable.name() + ";").
                    member().first();
        }
    }
}
