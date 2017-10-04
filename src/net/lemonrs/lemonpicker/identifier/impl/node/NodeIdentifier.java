package net.lemonrs.lemonpicker.identifier.impl.node;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.node.impl.jump.IfConditionNode;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;
import net.lemonrs.lemonpicker.query.impl.MethodQuery;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.Value;

/**
 * @author : const_
 */
public class NodeIdentifier extends AbstractClassIdentifier {

    private AbstractFieldIdentifier next;

    public NodeIdentifier() {
        add(next = new Next());
        add(new Previous());
        add(new Uid());
    }

    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        return query.branchSize(5).firstOnBranch(4);
    }

    public class Next extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final ClassElement identified = NodeIdentifier.this.identified();
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery(identified).member()
                    .returns("V")) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitIfCondition(IfConditionNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.conditon().hasField() && node.conditon().comparison().field().member() &&
                                node.conditon().comparison().field().desc().equals("L" + identified.name() + ";")) {
                            value.set(node.conditon().comparison().field());
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

    public class Previous extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement identified = NodeIdentifier.this.identified();
            return new FieldQuery(identified).member().notNamed(next.identified().name())
                    .desc("L" + identified.name() + ";").first();
        }
    }

    public class Uid extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            return new FieldQuery(NodeIdentifier.this.identified()).member().desc("J").first();
        }
    }
}
