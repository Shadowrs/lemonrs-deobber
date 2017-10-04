package net.lemonrs.lemonpicker.identifier.impl.renderable;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.ClientIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.NpcDefinitionIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.PlayerDefinitionIdentifier;
import net.lemonrs.lemonpicker.node.impl.field.StaticFieldCallNode;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.Flag;

/**
 * @author : const_
 */
public class NpcIdentifier extends AbstractClassIdentifier {

    public NpcIdentifier() {
        add(new Definition());
    }

    @Override
    public ClassElement identify() {
        ClassElement client = Main.get(ClientIdentifier.class).identified();
        ClassQuery npcQuery = new ClassQuery();
        final Flag npcArray = new Flag();
        for (final ClassElement possibleNpc : npcQuery.branchSize(5)
                .notNamed(Main.get(PlayerIdentifier.class).identified().name()).allOnBranch(0)) {
            npcArray.unflag();
            for (MethodElement element : client.methods()) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (node.desc().equals("[L" + possibleNpc.name() + ";")) {
                            npcArray.flag();
                        }
                    }
                };
                if (npcArray.flagged()) {
                    return possibleNpc;
                }
            }
        }
        return null;
    }


    private class Definition extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            FieldQuery query = new FieldQuery(NpcIdentifier.this.identified());
            ClassElement definition = Main.get(NpcDefinitionIdentifier.class).identified();
            return query.desc("L" + definition.name() + ";").member().first();
        }
    }
}
