package net.lemonrs.lemonpicker.identifier.impl.renderable;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;

/**
 * @author : const_
 */
public class RenderableIdentifier extends AbstractClassIdentifier {

    public RenderableIdentifier() {
        add(new Height());
    }

    @Override
    public ClassElement identify() {
        ClassElement player = Main.get(PlayerIdentifier.class).identified();
        ClassQuery query = new ClassQuery();
        return query.branchSize(5).onBranchAt(player, 0).firstOnBranch(2);
    }

    public class Height extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            return new FieldQuery(RenderableIdentifier.this.identified()).member().desc("I").first();
        }
    }
}
