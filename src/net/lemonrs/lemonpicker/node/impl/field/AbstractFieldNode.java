package net.lemonrs.lemonpicker.node.impl.field;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.tree.FieldInsnNode;

/**
 * @author : const_
 */
public abstract class AbstractFieldNode extends BasicNode<FieldInsnNode> {

    public AbstractFieldNode(FieldInsnNode node) {
        super(node);
    }

    public String desc() {
        if (field() == null) {
            return node().desc;
        }
        return field().desc();
    }

    public String name() {
        return node().name;
    }

    public String owner() {
        return node().owner;
    }

    public ClassElement ownerClass() {
        return Main.get(owner());
    }

    public FieldElement field() {
        ClassElement element = ownerClass();
        if (element != null) {
            return element.findField(name());
        }
        return null;
    }
}
