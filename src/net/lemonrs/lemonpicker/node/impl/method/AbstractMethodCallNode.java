package net.lemonrs.lemonpicker.node.impl.method;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author : const_
 */
public abstract class AbstractMethodCallNode extends BasicNode<MethodInsnNode> {

    public AbstractMethodCallNode(MethodInsnNode node) {
        super(node);
    }

    public String owner() {
        return node().owner;
    }

    public String desc() {
        return node().desc;
    }

    public String name() {
        return node().name;
    }

    public ClassElement ownerClass() {
        return Main.get(owner());
    }

    public MethodElement method() {
        ClassElement element = ownerClass();
        if (element != null) {
            return element.findMethod(name(), desc());
        }
        return null;
    }
}
