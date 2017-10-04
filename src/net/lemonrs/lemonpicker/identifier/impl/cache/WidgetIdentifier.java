package net.lemonrs.lemonpicker.identifier.impl.cache;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;

/**
 * @author : const_
 */
public class WidgetIdentifier extends AbstractClassIdentifier {

    public WidgetIdentifier() {
        add(new Children());
    }

    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        for (ClassElement possible : query.branchSize(2).hasField("[[I", true).hasFieldLeast("[Ljava/lang/Object;", true, 12).allOnBranch(0)) {
            if (possible.hasField("[L" + possible.name() + ";", true)) {
                return possible;
            }
        }
        return null;
    }

    public class Children extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            return new FieldQuery(WidgetIdentifier.this.identified()).member()
                    .desc("[L" + WidgetIdentifier.this.identified().name() + ";").first();
        }
    }
}
