package net.lemonrs.lemonpicker.node.impl.operand;

import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * @author : const_
 */
public class LdcNode extends BasicNode<LdcInsnNode> {

    public LdcNode(LdcInsnNode node) {
        super(node);
    }

    public Object value() {
        return node().cst;
    }

    @Override
    public int type() {
        return AbstractNode.LDC_NODE;
    }

}
