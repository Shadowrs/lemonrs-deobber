package net.lemonrs.lemonpicker.node.impl;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;

/**
 * @author : const_
 */
public class ReturnNode extends BasicNode<InsnNode> {

    public ReturnNode(InsnNode node) {
        super(node);
    }

    public enum ReturnType {
        OBJECT, VOID, INTEGER, FLOAT, DOUBLE, LONG;
    }

    public ReturnType returnType() {
        switch (opcode()) {
            case Opcodes.ARETURN:
                return ReturnType.OBJECT;
            case Opcodes.IRETURN:
                return ReturnType.INTEGER;
            case Opcodes.FRETURN:
                return ReturnType.FLOAT;
            case Opcodes.DRETURN:
                return ReturnType.DOUBLE;
            case Opcodes.LRETURN:
                return ReturnType.LONG;
            case Opcodes.RETURN:
                return ReturnType.VOID;
        }
        return null;
    }

    @Override
    public int type() {
        return AbstractNode.RETURN_NODE;
    }

}
