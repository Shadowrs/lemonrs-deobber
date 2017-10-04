package net.lemonrs.lemonpicker.identifier.impl.scene.object;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.renderable.RenderableIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.scene.TileIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;

/**
 * @author : const_
 */
public class GroundLayerIdentifier extends AbstractClassIdentifier {

    private AbstractFieldIdentifier layer1, layer2;


    public GroundLayerIdentifier() {
        add(layer1 = new Layer1());
        add(layer2 = new Layer2());
        add(new Layer3());
    }

    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        ClassElement renderable = Main.get(RenderableIdentifier.class).identified();
        ClassElement tile = Main.get(TileIdentifier.class).identified();
        return query.branchSize(1).hasField("L" + renderable.name() + ";", true, 3)
                .hasField("I", true, 5).classHasField(tile, "", true).first();
    }

    public class Layer1 extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement renderable = Main.get(RenderableIdentifier.class).identified();
            return new FieldQuery(GroundLayerIdentifier.this.identified()).desc("L" + renderable.name() + ";").
                    member().first();
        }
    }

    public class Layer2 extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement renderable = Main.get(RenderableIdentifier.class).identified();
            return new FieldQuery(GroundLayerIdentifier.this.identified()).desc("L" + renderable.name() + ";").
                    member().notNamed(layer1.identified().name()).first();
        }
    }

    public class Layer3 extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement renderable = Main.get(RenderableIdentifier.class).identified();
            return new FieldQuery(GroundLayerIdentifier.this.identified()).desc("L" + renderable.name() + ";").
                    member().notNamed(layer1.identified().name()).notNamed(layer2.identified().name()).first();
        }
    }
}
