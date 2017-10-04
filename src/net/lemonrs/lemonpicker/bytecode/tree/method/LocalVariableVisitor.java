package net.lemonrs.lemonpicker.bytecode.tree.method;

import net.lemonrs.lemonpicker.bytecode.tree.AbstractMethodVisitor;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * @author : const_
 */
public class LocalVariableVisitor extends AbstractMethodVisitor<VarInsnNode> {

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        add(new VarInsnNode(opcode, var));
    }
}
