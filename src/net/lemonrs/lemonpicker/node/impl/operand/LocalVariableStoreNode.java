package net.lemonrs.lemonpicker.node.impl.operand;

import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * @author : const_
 */
public class LocalVariableStoreNode extends BasicNode<VarInsnNode> {

    public LocalVariableStoreNode(VarInsnNode node) {
        super(node);
    }

    public int index() {
        return node().var;
    }

    public String desc() {
        switch (opcode()) {
            case Opcodes.ISTORE:
                return "I";
            case Opcodes.ASTORE:
                return "Ljava/lang/Object;";
            case Opcodes.DSTORE:
                return "D";
            case Opcodes.FSTORE:
                return "F";
            case Opcodes.LSTORE:
                return "J";
        }
        return "null";
    }

    @Override
    public int type() {
        return AbstractNode.LOCAL_VARIABLE_STORE_NODE;
    }

}
