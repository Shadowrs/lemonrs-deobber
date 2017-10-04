package net.lemonrs.lemonpicker.identifier.impl.cache.definition;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;

/**
 * @author : const_
 */
public class ObjectDefinitionIdentifier extends AbstractClassIdentifier {

    @Override
    public ClassElement identify() {
        ClassElement playerDefinition = Main.get(PlayerDefinitionIdentifier.class).identified();
        ClassElement npcDefinition = Main.get(NpcDefinitionIdentifier.class).identified();
        ClassElement itemDefinition = Main.get(ItemDefinitionIdentifier.class).identified();
        ClassQuery query = new ClassQuery();
        return query.branchSize(3).notNamed(playerDefinition.name()).notNamed(npcDefinition.name())
                .notNamed(itemDefinition.name()).hasField("[Ljava/lang/String;", true).hasField("[S", true).firstOnBranch(0);
    }
}
