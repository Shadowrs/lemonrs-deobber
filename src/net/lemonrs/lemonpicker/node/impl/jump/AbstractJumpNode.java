package net.lemonrs.lemonpicker.node.impl.jump;

import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import net.lemonrs.lemonpicker.node.impl.LabelNode;
import org.objectweb.asm.tree.JumpInsnNode;

/**
 * @author : const_
 */
public abstract class AbstractJumpNode extends BasicNode<JumpInsnNode> {

    public AbstractJumpNode(JumpInsnNode node) {
        super(node);
    }

    public <N extends BasicNode> N target() {
        org.objectweb.asm.tree.LabelNode target = node().label;
        LabelNode next = next(AbstractNode.LABEL_NODE);
        if (next != null) {
            if (next.node().equals(target)) {
                return (N) next;
            }
            while ((next = next.next(AbstractNode.LABEL_NODE)) != null) {
                if (next.node().equals(target)) {
                    return (N) next;
                }
            }
        }
        LabelNode prev = prev(AbstractNode.LABEL_NODE);
        if (prev != null) {
            if (prev.node().equals(target)) {
                return (N) prev;
            }
            while ((prev = prev.prev(AbstractNode.LABEL_NODE)) != null) {
                if (prev.node().equals(target)) {
                    return (N) prev;
                }
            }
        }
        return (N) new LabelNode(target);
    }
}
