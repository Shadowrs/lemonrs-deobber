package net.lemonrs.lemonpicker.identifier.impl.node;

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
public class QueueIdentifier extends AbstractClassIdentifier {

    public QueueIdentifier() {
        add(new Head());
    }

    @Override
    public ClassElement identify() {
        ClassElement cacheableNode = Main.get(CacheableNodeIdentifier.class).identified();
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasField("L" + cacheableNode.name() + ";", true, 1)
                .notImplement("java/lang/Iterable").fieldCount(1).first();
    }

    public class Head extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            return new FieldQuery(QueueIdentifier.this.identified()).member().
                    desc("L" + Main.get(CacheableNodeIdentifier.class).identified().name() + ";").first();
        }
    }
}