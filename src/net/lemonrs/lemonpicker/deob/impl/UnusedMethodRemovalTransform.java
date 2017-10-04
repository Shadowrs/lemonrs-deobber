package net.lemonrs.lemonpicker.deob.impl;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.deob.Transform;
import net.lemonrs.lemonpicker.graph.MethodCallGraph;
import net.lemonrs.lemonpicker.graph.method.EntryPoint;
import net.lemonrs.lemonpicker.graph.method.MethodCall;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class UnusedMethodRemovalTransform extends Transform {
    @Override
    public void transform(List<ClassElement> elements) {

        List<EntryPoint> entries = new LinkedList<>();
        for (ClassElement element : elements) {
            MethodElement method = element.findMethod("<clinit>", "()V");
            if (method != null) {
                entries.add(new EntryPoint(method));
            }
        }
        entries.add(new EntryPoint(Main.get("client").findMethod("init", "()V")));
        entries.add(new EntryPoint(Main.get(Main.get("client").node().superName).findMethod("start", "()V")));
        MethodCallGraph graph = new MethodCallGraph(entries);
        graph.build();
        List<MethodNode> remove = new LinkedList<>();
        List<MethodCall> called = graph.calls();
        for (ClassElement element : elements) {
            remove.clear();
            methods:
            for (MethodElement method : element.methods()) {
                if (method.isInherited() || method.isAbstract() || method.isNative() ||
                        method.name().equals("<init>") || method.name().equals("<clinit>")) {
                    tAdd();
                    continue;
                }
                if (element.node().superName.startsWith("java/")) {
                    try {
                        Class<?> super_ = ClassLoader.getSystemClassLoader().loadClass(element.node().superName.replaceAll("/", "."));
                        for(Method m : super_.getMethods()) {
                            if(m.getName().equals(method.name())) {
                                tAdd();
                                continue methods;
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                for (MethodCall call : called) {
                    if (call.node().name.equals(method.name()) &&
                            call.node().owner.equals(method.parent().name()) &&
                            call.node().desc.equals(method.desc())) {
                        tAdd();
                        continue methods;
                    }
                }
                remove.add(method.node());
                add();
            }
            element.node().methods.removeAll(remove);
        }
    }


    @Override
    public String result() {
        StringBuilder builder = new StringBuilder("\t\t\t↔ Executed ");
        builder.append(name()).append(" in ").append(exec()).append("ms\n\t\t\t\tRemoved ")
                .append(counter()).append(" unused lemon branches : kept ").append(total() - counter())
                .append(" branches.");
        return builder.toString();
    }
}
