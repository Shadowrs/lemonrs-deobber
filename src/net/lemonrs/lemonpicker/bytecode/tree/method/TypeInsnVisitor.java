package net.lemonrs.lemonpicker.bytecode.tree.method;

import net.lemonrs.lemonpicker.bytecode.tree.AbstractMethodVisitor;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * @author : const_
 */
public class TypeInsnVisitor extends AbstractMethodVisitor<TypeInsnNode> {

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
        add(new TypeInsnNode(opcode, type));
    }
}
