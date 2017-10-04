package net.lemonrs.lemonpicker.bytecode.tree;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author : const_
 */
public class SimpleClassVisitor extends ClassVisitor {

    public SimpleClassVisitor() {
        super(Opcodes.ASM5);
    }

}
