package net.lemonrs.lemonpicker.identifier.impl.scene;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;

/**
 * @author : const_
 */
public class CollisionMapIdentifier extends AbstractClassIdentifier {

    public CollisionMapIdentifier() {
        add(new Flags());
    }

    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasField("[[I", true, 1).hasField("I", true, 4).fieldCount(5, true).first();
    }

    public class Flags extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            return new FieldQuery(CollisionMapIdentifier.this.identified()).desc("[[I").member().first();
        }
    }
}
