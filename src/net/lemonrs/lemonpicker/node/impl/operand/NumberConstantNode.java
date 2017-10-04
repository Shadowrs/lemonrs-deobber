package net.lemonrs.lemonpicker.node.impl.operand;

import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * @author : const_
 */
public class NumberConstantNode extends BasicNode<AbstractInsnNode> {

    public NumberConstantNode(AbstractInsnNode node) {
        super(node);
    }

    public Number value() {
        switch (opcode()) {
            case Opcodes.ICONST_0:
                return 0;
            case Opcodes.ICONST_1:
                return 1;
            case Opcodes.ICONST_2:
                return 2;
            case Opcodes.ICONST_3:
                return 3;
            case Opcodes.ICONST_4:
                return 4;
            case Opcodes.ICONST_5:
                return 5;
            case Opcodes.ICONST_M1:
                return -1;
            case Opcodes.FCONST_0:
                return 0.0;
            case Opcodes.FCONST_1:
                return 1.0;
            case Opcodes.FCONST_2:
                return 2.0;
            case Opcodes.DCONST_0:
                return 0.0;
            case Opcodes.DCONST_1:
                return 1.0;
            case Opcodes.LCONST_0:
                return 0L;
            case Opcodes.LCONST_1:
                return 1L;
        }
        return -2;
    }

    @Override
    public int type() {
        return AbstractNode.NUMBER_CONSTANT_NODE;
    }

}
