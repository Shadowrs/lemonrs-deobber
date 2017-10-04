package net.lemonrs.lemonpicker.identifier.impl.node;

import net.lemonrs.lemonpicker.Main;
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
public class BagIdentifier extends AbstractClassIdentifier {

    private AbstractFieldIdentifier next;
    public BagIdentifier() {
        add(new Cache());
        add(next = new Next());
        add(new Previous());
    }

    @Override
    public ClassElement identify() {
        ClassElement node = Main.get(NodeIdentifier.class).identified();
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasField("[L" + node.name() + ";", true, 1).hasField("L" + node.name() + ";", true, 2)
                .fieldCount(5).hasField("I", true, 2).first();
    }

    public class Cache extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement node = Main.get(NodeIdentifier.class).identified();
            return new FieldQuery(BagIdentifier.this.identified()).desc("[L" + node.name() + ";").member().first();
        }
    }

    public class Next extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final ClassElement identified = Main.get(NodeIdentifier.class).identified();
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery(BagIdentifier.this.identified()).member()
                    .returns("V")) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitIfCondition(IfConditionNode node) {
                        if(value.set()) {
                            return;
                        }
                        if (node.conditon().hasField() && node.conditon().comparison().field().member() &&
                                node.conditon().comparison().field().desc().equals("L" + identified.name() + ";")) {
                            value.set(node.conditon().comparison().field());
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

    public class Previous extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement identified= Main.get(NodeIdentifier.class).identified();
            return new FieldQuery(BagIdentifier.this.identified()).member().notNamed(next.identified().name())
                    .desc("L" + identified.name() + ";").first();
        }
    }
}
