package net.lemonrs.lemonpicker.identifier.impl.renderable;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.ClientIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.PlayerDefinitionIdentifier;
import net.lemonrs.lemonpicker.node.impl.field.StaticFieldCallNode;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.Flag;

/**
 * @author : const_
 */
public class PlayerIdentifier extends AbstractClassIdentifier {

    public PlayerIdentifier() {
        add(new Definition());
        add(new Name());
    }

    public class Name extends AbstractFieldIdentifier{
        @Override
        public FieldElement identify() {
            return new FieldQuery(PlayerIdentifier.this.identified()).desc("Ljava/lang/String;").member().first();
        }
    }

    @Override
    public ClassElement identify() {
        ClassElement client = Main.get(ClientIdentifier.class).identified();
        ClassQuery playerQuery = new ClassQuery();
        final Flag playerArray = new Flag();
        final Flag localPlayer = new Flag();
        for (final ClassElement possiblePlayer : playerQuery.branchSize(5).allOnBranch(0)) {
            playerArray.unflag();
            localPlayer.unflag();
            for (MethodElement element : client.methods()) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (node.desc().equals("[L" + possiblePlayer.name() + ";")) {
                            playerArray.flag();
                        }
                        if (node.desc().equals("L" + possiblePlayer.name() + ";")) {
                            localPlayer.flag();
                        }
                    }
                };
                if (localPlayer.flagged() && playerArray.flagged()) {
                    return possiblePlayer;
                }
            }
        }
        return null;
    }

    private class Definition extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            FieldQuery query = new FieldQuery(PlayerIdentifier.this.identified());
            ClassElement definition = Main.get(PlayerDefinitionIdentifier.class).identified();
            return query.desc("L" + definition.name() + ";").member().first();
        }
    }
}
