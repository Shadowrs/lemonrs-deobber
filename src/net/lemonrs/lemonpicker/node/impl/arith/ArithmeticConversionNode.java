package net.lemonrs.lemonpicker.node.impl.arith;

import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.tree.InsnNode;

/**
 * @author : const_
 */
public class ArithmeticConversionNode extends BasicNode<InsnNode> {

    public ArithmeticConversionNode(InsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.ARITHMETIC_CONVERSION_NODE;
    }

}
