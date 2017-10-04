package net.lemonrs.lemonpicker.node.impl.jump;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.JumpInsnNode;

/**
 * @author : const_
 */
public class GotoNode extends AbstractJumpNode {

    public GotoNode(JumpInsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.GOTO_NODE;
    }
}
