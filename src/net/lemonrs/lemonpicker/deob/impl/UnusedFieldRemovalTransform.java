package net.lemonrs.lemonpicker.deob.impl;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.bytecode.tree.FlowGraph;
import net.lemonrs.lemonpicker.deob.Transform;
import net.lemonrs.lemonpicker.node.impl.field.VirtualFieldCallNode;
import net.lemonrs.lemonpicker.node.impl.field.StaticFieldCallNode;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import org.objectweb.asm.tree.FieldNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class UnusedFieldRemovalTransform extends Transform {


    @Override
    public void transform(List<ClassElement> elements) {
        final List<UsedField> used = new LinkedList<>();
        List<FieldNode> remove = new LinkedList<>();
        for (ClassElement element : elements) {
            used.clear();
            for (MethodElement method : element.methods()) {
                FlowGraph graph = method.graph();
                MethodVisitor visitor = new MethodVisitor(method) {
                    @Override
                    public void visitVirtualFieldCall(VirtualFieldCallNode node) {
                        used.add(new UsedField(node.owner(),node.name()));
                    }

                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        used.add(new UsedField(node.owner(), node.name()));
                    }
                };
            }
            remove.clear();
            for (FieldNode node : element.node().fields) {
                if (contains(element, node, used)) {
                    tAdd();
                    continue;
                }
                add();
                remove.add(node);
            }
            element.node().fields.removeAll(remove);
        }
    }

    @Override
    public String result() {
        StringBuilder builder = new StringBuilder("\t\t\t↔ Executed ");
        builder.append(name()).append(" in ").append(exec()).append("ms\n\t\t\t\tRemoved ")
                .append(counter()).append(" unused lemon seeds : kept ").append(total() - counter())
                .append(" seeds.");
        return builder.toString();
    }

    private boolean contains(ClassElement element, FieldNode node, List<UsedField> used) {
        for (UsedField field : used) {
            if (field.owner.equals(element.name()) &&
                    field.name.equals(node.name)) {
                return true;
            }
        }
        return false;
    }

    private class UsedField {

        private String owner;
        private String name;

        private UsedField(String owner, String name) {
            this.owner = owner;
            this.name = name;
        }
    }
}
