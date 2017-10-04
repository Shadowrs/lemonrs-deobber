package net.lemonrs.lemonpicker.identifier.impl.cache;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.ClientIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.BagIdentifier;
import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.field.StaticFieldCallNode;
import net.lemonrs.lemonpicker.node.impl.field.VirtualFieldCallNode;
import net.lemonrs.lemonpicker.node.impl.type.CastNode;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.MethodQuery;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.Value;

/**
 * @author : const_
 */
public class WidgetNodeIdentifier extends AbstractClassIdentifier {

    public WidgetNodeIdentifier() {
        add(new Id());
    }

    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        return query.branchSize(2).hasField("Z", true, 1).hasField("I", true, 2).firstOnBranch(0);
    }

    public class Id extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            final FieldElement widgetNodeBag = Main.get(ClientIdentifier.class).widgetNodeBag.identified();
            final ClassElement widgetNode = Main.get(WidgetNodeIdentifier.class).identified();
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().hasCast(widgetNode.name()).
                    references(widgetNodeBag).returns("V").notMember()) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitVirtualFieldCall(VirtualFieldCallNode node) {
                        if(value.set()) {
                            return;
                        }
                        if(node.owner().equals(widgetNode.name()) && node.desc().equals("I")) {
                            value.set(node.field());
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
}
