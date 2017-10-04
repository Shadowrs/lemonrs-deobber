package net.lemonrs.lemonpicker.deob.impl;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.deob.Transform;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class OpaquePredicateRemovalTransform extends Transform {
    @Override
    public void transform(List<ClassElement> elements) {
        List<AbstractInsnNode> remove = new LinkedList<>();
        for (ClassElement element : elements) {
            for (MethodElement method : element.methods()) {
                switch (method.desc().charAt(method.desc().indexOf(')') - 1)) {
                    case 'I':
                    case 'S':
                    case 'B':
                        int var = method.parameterCount();
                        for (AbstractInsnNode node : method.instructionList()) {
                            if (node.getOpcode() == Opcodes.ILOAD) {
                                remove.clear();
                                VarInsnNode varInsn = (VarInsnNode) node;
                                remove.add(varInsn);
                                if (varInsn.var == var) {
                                    if (varInsn.getNext() != null &&
                                            varInsn.getNext().getType() == AbstractInsnNode.JUMP_INSN &&
                                            varInsn.getNext().getOpcode() != Opcodes.GOTO) {
                                        method.node().instructions.insert(
                                                varInsn.getNext(),
                                                new JumpInsnNode(
                                                        Opcodes.GOTO, ((JumpInsnNode) varInsn.getNext()).label));
                                        remove.add(varInsn.getNext());
                                        remove.add(varInsn.getPrevious());
                                    } else if (varInsn.getNext() != null &&
                                            varInsn.getNext().getNext() != null &&
                                            varInsn.getNext().getNext().getType() == AbstractInsnNode.JUMP_INSN &&
                                            varInsn.getNext().getNext().getOpcode() != Opcodes.GOTO) {
                                        method.node().instructions.insert(
                                                varInsn.getNext(),
                                                new JumpInsnNode(
                                                        Opcodes.GOTO, ((JumpInsnNode) varInsn.getNext().getNext()).label));
                                        remove.add(varInsn.getNext());
                                        remove.add(varInsn.getNext());
                                    }
                                }
                                for (ClassElement element_ : elements) {
                                    List<AbstractInsnNode> remove_ = new LinkedList<>();
                                    for (MethodElement method_ : element_.methods()) {
                                        if(method_.node().instructions.size() <= 0) {
                                            continue;
                                        }
                                        remove_.clear();
                                        for (AbstractInsnNode ain : method_.instructionList()) {
                                            if (ain instanceof MethodInsnNode) {
                                                MethodInsnNode min = (MethodInsnNode) ain;
                                                if (min.owner.equals(element.name()) &&
                                                        min.name.equals(method.name()) &&
                                                        min.desc.equals(method.desc()) &&
                                                        min.getPrevious() != null &&
                                                        (min.getPrevious().getOpcode() == Opcodes.LDC ||
                                                                min.getPrevious().getOpcode() == Opcodes.BIPUSH ||
                                                                min.getPrevious().getOpcode() == Opcodes.SIPUSH)) {
                                                    remove_.add(min.getPrevious());
                                                }
                                            }
                                        }
                                        if (remove_.size() > 0 && remove.size() == 3) {
                                            for (AbstractInsnNode ain : remove) {
                                                if (ain == null) {
                                                    continue;
                                                }
                                                method.node().instructions.remove(ain);
                                            }
                                            for (AbstractInsnNode ain : remove_) {
                                                method_.node().instructions.remove(ain);
                                            }
                                        }
                                        add();
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        }
    }

    @Override
    public String result() {
        StringBuilder builder = new StringBuilder("\t\t\t↔ Executed ");
        builder.append(name()).append(" in ").append(exec()).append("ms\n\t\t\t\tRemoved ")
                .append(counter()).append(" dead tree leaves");
        return builder.toString();
    }
}
