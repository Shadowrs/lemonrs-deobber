package net.lemonrs.lemonpicker.identifier.impl.input;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;

/**
 * @author : const_
 */
public class MouseIdentifier extends AbstractClassIdentifier {
    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).implement("java/awt/event/MouseListener")
                .implement("java/awt/event/MouseMotionListener").first();
    }
}
