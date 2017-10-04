package net.lemonrs.lemonpicker.graph.condition;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.jump.IfConditionNode;
import net.lemonrs.lemonpicker.util.Value;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * @author : const_
 */
public class Condition<T> {

    private AbstractNode trueTarget;
    private AbstractNode falseTarget;
    private Comparison comparison;
    private IfConditionNode node;

    public Condition(IfConditionNode node) {
        this.node = node;
        build();
    }

    public Object constant() {
        return comparison().constant().value();
    }

    public Comparison comparison() {
        return comparison;
    }

    public FieldElement field() {
        return comparison().field();
    }

    public AbstractNode trueTarget() {
        return trueTarget;
    }

    public AbstractNode falseTarget() {
        return falseTarget;
    }

    public boolean hasConstant() {
        return comparison() != null && comparison().constant() != null && comparison().constant().value() != null &&
                comparison().constant().set();
    }

    public boolean hasField() {
        return comparison() != null && comparison().field() != null;
    }

    public void build() {
        String operator = node.operator();
        Value<T> value = new Value<>();
        FieldInsnNode field = null;
        switch (node.opcode()) {
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
                AbstractInsnNode prev = node.node();
                int count = 0;
                while ((prev = prev.getPrevious()) != null) {
                    if (count > 12 || value.set() && field != null) {
                        break;
                    }
                    count++;
                    if (prev.getType() == AbstractInsnNode.FIELD_INSN) {
                        if (field == null) {
                            field = (FieldInsnNode) prev;
                            continue;
                        }
                        if (!value.set()) {
                            value.set((T) prev);
                        }
                    }
                }
                break;
            case Opcodes.IF_ICMPEQ:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPNE:
                prev = node.node();
                count = 0;
                while ((prev = prev.getPrevious()) != null) {
                    if (count > 12 || value.set() && field != null) {
                        break;
                    }
                    count++;
                    if (prev.getType() == AbstractInsnNode.FIELD_INSN && field == null) {
                        field = (FieldInsnNode) prev;
                    }
                    if (!value.set()) {
                        if ((prev.getType() == AbstractInsnNode.INT_INSN ||
                                prev.getType() == AbstractInsnNode.LDC_INSN) && prev.getPrevious() != null &&
                                prev.getPrevious().getType() != AbstractInsnNode.FIELD_INSN && prev.getNext() != null &&
                                prev.getNext().getOpcode() != Opcodes.IMUL) {
                            if (prev.getType() == AbstractInsnNode.INT_INSN) {
                                IntInsnNode node = (IntInsnNode) prev;
                                value.set((T) new Integer(node.operand));
                            } else {
                                LdcInsnNode ldc = (LdcInsnNode) prev;
                                value.set((T) ldc.cst);
                            }
                        }
                        switch (prev.getOpcode()) {
                            case Opcodes.ICONST_0:
                                value.set((T) new Integer(0));
                                break;
                            case Opcodes.ICONST_1:
                                value.set((T) new Integer(1));
                                break;
                            case Opcodes.ICONST_2:
                                value.set((T) new Integer(2));
                                break;
                            case Opcodes.ICONST_3:
                                value.set((T) new Integer(3));
                                break;
                            case Opcodes.ICONST_4:
                                value.set((T) new Integer(4));
                                break;
                            case Opcodes.ICONST_5:
                                value.set((T) new Integer(5));
                                break;
                            case Opcodes.ICONST_M1:
                                value.set((T) new Integer(-1));
                                break;
                            case Opcodes.FCONST_0:
                                value.set((T) new Float(0.0));
                                break;
                            case Opcodes.FCONST_1:
                                value.set((T) new Float(1.0));
                                break;
                            case Opcodes.FCONST_2:
                                value.set((T) new Float(2.0));
                                break;
                            case Opcodes.DCONST_0:
                                value.set((T) new Double(0.0));
                                break;
                            case Opcodes.DCONST_1:
                                value.set((T) new Double(1.0));
                                break;
                            case Opcodes.LCONST_0:
                                value.set((T) new Long(0));
                                break;
                            case Opcodes.LCONST_1:
                                value.set((T) new Long(1));
                                break;
                        }
                    }
                }
                break;
            case Opcodes.IFEQ:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.IFLT:
            case Opcodes.IFNE:
                prev = node.node();
                count = 0;
                while ((prev = prev.getPrevious()) != null) {
                    if (count > 12 || value.set() && field != null) {
                        break;
                    }
                    count++;
                    if (prev.getType() == AbstractInsnNode.FIELD_INSN && field == null) {
                        field = (FieldInsnNode) prev;
                    }
                }
                value.set((T) new Integer(0));
                break;
            case Opcodes.IFNONNULL:
            case Opcodes.IFNULL:
                prev = node.node();
                count = 0;
                while ((prev = prev.getPrevious()) != null) {
                    if (count > 12 || value.set() && field != null) {
                        break;
                    }
                    count++;
                    if (prev.getType() == AbstractInsnNode.FIELD_INSN && field == null) {
                        field = (FieldInsnNode) prev;
                    }
                }
                value.set((T) "NULL");
                break;
        }
        FieldElement element = null;
        if (field != null) {
            ClassElement owner = Main.get(field.owner);
            if (owner != null) {
                element = owner.findField(field.name);
            }
        }
        comparison = new Comparison(value, element, this, operator);
        AbstractNode jump = node.target();
        AbstractNode continueOn = null;
        AbstractNode next = node;
        while ((next = next.next()) != null) {
            if (next.type() == AbstractNode.LABEL_NODE) {
                continue;
            }
            continueOn = next;
            break;
        }
        if (continueOn != null) {
            switch (node.opcode()) {
                case Opcodes.IFNULL:
                case Opcodes.IF_ACMPEQ:
                case Opcodes.IFEQ:
                case Opcodes.IF_ICMPEQ:
                case Opcodes.IFLT:
                case Opcodes.IF_ICMPLT:
                case Opcodes.IF_ICMPLE:
                case Opcodes.IFLE:
                case Opcodes.IF_ICMPGT:
                case Opcodes.IFGT:
                case Opcodes.IF_ICMPGE:
                case Opcodes.IFGE:
                    trueTarget = jump;
                    falseTarget = continueOn;
                    break;
                case Opcodes.IF_ACMPNE:
                case Opcodes.IF_ICMPNE:
                case Opcodes.IFNE:
                case Opcodes.IFNONNULL:
                    trueTarget = continueOn;
                    falseTarget = jump;
                    break;
            }
        }
    }
}
