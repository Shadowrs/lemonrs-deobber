package net.lemonrs.lemonpicker.bytecode.element;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.tree.FlowGraph;
import net.lemonrs.lemonpicker.graph.ControlFlowGraph;
import net.lemonrs.lemonpicker.graph.flow.Block;
import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.BasicNode;
import net.lemonrs.lemonpicker.node.impl.DuplicateNode;
import net.lemonrs.lemonpicker.node.impl.ReturnNode;
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
import net.lemonrs.lemonpicker.node.impl.operand.*;
import net.lemonrs.lemonpicker.node.impl.type.ANewArrayNode;
import net.lemonrs.lemonpicker.node.impl.type.CastNode;
import net.lemonrs.lemonpicker.node.impl.type.InstanceOfNode;
import net.lemonrs.lemonpicker.node.impl.type.NewNode;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.Flag;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class MethodElement {

    private ClassElement parent;
    private MethodNode node;
    private FlowGraph graph;
    private List<AbstractNode> instructions;
    private List<FieldElement> fields;
    private int size;
    private ControlFlowGraph cfg;

    public MethodElement(ClassElement parent, MethodNode node) {
        this.parent = parent;
        this.node = node;
    }

    public Block find(AbstractNode node) {
        if (cfg == null) {
            cfg = new ControlFlowGraph(this);
            cfg.build();
        }
        for (Block block : cfg.blocks()) {
            if (block.instructions().contains(node)) {
                return block;
            }
        }
        return null;
    }


    public Block findASM(AbstractInsnNode node) {
        if (cfg == null) {
            cfg = new ControlFlowGraph(this);
            cfg.build();
        }
        for (Block block : cfg.blocks()) {
            if (block.nodes().containsKey(node().instructions.indexOf(node))) {
                return block;
            }
        }
        return null;
    }


    public AbstractNode find(AbstractInsnNode node) {
        for (AbstractNode node_ : instructions()) {
            if (node_.node().equals(node)) {
                return node_;
            }
        }
        return null;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int size() {
        return size;
    }

    public void regraph() {
        fields = null;
        instructions = null;
        instructions();
        fields();
    }

    public List<FieldElement> fields() {
        if (fields == null) {
            fields = new LinkedList<>();
            MethodVisitor visitor = new MethodVisitor(this) {
                @Override
                public void visitVirtualFieldCall(VirtualFieldCallNode node) {
                    if (!fields.contains(node.field())) {
                        fields.add(node.field());
                    }
                }

                @Override
                public void visitVirtualFieldStore(VirtualFieldStoreNode node) {
                    if (!fields.contains(node.field())) {
                        fields.add(node.field());
                    }
                }

                @Override
                public void visitStaticFieldCall(StaticFieldCallNode node) {
                    if (!fields.contains(node.field())) {
                        fields.add(node.field());
                    }
                }

                @Override
                public void visitStaticFieldStore(StaticFieldStoreNode node) {
                    if (!fields.contains(node.field())) {
                        fields.add(node.field());
                    }
                }
            };
        }
        return fields;
    }

    public boolean hasCast(final String cast) {
        final Flag flag = new Flag();
        MethodVisitor visitor = new MethodVisitor(this) {
            @Override
            public void visitCast(CastNode node) {
                if (node.desc().equals(cast)) {
                    flag.flag();
                }
            }
        };
        return flag.flagged();
    }

    public boolean returns(String desc) {
        return node().desc.substring(node().desc.lastIndexOf(')')).equals(desc);
    }

    public boolean takes(String desc) {
        return node().desc.substring(node().desc.indexOf('(') + 1, node().desc.lastIndexOf(')')).contains(desc);
    }

    public boolean hasConstant(final Number constant) {
        final Flag flag = new Flag();
        MethodVisitor visitor = new MethodVisitor(this) {
            @Override
            public void visitNumberConstant(NumberConstantNode node) {
                super.visitNumberConstant(node);
                if (node.value().equals(constant)) {
                    flag.flag();
                }
            }

            @Override
            public void visitPush(PushNode node) {
                super.visitPush(node);
                if (node.push() == constant.intValue()) {
                    flag.flag();
                }
            }

            @Override
            public void visitLdc(LdcNode node) {
                super.visitLdc(node);
                if (node.value().equals(constant)) {
                    flag.flag();
                }
            }
        };
        return flag.flagged();
    }


    public List<AbstractNode> instructions() {
        if (instructions == null) {
            instructions = new LinkedList<>();
            for (AbstractInsnNode node : instructionList()) {
                if (node == null) {
                    continue;
                }
                switch (node.getOpcode()) {
                    case Opcodes.BIPUSH:
                    case Opcodes.SIPUSH:
                        instructions.add(new PushNode((IntInsnNode) node));
                        break;
                    case Opcodes.GETFIELD:
                        instructions.add(new VirtualFieldCallNode((FieldInsnNode) node));
                        break;
                    case Opcodes.PUTFIELD:
                        instructions.add(new VirtualFieldStoreNode((FieldInsnNode) node));
                        break;
                    case Opcodes.GETSTATIC:
                        instructions.add(new StaticFieldCallNode((FieldInsnNode) node));
                        break;
                    case Opcodes.PUTSTATIC:
                        instructions.add(new StaticFieldStoreNode((FieldInsnNode) node));
                        break;
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
                        instructions.add(new ArithmeticOperationNode((InsnNode) node));
                        break;
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
                        instructions.add(new ArithmeticConversionNode((InsnNode) node));
                        break;
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
                        instructions.add(new BitwiseOperationNode((InsnNode) node));
                        break;
                    case Opcodes.ARETURN:
                    case Opcodes.IRETURN:
                    case Opcodes.FRETURN:
                    case Opcodes.DRETURN:
                    case Opcodes.LRETURN:
                    case Opcodes.RETURN:
                        instructions.add(new ReturnNode((InsnNode) node));
                        break;
                    case Opcodes.INVOKEDYNAMIC:
                        instructions.add(new DynamicMethodCallNode((MethodInsnNode) node));
                        break;
                    case Opcodes.INVOKEINTERFACE:
                        instructions.add(new InterfaceMethodCallNode((MethodInsnNode) node));
                        break;
                    case Opcodes.INVOKESPECIAL:
                        instructions.add(new SpecialMethodCallNode((MethodInsnNode) node));
                        break;
                    case Opcodes.INVOKESTATIC:
                        instructions.add(new StaticMethodCallNode((MethodInsnNode) node));
                        break;
                    case Opcodes.INVOKEVIRTUAL:
                        instructions.add(new VirtualMethodCallNode((MethodInsnNode) node));
                        break;
                    case Opcodes.NEW:
                        instructions.add(new NewNode((TypeInsnNode) node));
                        break;
                    case Opcodes.ANEWARRAY:
                        instructions.add(new ANewArrayNode((TypeInsnNode) node));
                        break;
                    case Opcodes.INSTANCEOF:
                        instructions.add(new InstanceOfNode((TypeInsnNode) node));
                        break;
                    case Opcodes.CHECKCAST:
                        instructions.add(new CastNode((TypeInsnNode) node));
                        break;
                    case Opcodes.GOTO:
                        instructions.add(new GotoNode((JumpInsnNode) node));
                        break;
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
                        instructions.add(new IfConditionNode((JumpInsnNode) node));
                        break;
                    case Opcodes.DUP:
                    case Opcodes.DUP_X1:
                    case Opcodes.DUP_X2:
                    case Opcodes.DUP2:
                    case Opcodes.DUP2_X1:
                    case Opcodes.DUP2_X2:
                        instructions.add(new DuplicateNode((InsnNode) node));
                        break;
                    case Opcodes.ISTORE:
                    case Opcodes.ASTORE:
                    case Opcodes.DSTORE:
                    case Opcodes.FSTORE:
                    case Opcodes.LSTORE:
                        instructions.add(new LocalVariableStoreNode((VarInsnNode) node));
                        break;
                    case Opcodes.ILOAD:
                    case Opcodes.ALOAD:
                    case Opcodes.DLOAD:
                    case Opcodes.FLOAD:
                    case Opcodes.LLOAD:
                        instructions.add(new LocalVariableCallNode((VarInsnNode) node));
                        break;
                    default:
                        instructions.add(new BasicNode<>(node));
                        break;
                }
            }
        }
        return instructions;
    }

    public List<AbstractInsnNode> instructionList() {
        List<AbstractInsnNode> nodes = new LinkedList<>();
        for (AbstractInsnNode node : node().instructions.toArray()) {
            nodes.add(node);
        }
        return nodes;
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(node.access);
    }

    public boolean isNative() {
        return Modifier.isNative(node.access);
    }

    public boolean member() {
        return !Modifier.isStatic(node.access);
    }

    public boolean isInherited() {
        ClassElement super_ = Main.get(parent.node().superName);
        while (super_ != null) {
            if (super_.findMethod(name(), desc()) != null) {
                return true;
            }
            super_ = Main.get(super_.node().superName);
        }
        return false;
    }

    public ClassElement parent() {
        return parent;
    }

    public MethodNode node() {
        return node;
    }

    public FlowGraph graph() {
        if (graph == null) {
            graph = new FlowGraph(parent(), this);
        }
        return graph;
    }

    public int parameterCount() {
        String desc = desc().split("(|)")[1];
        boolean inDesc = false;
        int count = 0;
        for (char c : desc.toCharArray()) {
            if (inDesc) {
                if (c == ';') {
                    inDesc = false;
                    count++;
                }
                continue;
            }
            switch (c) {
                case 'L':
                    inDesc = true;
                    break;
                default:
                    count++;
                    break;
            }
        }
        return count;
    }

    public String name() {
        return node().name;
    }

    public String desc() {
        return node().desc;
    }
}
