package net.lemonrs.lemonpicker.node.impl;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

/**
 * @author : const_
 */
public class DuplicateNode extends BasicNode<InsnNode> {

    public DuplicateNode(InsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.DUPLICATE_NODE;
    }
}
