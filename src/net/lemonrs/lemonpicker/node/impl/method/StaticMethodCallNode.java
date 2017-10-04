package net.lemonrs.lemonpicker.node.impl.method;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author : const_
 */
public class StaticMethodCallNode extends AbstractMethodCallNode {

    public StaticMethodCallNode(MethodInsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.STATIC_METHOD_CALL_NODE;
    }
}
