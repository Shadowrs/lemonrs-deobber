package net.lemonrs.lemonpicker.node.impl;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * @author : const_
 */
public class BasicNode<T extends AbstractInsnNode> extends AbstractNode<T> {

    public BasicNode(T node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.BASIC_NODE;
    }
}
