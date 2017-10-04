package net.lemonrs.lemonpicker.identifier.impl.node;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.field.VirtualFieldCallNode;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;
import net.lemonrs.lemonpicker.query.impl.MethodQuery;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.Value;

/**
 * @author : const_
 */
public class DequeIdentifier extends AbstractClassIdentifier {

    private AbstractFieldIdentifier head;

    public DequeIdentifier() {
        add(head = new Head());
        add(new Current());
    }

    @Override
    public ClassElement identify() {
        ClassElement node = Main.get(NodeIdentifier.class).identified();
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasField("L" + node.name() + ";", true, 2).fieldCount(2).first();
    }

    public class Head extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final ClassElement nodeElement = Main.get(NodeIdentifier.class).identified();
            for(MethodElement element : new MethodQuery(DequeIdentifier.this.identified()).member()
                    .returns("L" + nodeElement.name() + ";")) {
                final Value<FieldElement> value = new Value<>();
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitVirtualFieldCall(VirtualFieldCallNode node) {
                        if(value.set()) {
                            return;
                        }
                        if(node.desc().equals("L" + nodeElement.name() + ";") &&
                                node.next().type() == AbstractNode.VIRTUAL_FIELD_CALL_NODE &&
                                node.ownerClass().name().equals(DequeIdentifier.this.identified().name())) {
                            VirtualFieldCallNode second = node.next();
                            if(second.desc().equals("L" + nodeElement.name() + ";")) {
                                value.set(node.field());
                            }
                        }
                    }
                };
                if(value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class Current extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement identified= Main.get(NodeIdentifier.class).identified();
            return new FieldQuery(DequeIdentifier.this.identified()).member().notNamed(head.identified().name())
                    .desc("L" + identified.name() + ";").first();
        }
    }
}
