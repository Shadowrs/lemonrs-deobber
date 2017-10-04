package net.lemonrs.lemonpicker.identifier.impl.cache.definition;

import com.sun.swing.internal.plaf.metal.resources.metal;
import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.graph.flow.Block;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.net.StreamIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.renderable.NpcIdentifier;
import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.field.VirtualFieldStoreNode;
import net.lemonrs.lemonpicker.node.impl.jump.IfConditionNode;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;
import net.lemonrs.lemonpicker.query.impl.MethodQuery;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.Value;

/**
 * @author : const_
 */
public class NpcDefinitionIdentifier extends AbstractClassIdentifier {

    public NpcDefinitionIdentifier() {
        add(new ModelIds());
        add(new Name());
    }

    @Override
    public ClassElement identify() {
        ClassElement npc = Main.get(NpcIdentifier.class).identified();
        ClassQuery query = new ClassQuery();
        return query.branchSize(3).hasField("[Ljava/lang/String;", true)
                .hasField("[S", true).classHasField(npc, "", true).firstOnBranch(0);
    }

    public class Name extends AbstractFieldIdentifier{
        @Override
        public FieldElement identify() {
            return new FieldQuery(NpcDefinitionIdentifier.this.identified()).desc("Ljava/lang/String;").member().first();
        }
    }

    public class ModelIds extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement stream = Main.get(StreamIdentifier.class).identified();
            for (final MethodElement element : new MethodQuery(NpcDefinitionIdentifier.this.identified().methods())
                    .takes("L" + stream.name() + ";").takes("I")) {
                final Value<FieldElement> field = new Value<>();
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitIfCondition(IfConditionNode node) {
                        if (field.set()) {
                            return;
                        }
                        if (node.conditon().hasConstant() && node.conditon().comparison().constant().value().equals(1)) {
                            AbstractNode target = node.conditon().trueTarget();
                            VirtualFieldStoreNode store = (VirtualFieldStoreNode) target.next(AbstractNode.VIRTUAL_FIELD_STORE_NODE);
                            while (store != null) {
                                if (field.set()) {
                                    break;
                                }
                                if (store.field().desc().equals("[I")) {
                                    field.set(store.field());
                                }
                                store = (VirtualFieldStoreNode) store.next(AbstractNode.VIRTUAL_FIELD_STORE_NODE);
                            }
                            if (store != null && store.field().desc().equals("[I")) {
                                field.set(store.field());
                            }
                        }
                    }
                };
                if (field.set()) {
                    return field.value();
                }
            }
            return null;
        }
    }
}
