package net.lemonrs.lemonpicker.node.impl.operand;

import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;

/**
 * @author : const_
 */
public class PushNode extends BasicNode<IntInsnNode> {

    public PushNode(IntInsnNode node) {
        super(node);
    }

    public int push() {
        return node().operand;
    }

    @Override
    public int type() {
        return AbstractNode.PUSH_NODE;
    }


}
