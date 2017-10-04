package net.lemonrs.lemonpicker.identifier.impl.cache;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.BagIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.CacheableNodeIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.NodeIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.QueueIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;

/**
 * @author : const_
 */
public class CacheIdentifier extends AbstractClassIdentifier {

    public CacheIdentifier() {
        add(new Bag());
        add(new Queue());
        add(new EmptyCacheableNode());
    }

    @Override
    public ClassElement identify() {
        ClassElement cacheableNode = Main.get(CacheableNodeIdentifier.class).identified();
        ClassElement bag = Main.get(BagIdentifier.class).identified();
        ClassElement queue = Main.get(QueueIdentifier.class).identified();
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasField("L" + cacheableNode.name() + ";", true, 1).hasField("L" + bag.name() + ";", true, 1)
                .fieldCount(5).hasField("L" + queue.name() + ";", true, 1).hasField("I", true, 2).first();
    }

    public class Bag extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement bag = Main.get(BagIdentifier.class).identified();
            return new FieldQuery(CacheIdentifier.this.identified()).member().desc("L" + bag.name() + ";").first();
        }
    }

    public class Queue extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement queue = Main.get(QueueIdentifier.class).identified();
            return new FieldQuery(CacheIdentifier.this.identified()).member().desc("L" + queue.name() + ";").first();
        }
    }

    public class EmptyCacheableNode extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement cacheableNode = Main.get(CacheableNodeIdentifier.class).identified();
            return new FieldQuery(CacheIdentifier.this.identified()).member().desc("L" + cacheableNode.name() + ";").first();
        }
    }
}
