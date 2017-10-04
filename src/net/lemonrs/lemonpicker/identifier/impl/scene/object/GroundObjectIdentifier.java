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
public class GroundObjectIdentifier extends AbstractClassIdentifier {

    public GroundObjectIdentifier() {
        add(new Renderable());
    }
    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        ClassElement renderable = Main.get(RenderableIdentifier.class).identified();
        ClassElement tile = Main.get(TileIdentifier.class).identified();
        return query.branchSize(1).hasField("L" + renderable.name() + ";", true, 1)
                .hasField("I", true, 5).classHasField(tile, "", true).first();
    }

    public class Renderable extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement renderable = Main.get(RenderableIdentifier.class).identified();
            return new FieldQuery(GroundObjectIdentifier.this.identified()).desc("L" + renderable.name() + ";").
                    member().first();
        }
    }
}
