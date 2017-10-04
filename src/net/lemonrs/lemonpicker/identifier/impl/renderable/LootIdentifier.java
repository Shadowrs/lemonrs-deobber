package net.lemonrs.lemonpicker.identifier.impl.renderable;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.ModelIdentifier;
import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.field.VirtualFieldCallNode;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.MethodQuery;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.Value;

/**
 * @author : const_
 */
public class LootIdentifier extends AbstractClassIdentifier {

    public LootIdentifier() {
        add(new Id());
        add(new StackSize());
    }

    @Override
    public ClassElement identify() {
        ClassElement renderable = Main.get(RenderableIdentifier.class).identified();
        ClassQuery query = new ClassQuery();
        return query.branchSize(4).hasField("I", true, 2).fieldCount(2).extend(renderable.name()).firstOnBranch(0);
    }

    public class Id extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement model = Main.get(ModelIdentifier.class).identified();
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery(LootIdentifier.this.identified()).
                    returns("L" + model.name() + ";").member()) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitVirtualFieldCall(VirtualFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.ownerClass().name().equals(LootIdentifier.this.identified().name()) &&
                                node.desc().equals("I")) {
                            VirtualFieldCallNode next = node.next(AbstractNode.VIRTUAL_FIELD_CALL_NODE);
                            if (next != null && next.desc().equals("I")
                                    && next.ownerClass().name().equals(LootIdentifier.this.identified().name())) {
                                value.set(node.field());
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class StackSize extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement model = Main.get(ModelIdentifier.class).identified();
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery(LootIdentifier.this.identified()).
                    returns("L" + model.name() + ";").member()) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitVirtualFieldCall(VirtualFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.ownerClass().name().equals(LootIdentifier.this.identified().name()) &&
                                node.desc().equals("I") && node.next(AbstractNode.VIRTUAL_FIELD_CALL_NODE) == null) {
                            value.set(node.field());
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }
}
