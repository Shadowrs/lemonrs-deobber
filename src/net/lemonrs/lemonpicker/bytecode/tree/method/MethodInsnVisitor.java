package net.lemonrs.lemonpicker.bytecode.tree.method;

import net.lemonrs.lemonpicker.bytecode.tree.AbstractMethodVisitor;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author : const_
 */
public class MethodInsnVisitor extends AbstractMethodVisitor<MethodInsnNode> {

    @Override
    public void visitMethodInsn(int i, String s, String s2, String s3, boolean b) {
        super.visitMethodInsn(i, s, s2, s3, b);
        add(new MethodInsnNode(i, s, s2, s3));
    }
}
