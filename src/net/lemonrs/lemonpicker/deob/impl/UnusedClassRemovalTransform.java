package net.lemonrs.lemonpicker.deob.impl;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.deob.Transform;
import net.lemonrs.lemonpicker.node.impl.field.StaticFieldCallNode;
import net.lemonrs.lemonpicker.node.impl.field.StaticFieldStoreNode;
import net.lemonrs.lemonpicker.node.impl.field.VirtualFieldCallNode;
import net.lemonrs.lemonpicker.node.impl.field.VirtualFieldStoreNode;
import net.lemonrs.lemonpicker.node.impl.method.*;
import net.lemonrs.lemonpicker.node.impl.type.ANewArrayNode;
import net.lemonrs.lemonpicker.node.impl.type.CastNode;
import net.lemonrs.lemonpicker.node.impl.type.InstanceOfNode;
import net.lemonrs.lemonpicker.node.impl.type.NewNode;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.ASMUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class UnusedClassRemovalTransform extends Transform {

    @Override
    public void transform(List<ClassElement> elements) {
        final List<String> used = new LinkedList<>();
        for(ClassElement element : elements) {
            for(MethodElement method : element.methods()) {
                MethodVisitor visitor = new MethodVisitor(method) {

                    @Override
                    public void visitCast(CastNode node) {
                        String name = ASMUtil.stripDesc(node.desc());
                        if(!used.contains(name)) {
                            used.add(name);
                        }
                    }

                    @Override
                    public void visitNew(NewNode node) {
                        String name = ASMUtil.stripDesc(node.desc());
                        if(!used.contains(name)) {
                            used.add(name);
                        }
                    }

                    @Override
                    public void visitInstanceOf(InstanceOfNode node) {
                        String name = ASMUtil.stripDesc(node.desc());
                        if(!used.contains(name)) {
                            used.add(name);
                        }
                    }

                    @Override
                    public void visitNewArray(ANewArrayNode node) {
                        String name = ASMUtil.stripDesc(node.desc());
                        if(!used.contains(name)) {
                            used.add(name);
                        }
                    }

                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        used.add(node.owner());
                    }

                    @Override
                    public void visitStaticFieldStore(StaticFieldStoreNode node) {
                        used.add(node.owner());
                    }

                    @Override
                    public void visitVirtualFieldCall(VirtualFieldCallNode node) {
                        used.add(node.owner());
                    }

                    @Override
                    public void visitVirtualFieldStore(VirtualFieldStoreNode node) {
                        used.add(node.owner());
                    }

                    @Override
                    public void visitStaticMethodCall(StaticMethodCallNode node) {
                        used.add(node.owner());
                    }

                    @Override
                    public void visitVirtualMethodCall(VirtualMethodCallNode node) {
                        used.add(node.owner());
                    }

                    @Override
                    public void visitInterfaceMethodCall(InterfaceMethodCallNode node) {
                        used.add(node.owner());
                    }

                    @Override
                    public void visitDynamicMethodCall(DynamicMethodCallNode node) {
                        used.add(node.owner());
                    }

                    @Override
                    public void visitSpecialMethodCall(SpecialMethodCallNode node) {
                        used.add(node.owner());
                    }
                };
            }
        }
        List<ClassElement> remove = new LinkedList<>();
        for(ClassElement element : elements) {
            if(!used.contains(element.name())) {
                remove.add(element);
                add();
                continue;
            }
            tAdd();
        }
        elements.removeAll(remove);
    }

    @Override
    public String result() {
        StringBuilder builder = new StringBuilder("\t\t\t↔ Executed ");
        builder.append(name()).append(" in ").append(exec()).append("ms\n\t\t\t\tRemoved ")
                .append(counter()).append(" sour lemons : kept ").append(total() - counter())
                .append(" lemons.");
        return builder.toString();
    }
}
