package net.lemonrs.lemonpicker.node.impl.jump;

import net.lemonrs.lemonpicker.graph.condition.Comparison;
import net.lemonrs.lemonpicker.graph.condition.Condition;
import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.JumpInsnNode;

/**
 * @author : const_
 */
public class IfConditionNode extends AbstractJumpNode {

    private Condition condition;

    public IfConditionNode(JumpInsnNode node) {
        super(node);
    }

    public String operator() {
        switch (opcode()) {
            case Opcodes.IFNULL:
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IFEQ:
            case Opcodes.IF_ICMPEQ:
                return "==";
            case Opcodes.IF_ACMPNE:
            case Opcodes.IF_ICMPNE:
            case Opcodes.IFNE:
            case Opcodes.IFNONNULL:
                return "!=";
            case Opcodes.IF_ICMPGE:
            case Opcodes.IFGE:
                return ">=";
            case Opcodes.IF_ICMPGT:
            case Opcodes.IFGT:
                return ">";
            case Opcodes.IF_ICMPLE:
            case Opcodes.IFLE:
                return "<=";
            case Opcodes.IFLT:
            case Opcodes.IF_ICMPLT:
                return "<";
        }
        return "null";
    }

    public Condition conditon() {
        if(condition == null) {
            condition = new Condition<>(this);
        }
        return condition;
    }

    public String definition() {
        switch (opcode()) {
            case Opcodes.IF_ACMPEQ:
                return "If the two values are equal(==) branches to label: " + target();
            case Opcodes.IF_ACMPNE:
                return "If the two values are NOT equal(!=) branches to label: " + target();
            case Opcodes.IF_ICMPEQ:
                return "If two int values are equal (==) branches to label: " + target();
            case Opcodes.IF_ICMPGE:
                return "If first value is greater than or equal to (>=) second value branches to label: " + target();
            case Opcodes.IF_ICMPGT:
                return "If first value is greater than (>) second value branches to label: " + target();
            case Opcodes.IF_ICMPLE:
                return "If first value is less than or equal to(<=) second value branches to label: " + target();
            case Opcodes.IF_ICMPLT:
                return "If first value is less than (<) second value branches to label: " + target();
            case Opcodes.IF_ICMPNE:
                return "If the two values are not equal (!=) branches to label: " + target();
            case Opcodes.IFEQ:
                return "If the value is equal to 0 (==) branches to label: " + target();
            case Opcodes.IFGE:
                return "If the value is greater than or equal to 0 (>=) branches to label: " + target();
            case Opcodes.IFGT:
                return "If the value is greater than 0 (>) branches to label: " + target();
            case Opcodes.IFLE:
                return "If the value is less than or equal to 0 (<=) branches to label: " + target();
            case Opcodes.IFLT:
                return "If the value is less than 0 (<) branches to label: " + target();
            case Opcodes.IFNE:
                return "If the value is NOT equal to 0 (!=) branches to label: " + target();
            case Opcodes.IFNONNULL:
                return "If the value is not null (!=) branches to label: " + target();
            case Opcodes.IFNULL:
                return "If the value is null (==) branches to label: " + target();
        }
        return "null";
    }

    @Override
    public int type() {
        return AbstractNode.IF_CONDITION_NODE;
    }
}
