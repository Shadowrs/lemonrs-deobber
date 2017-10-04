package net.lemonrs.lemonpicker.bytecode.tree;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author : const_
 */
public abstract class AbstractMethodVisitor<T extends AbstractInsnNode> extends MethodVisitor {

    private List<T> insns = new LinkedList<>();
    private ListIterator<T> iterator;

    public AbstractMethodVisitor() {
        super(Opcodes.ASM5);
    }

    public void add(T node) {
        insns.add(node);
    }

    public List<T> instructions() {
        return insns;
    }

    public ListIterator<T> iterator() {
        return insns.listIterator();
    }

    public T first() {
        return iterator().next();
    }

    public T next() {
        if(iterator == null) {
            iterator = iterator();
        }
        return iterator().next();
    }
}
