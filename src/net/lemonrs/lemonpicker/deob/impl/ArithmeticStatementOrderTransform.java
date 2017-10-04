package net.lemonrs.lemonpicker.deob.impl;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.deob.Transform;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.List;

/**
 * @author : const_
 */
public class ArithmeticStatementOrderTransform extends Transform {

    @Override
    public void transform(List<ClassElement> elements) {
        for (ClassElement element : elements) {
            for (MethodElement method : element.methods()) {
                for (AbstractInsnNode ain : method.instructionList()) {
                    switch (ain.getOpcode()) {
                        case Opcodes.IMUL:
                        case Opcodes.LMUL:
                        case Opcodes.DMUL:
                        case Opcodes.FMUL:
                        case Opcodes.IADD:
                        case Opcodes.LADD:
                        case Opcodes.DADD:
                        case Opcodes.FADD:
                        case Opcodes.ISUB:
                        case Opcodes.LSUB:
                        case Opcodes.DSUB:
                        case Opcodes.FSUB:
                        case Opcodes.IDIV:
                        case Opcodes.LDIV:
                        case Opcodes.DDIV:
                        case Opcodes.FDIV:
                            AbstractInsnNode field = null;
                            VarInsnNode var = null;
                            AbstractInsnNode constant = null;
                            AbstractInsnNode prev = ain;
                            int count = 0;
                            while ((prev = prev.getPrevious()) != null) {
                                if (count > 12) {
                                    break;
                                }
                                count++;
                                switch (prev.getType()) {
                                    case AbstractInsnNode.FIELD_INSN:
                                        if (field == null) {
                                            field = prev;
                                        }
                                        break;
                                    case AbstractInsnNode.VAR_INSN:
                                        if (var == null && prev.getOpcode() == Opcodes.ALOAD) {
                                            var = (VarInsnNode) prev;
                                        }
                                        if (prev.getOpcode() == Opcodes.ILOAD ||
                                                prev.getOpcode() == Opcodes.FLOAD ||
                                                prev.getOpcode() == Opcodes.DLOAD ||
                                                prev.getOpcode() == Opcodes.LLOAD) {
                                            if (field == null) {
                                                field = prev;
                                            }
                                        }
                                        break;
                                    case AbstractInsnNode.LDC_INSN:
                                    case AbstractInsnNode.INT_INSN:
                                        if (constant == null) {
                                            constant = prev;
                                        }
                                        break;
                                }
                            }
                            if (field != null && constant != null && (var != null || field.getOpcode() == Opcodes.GETSTATIC ||
                                    field.getOpcode() == Opcodes.PUTSTATIC)) {
                                if (field.getOpcode() != Opcodes.GETSTATIC &&
                                        field.getOpcode() != Opcodes.PUTSTATIC &&
                                        field.getType() == AbstractInsnNode.FIELD_INSN) {
                                    method.node().instructions.remove(var);
                                    method.node().instructions.insertBefore(field, var);
                                }
                                method.node().instructions.remove(constant);
                                method.node().instructions.insert(field, constant);
                                add();
                            }
                    }
                }
                method.node().visitMaxs(1, 1);
                method.node().visitEnd();
            }
            element.node().visitEnd();
        }
    }

    @Override
    public String result() {
        StringBuilder builder = new StringBuilder("\t\t\t↔ Executed ");
        builder.append(name()).append(" in ").append(exec()).append("ms\n\t\t\t\tRearranged ")
                .append(counter()).append(" roots.");
        return builder.toString();
    }
}
