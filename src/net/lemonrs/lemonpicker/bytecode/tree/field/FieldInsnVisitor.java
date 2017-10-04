package net.lemonrs.lemonpicker.bytecode.tree.field;

import net.lemonrs.lemonpicker.bytecode.tree.AbstractMethodVisitor;
import org.objectweb.asm.tree.FieldInsnNode;

/**
 * @author : const_
 */
public class FieldInsnVisitor extends AbstractMethodVisitor<FieldInsnNode> {

    @Override
    public void visitFieldInsn(int i, String s, String s2, String s3) {
        super.visitFieldInsn(i, s, s2, s3);
        add(new FieldInsnNode(i, s, s2, s3));
    }
}
