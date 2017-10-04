package net.lemonrs.lemonpicker.identifier.impl.cache.definition;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.renderable.PlayerIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;

/**
 * @author : const_
 */
public class PlayerDefinitionIdentifier extends AbstractClassIdentifier {
    @Override
    public ClassElement identify() {
        ClassElement player = Main.get(PlayerIdentifier.class).identified();
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasField("[I", true, 2)
                .hasField("J", true, 2).classHasField(player, "", true).first();
    }
}
