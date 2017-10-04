package net.lemonrs.lemonpicker.bytecode.tree;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.bytecode.tree.field.FieldInsnVisitor;
import net.lemonrs.lemonpicker.bytecode.tree.method.MethodInsnVisitor;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;

/**
 * @author : const_
 */
public class FlowGraph {

    private ClassElement element;
    private MethodElement method;

    public FlowGraph(ClassElement element, MethodElement method) {
        this.element = element;
        this.method = method;
    }

    public MethodVisitor visit() {
        return new MethodVisitor(method);
    }
    
    public <E extends AbstractMethodVisitor> E visit(Class<E> visitor) {
        try {
            E instance = visitor.newInstance();
            method.node().accept(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
