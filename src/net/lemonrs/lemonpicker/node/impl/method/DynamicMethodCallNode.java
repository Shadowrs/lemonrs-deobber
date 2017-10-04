package net.lemonrs.lemonpicker.node.impl.method;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author : const_
 */
public class DynamicMethodCallNode extends AbstractMethodCallNode {

    public DynamicMethodCallNode(MethodInsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.DYNAMIC_METHOD_CALL_NODE;
    }
}
