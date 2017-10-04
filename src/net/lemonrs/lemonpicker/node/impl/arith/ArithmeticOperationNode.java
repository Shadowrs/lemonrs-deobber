package net.lemonrs.lemonpicker.node.impl.arith;

import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

/**
 * @author : const_
 */
public class ArithmeticOperationNode extends BasicNode<InsnNode> {

    public ArithmeticOperationNode(InsnNode node) {
        super(node);
    }

    public enum Operation {
        ADD("+"), SUBTRACT("-"), DIVIDE("/"), MULTIPLY("*");
        private String operator;

        Operation(String operator) {
            this.operator = operator;
        }

        public String operator() {
            return operator;
        }
    }

    @Override
    public int type() {
        return AbstractNode.ARITHMETIC_OPERATION_NODE;
    }

    public Operation operation() {
        switch (opcode()) {
            case Opcodes.IMUL:
            case Opcodes.LMUL:
            case Opcodes.DMUL:
            case Opcodes.FMUL:
                return Operation.MULTIPLY;
            case Opcodes.IADD:
            case Opcodes.LADD:
            case Opcodes.DADD:
            case Opcodes.FADD:
                return Operation.ADD;
            case Opcodes.ISUB:
            case Opcodes.LSUB:
            case Opcodes.DSUB:
            case Opcodes.FSUB:
                return Operation.SUBTRACT;
            case Opcodes.IDIV:
            case Opcodes.LDIV:
            case Opcodes.DDIV:
            case Opcodes.FDIV:
                return Operation.DIVIDE;
        }
        return null;
    }
}
