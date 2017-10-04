package net.lemonrs.lemonpicker.identifier.impl.scene;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.scene.object.*;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;

/**
 * @author : const_
 */
public class TileIdentifier extends AbstractClassIdentifier {

    public TileIdentifier() {
        add(new GameObjects());
        add(new BoundaryObject());
        add(new GroundLayer());
        add(new GroundObject());
        add(new WallObject());
    }

    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        ClassElement region = Main.get(RegionIdentifier.class).identified();
        return query.branchSize(2).classHasField(region, "[[[", true).firstOnBranch(0);
    }

    public class GameObjects extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            ClassElement gameObject = Main.get(GameObjectIdentifier.class).identified();
            return new FieldQuery(TileIdentifier.this.identified()).desc("[L" + gameObject.name() + ";").member().first();
        }
    }

    public class BoundaryObject extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            ClassElement object = Main.get(BoundaryObjectIdentifier.class).identified();
            return new FieldQuery(TileIdentifier.this.identified()).desc("L" + object.name() + ";").member().first();
        }
    }

    public class GroundLayer extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            ClassElement object = Main.get(GroundLayerIdentifier.class).identified();
            return new FieldQuery(TileIdentifier.this.identified()).desc("L" + object.name() + ";").member().first();
        }
    }

    public class GroundObject extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            ClassElement object = Main.get(GroundObjectIdentifier.class).identified();
            return new FieldQuery(TileIdentifier.this.identified()).desc("L" + object.name() + ";").member().first();
        }
    }

    public class WallObject extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            ClassElement object = Main.get(WallObjectIdentifier.class).identified();
            return new FieldQuery(TileIdentifier.this.identified()).desc("L" + object.name() + ";").member().first();
        }
    }
}
