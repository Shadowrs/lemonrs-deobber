package net.lemonrs.lemonpicker.graph.method;

import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.bytecode.tree.method.MethodInsnVisitor;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class EntryPoint {

    private MethodElement element;
    private List<MethodCall> calls;

    public EntryPoint(MethodElement element) {
        this.element = element;
    }

    public List<MethodCall> calls() {
        if(calls == null) {
            calls = new LinkedList<>();
            MethodInsnVisitor visitor = element.graph().visit(MethodInsnVisitor.class);
            for(MethodInsnNode node : visitor.instructions()) {
                calls.add(new MethodCall(node));
            }
        }
        return calls;
    }
}
