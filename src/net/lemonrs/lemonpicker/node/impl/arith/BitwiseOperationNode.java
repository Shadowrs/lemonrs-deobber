package net.lemonrs.lemonpicker.node.impl.arith;

import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;

/**
 * @author : const_
 */
public class BitwiseOperationNode extends BasicNode<InsnNode> {

    public BitwiseOperationNode(InsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.BITWISE_OPERATION_NODE;
    }

    public enum Operation {
        SHIFT_RIGHT(">"), SHIFT_LEFT("<"), UNSIGNED_SHIFT_RIGHT(">>"),
        XOR("^"), OR("|"), AND("&");

        private String operator;

        Operation(String operator) {
            this.operator = operator;
        }

        public String operator() {
            return operator;
        }
    }

    public Operation operation() {
        switch (opcode()) {
            case Opcodes.ISHL:
            case Opcodes.LSHL:
                return Operation.SHIFT_LEFT;
            case Opcodes.ISHR:
            case Opcodes.LSHR:
                return Operation.SHIFT_RIGHT;
            case Opcodes.IXOR:
            case Opcodes.LXOR:
                return Operation.XOR;
            case Opcodes.IOR:
            case Opcodes.LOR:
                return Operation.OR;
            case Opcodes.IAND:
            case Opcodes.LAND:
                return Operation.AND;
            case Opcodes.IUSHR:
            case Opcodes.LUSHR:
                return Operation.UNSIGNED_SHIFT_RIGHT;
        }
        return null;
    }
}
