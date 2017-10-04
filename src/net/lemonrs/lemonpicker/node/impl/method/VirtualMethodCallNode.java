package net.lemonrs.lemonpicker.node.impl.method;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author : const_
 */
public class VirtualMethodCallNode extends AbstractMethodCallNode {

    public VirtualMethodCallNode(MethodInsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.VIRTUAL_METHOD_CALL_NODE;
    }
}
