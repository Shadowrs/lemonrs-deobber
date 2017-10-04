package net.lemonrs.lemonpicker.identifier.impl.scene;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;

/**
 * @author : const_
 */
public class RegionIdentifier extends AbstractClassIdentifier {

    public RegionIdentifier() {
        add(new Tiles());
    }

    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasFieldDescStartWith("[[[L", true).hasFieldDescStartWith("[L", true).first();
    }

    public class Tiles extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            ClassElement tile = Main.get(TileIdentifier.class).identified();
            return new FieldQuery(RegionIdentifier.this.identified()).desc("[[[L" + tile.name() + ";").member().first();
        }
    }
}
