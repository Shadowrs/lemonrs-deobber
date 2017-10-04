package net.lemonrs.lemonpicker.node;

import net.lemonrs.lemonpicker.node.impl.*;
import net.lemonrs.lemonpicker.node.impl.LabelNode;
import net.lemonrs.lemonpicker.node.impl.arith.ArithmeticConversionNode;
import net.lemonrs.lemonpicker.node.impl.arith.ArithmeticOperationNode;
import net.lemonrs.lemonpicker.node.impl.arith.BitwiseOperationNode;
import net.lemonrs.lemonpicker.node.impl.field.StaticFieldCallNode;
import net.lemonrs.lemonpicker.node.impl.field.StaticFieldStoreNode;
import net.lemonrs.lemonpicker.node.impl.field.VirtualFieldCallNode;
import net.lemonrs.lemonpicker.node.impl.field.VirtualFieldStoreNode;
import net.lemonrs.lemonpicker.node.impl.jump.GotoNode;
import net.lemonrs.lemonpicker.node.impl.jump.IfConditionNode;
import net.lemonrs.lemonpicker.node.impl.method.*;
import net.lemonrs.lemonpicker.node.impl.operand.LocalVariableCallNode;
import net.lemonrs.lemonpicker.node.impl.operand.LocalVariableStoreNode;
import net.lemonrs.lemonpicker.node.impl.operand.PushNode;
import net.lemonrs.lemonpicker.node.impl.type.ANewArrayNode;
import net.lemonrs.lemonpicker.node.impl.type.CastNode;
import net.lemonrs.lemonpicker.node.impl.type.InstanceOfNode;
import net.lemonrs.lemonpicker.node.impl.type.NewNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public abstract class AbstractNode<T extends AbstractInsnNode> {

    private T node;
    public static final int BASIC_NODE = 0;
    public static final int VIRTUAL_FIELD_CALL_NODE = 1;
    public static final int VIRTUAL_FIELD_STORE_NODE = 2;
    public static final int RETURN_NODE = 3;
    public static final int ARITHMETIC_CONVERSION_NODE = 4;
    public static final int ARITHMETIC_OPERATION_NODE = 5;
    public static final int BITWISE_OPERATION_NODE = 6;
    public static final int LDC_NODE = 7;
    public static final int LOCAL_VARIABLE_CALL_NODE = 8;
    public static final int LOCAL_VARIABLE_STORE_NODE = 9;
    public static final int NUMBER_CONSTANT_NODE = 10;
    public static final int PUSH_NODE = 11;
    public static final int STATIC_FIELD_CALL_NODE = 12;
    public static final int STATIC_FIELD_STORE_NODE = 13;
    public static final int STATIC_METHOD_CALL_NODE = 14;
    public static final int DYNAMIC_METHOD_CALL_NODE = 15;
    public static final int INTERFACE_METHOD_CALL_NODE = 16;
    public static final int SPECIAL_METHOD_CALL_NODE = 17;
    public static final int VIRTUAL_METHOD_CALL_NODE = 18;
    public static final int CAST_NODE = 19;
    public static final int INSTANCE_OF_NODE = 20;
    public static final int ANEW_ARRAY_NODE = 21;
    public static final int NEW_NODE = 22;
    public static final int LABEL_NODE = 23;
    public static final int IF_CONDITION_NODE = 24;
    public static final int GOTO_NODE = 25;
    public static final int DUPLICATE_NODE = 26;


    public AbstractNode(T node) {
        this.node = node;
    }

    public int opcode() {
        return node().getOpcode();
    }

    public abstract int type();

    public <N extends BasicNode> N next() {
        if (node.getNext() == null) {
            return null;
        }
        if (node.getNext().getType() == AbstractInsnNode.LABEL) {
            return (N) new LabelNode((org.objectweb.asm.tree.LabelNode) node.getNext());
        }
        switch (node.getNext().getOpcode()) {
            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
                return (N) new PushNode((IntInsnNode) node.getNext());
            case Opcodes.GETFIELD:
                return (N) new VirtualFieldCallNode((FieldInsnNode) node.getNext());
            case Opcodes.PUTFIELD:
                return (N) new VirtualFieldStoreNode((FieldInsnNode) node.getNext());
            case Opcodes.GETSTATIC:
                return (N) new StaticFieldCallNode((FieldInsnNode) node.getNext());
            case Opcodes.PUTSTATIC:
                return (N) new StaticFieldStoreNode((FieldInsnNode) node.getNext());
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
                return (N) new ArithmeticOperationNode((InsnNode) node.getNext());
            case Opcodes.F2I:
            case Opcodes.F2L:
            case Opcodes.F2D:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2D:
            case Opcodes.I2F:
            case Opcodes.I2L:
            case Opcodes.I2S:
            case Opcodes.D2I:
            case Opcodes.D2F:
            case Opcodes.D2L:
                return (N) new ArithmeticConversionNode((InsnNode) node.getNext());
            case Opcodes.ISHL:
            case Opcodes.ISHR:
            case Opcodes.LSHL:
            case Opcodes.LSHR:
            case Opcodes.IXOR:
            case Opcodes.LXOR:
            case Opcodes.IOR:
            case Opcodes.LOR:
            case Opcodes.IAND:
            case Opcodes.LAND:
            case Opcodes.IUSHR:
            case Opcodes.LUSHR:
                return (N) new BitwiseOperationNode((InsnNode) node.getNext());
            case Opcodes.ARETURN:
            case Opcodes.IRETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.LRETURN:
            case Opcodes.RETURN:
                return (N) new ReturnNode((InsnNode) node.getNext());
            case Opcodes.INVOKEDYNAMIC:
                return (N) new DynamicMethodCallNode((MethodInsnNode) node.getNext());
            case Opcodes.INVOKEINTERFACE:
                return (N) new InterfaceMethodCallNode((MethodInsnNode) node.getNext());
            case Opcodes.INVOKESPECIAL:
                return (N) new SpecialMethodCallNode((MethodInsnNode) node.getNext());
            case Opcodes.INVOKESTATIC:
                return (N) new StaticMethodCallNode((MethodInsnNode) node.getNext());
            case Opcodes.INVOKEVIRTUAL:
                return (N) new VirtualMethodCallNode((MethodInsnNode) node.getNext());
            case Opcodes.NEW:
                return (N) new NewNode((TypeInsnNode) node.getNext());
            case Opcodes.ANEWARRAY:
                return (N) new ANewArrayNode((TypeInsnNode) node.getNext());
            case Opcodes.INSTANCEOF:
                return (N) new InstanceOfNode((TypeInsnNode) node.getNext());
            case Opcodes.CHECKCAST:
                return (N) new CastNode((TypeInsnNode) node.getNext());
            case Opcodes.GOTO:
                return (N) new GotoNode((JumpInsnNode) node.getNext());
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
            case Opcodes.IF_ICMPEQ:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPNE:
            case Opcodes.IFEQ:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.IFLT:
            case Opcodes.IFNE:
            case Opcodes.IFNONNULL:
            case Opcodes.IFNULL:
                return (N) new IfConditionNode((JumpInsnNode) node.getNext());
            case Opcodes.DUP:
            case Opcodes.DUP_X1:
            case Opcodes.DUP_X2:
            case Opcodes.DUP2:
            case Opcodes.DUP2_X1:
            case Opcodes.DUP2_X2:
                return (N) new DuplicateNode((InsnNode) node.getNext());
            case Opcodes.ISTORE:
            case Opcodes.ASTORE:
            case Opcodes.DSTORE:
            case Opcodes.FSTORE:
            case Opcodes.LSTORE:
                return (N) new LocalVariableStoreNode((VarInsnNode) node.getNext());
            case Opcodes.ILOAD:
            case Opcodes.ALOAD:
            case Opcodes.DLOAD:
            case Opcodes.FLOAD:
            case Opcodes.LLOAD:
                return (N) new LocalVariableCallNode((VarInsnNode) node.getNext());
            default:
                return (N) new BasicNode<>(node.getNext());
        }
    }

    public <P extends BasicNode> P prev() {
        if (node.getPrevious() == null) {
            return null;
        }
        if (node.getPrevious().getType() == AbstractInsnNode.LABEL) {
            return (P) new LabelNode((org.objectweb.asm.tree.LabelNode) node.getPrevious());
        }
        switch (node.getPrevious().getOpcode()) {
            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
                return (P) new PushNode((IntInsnNode) node.getPrevious());
            case Opcodes.GETFIELD:
                return (P) new VirtualFieldCallNode((FieldInsnNode) node.getPrevious());
            case Opcodes.PUTFIELD:
                return (P) new VirtualFieldStoreNode((FieldInsnNode) node.getPrevious());
            case Opcodes.GETSTATIC:
                return (P) new StaticFieldCallNode((FieldInsnNode) node.getPrevious());
            case Opcodes.PUTSTATIC:
                return (P) new StaticFieldStoreNode((FieldInsnNode) node.getPrevious());
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
                return (P) new ArithmeticOperationNode((InsnNode) node.getPrevious());
            case Opcodes.F2I:
            case Opcodes.F2L:
            case Opcodes.F2D:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2D:
            case Opcodes.I2F:
            case Opcodes.I2L:
            case Opcodes.I2S:
            case Opcodes.D2I:
            case Opcodes.D2F:
            case Opcodes.D2L:
                return (P) new ArithmeticConversionNode((InsnNode) node.getPrevious());
            case Opcodes.ISHL:
            case Opcodes.ISHR:
            case Opcodes.LSHL:
            case Opcodes.LSHR:
            case Opcodes.IXOR:
            case Opcodes.LXOR:
            case Opcodes.IOR:
            case Opcodes.LOR:
            case Opcodes.IAND:
            case Opcodes.LAND:
            case Opcodes.IUSHR:
            case Opcodes.LUSHR:
                return (P) new BitwiseOperationNode((InsnNode) node.getPrevious());
            case Opcodes.ARETURN:
            case Opcodes.IRETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.LRETURN:
            case Opcodes.RETURN:
                return (P) new ReturnNode((InsnNode) node.getPrevious());
            case Opcodes.INVOKEDYNAMIC:
                return (P) new DynamicMethodCallNode((MethodInsnNode) node.getPrevious());
            case Opcodes.INVOKEINTERFACE:
                return (P) new InterfaceMethodCallNode((MethodInsnNode) node.getPrevious());
            case Opcodes.INVOKESPECIAL:
                return (P) new SpecialMethodCallNode((MethodInsnNode) node.getPrevious());
            case Opcodes.INVOKESTATIC:
                return (P) new StaticMethodCallNode((MethodInsnNode) node.getPrevious());
            case Opcodes.INVOKEVIRTUAL:
                return (P) new VirtualMethodCallNode((MethodInsnNode) node.getPrevious());
            case Opcodes.NEW:
                return (P) new NewNode((TypeInsnNode) node.getPrevious());
            case Opcodes.ANEWARRAY:
                return (P) new ANewArrayNode((TypeInsnNode) node.getPrevious());
            case Opcodes.INSTANCEOF:
                return (P) new InstanceOfNode((TypeInsnNode) node.getPrevious());
            case Opcodes.CHECKCAST:
                return (P) new CastNode((TypeInsnNode) node.getPrevious());
            case Opcodes.GOTO:
                return (P) new GotoNode((JumpInsnNode) node.getPrevious());
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
            case Opcodes.IF_ICMPEQ:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPNE:
            case Opcodes.IFEQ:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.IFLT:
            case Opcodes.IFNE:
            case Opcodes.IFNONNULL:
            case Opcodes.IFNULL:
                return (P) new IfConditionNode((JumpInsnNode) node.getPrevious());
            case Opcodes.DUP:
            case Opcodes.DUP_X1:
            case Opcodes.DUP_X2:
            case Opcodes.DUP2:
            case Opcodes.DUP2_X1:
            case Opcodes.DUP2_X2:
                return (P) new DuplicateNode((InsnNode) node.getPrevious());
            case Opcodes.ISTORE:
            case Opcodes.ASTORE:
            case Opcodes.DSTORE:
            case Opcodes.FSTORE:
            case Opcodes.LSTORE:
                return (P) new LocalVariableStoreNode((VarInsnNode) node.getPrevious());
            case Opcodes.ILOAD:
            case Opcodes.ALOAD:
            case Opcodes.DLOAD:
            case Opcodes.FLOAD:
            case Opcodes.LLOAD:
                return (P) new LocalVariableCallNode((VarInsnNode) node.getPrevious());
            default:
                return (P) new BasicNode<>(node.getPrevious());
        }
    }

    public <E extends BasicNode> E next(int type) {
        switch (type) {
            case BASIC_NODE:
                return (E) next();
            default:
                E next = (E) this;
                while ((next = (E) next.next()) != null) {
                    if (next.type() == type) {
                        return next;
                    }
                }
                break;
        }
        return null;
    }


    public <E extends BasicNode> E next(int type, int dist) {
        int total = 0;
        switch (type) {
            case BASIC_NODE:
                return (E) next();
            default:
                E next = (E) this;
                while ((next = (E) next.next()) != null) {
                    if(total > dist) {
                       break;
                    }
                    if (next.type() == type) {
                        return next;
                    }
                    total++;
                }
                break;
        }
        return null;
    }

    public <E extends BasicNode> E prev(int type) {
        switch (type) {
            case BASIC_NODE:
                return (E) prev();
            default:
                E prev = (E) this;
                while ((prev = (E) prev.prev()) != null) {
                    if (prev.type() == type) {
                        return prev;
                    }
                }
                break;
        }
        return null;
    }


    public <E extends BasicNode> E prev(int type, int dist) {
        switch (type) {
            case BASIC_NODE:
                return (E) prev();
            default:
                E prev = (E) this;
                int total = 0;
                while ((prev = (E) prev.prev()) != null) {
                    if(total > dist) {
                        break;
                    }
                    if (prev.type() == type) {
                        return prev;
                    }
                    total++;
                }
                break;
        }
        return null;
    }

    public T node() {
        return node;
    }
}
