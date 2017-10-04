package net.lemonrs.lemonpicker.identifier.impl;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;

/**
 * @author : const_
 */
public class FacadeIdentifier extends AbstractClassIdentifier {
    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasField("[I", false, 3).hasMethodReturns("I", false).first();
    }
}
