package net.lemonrs.lemonpicker.graph.method;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.bytecode.tree.method.MethodInsnVisitor;
import net.lemonrs.lemonpicker.node.impl.method.*;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class MethodCall {

    private MethodInsnNode node;
    private MethodCall parent;
    private MethodElement element;
    private List<MethodCall> calls;

    public MethodCall(MethodInsnNode node, MethodCall parent) {
        this.node = node;
        this.parent = parent;
        ClassElement super_ = Main.get(node.owner);
        while (element == null && super_ != null) {
            element = super_.findMethod(node.name, node.desc);
            super_ = Main.get(super_.node().superName);
        }
    }

    public MethodCall(MethodInsnNode node) {
        this.node = node;
        ClassElement super_ = Main.get(node.owner);
        while (element == null && super_ != null) {
            element = super_.findMethod(node.name, node.desc);
            super_ = Main.get(super_.node().superName);
        }
    }

    public List<MethodCall> calls() {
        if (calls == null) {
            calls = new LinkedList<>();
            if (element != null) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitDynamicMethodCall(DynamicMethodCallNode node) {
                        calls.add(new MethodCall(node(), MethodCall.this));
                    }

                    @Override
                    public void visitInterfaceMethodCall(InterfaceMethodCallNode node) {
                        calls.add(new MethodCall(node(), MethodCall.this));
                    }

                    @Override
                    public void visitSpecialMethodCall(SpecialMethodCallNode node) {
                        calls.add(new MethodCall(node(), MethodCall.this));
                    }

                    @Override
                    public void visitStaticMethodCall(StaticMethodCallNode node) {
                        calls.add(new MethodCall(node(), MethodCall.this));
                    }

                    @Override
                    public void visitVirtualMethodCall(VirtualMethodCallNode node) {
                        calls.add(new MethodCall(node(), MethodCall.this));
                    }
                };
            }
        }
        return calls;
    }

    public boolean fromEntry() {
        return parent == null;
    }

    public MethodCall parent() {
        return parent;
    }

    public MethodInsnNode node() {
        return node;
    }
}
