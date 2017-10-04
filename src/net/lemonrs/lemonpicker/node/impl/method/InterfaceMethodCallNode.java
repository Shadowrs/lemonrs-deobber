package net.lemonrs.lemonpicker.node.impl.method;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author : const_
 */
public class InterfaceMethodCallNode extends AbstractMethodCallNode{

    public InterfaceMethodCallNode(MethodInsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.INTERFACE_METHOD_CALL_NODE;
    }
}
