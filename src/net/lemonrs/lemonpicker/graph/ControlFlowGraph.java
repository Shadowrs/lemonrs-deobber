package net.lemonrs.lemonpicker.graph;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.graph.flow.Block;
import net.lemonrs.lemonpicker.util.ASMUtil;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class ControlFlowGraph {

    private MethodElement method;
    private List<Block> blocks = new LinkedList<>();
    private List<Block> ordered = new LinkedList<>();

    public static void main(String[] args) {
        Main.elements = ASMUtil.load(new File("./gamepack.jar"));
        ControlFlowGraph graph = new ControlFlowGraph(Main.get("client").findMethod("ho", "(III)V"));
        graph.build();
        for (int i = 0; i < graph.ordered.size(); i++) {
            Block block = graph.ordered.get(i);
            System.out.println("Block #" + i);
           /* for(AbstractInsnNode node : block.nodes()) {
                if(node.getType() == AbstractInsnNode.FIELD_INSN) {
                    FieldInsnNode fin = (FieldInsnNode) node;
                    System.out.println(fin.owner + "." + fin.name);
                }
            }*/
            System.out.println(block.format());
        }
        int original = 0;
        for(Block block : graph.blocks) {
            original += block.nodes().size();
        }
        int n = 0;
        for (Block block : graph.ordered) {
            n += block.nodes().size();
        }
        System.out.println("Graphed " + n + " out of " + original);
    }

    public ControlFlowGraph(MethodElement method) {
        this.method = method;
    }

    public List<Block> blocks() {
        return blocks;
    }

    public Block follow(Block block) {
        return findWithPosition(block.destination());
    }

    public Block followBackwards(Block block) {
        return findBlockWithDestination(block.position());
    }

    public void build() {
        buildBlocks();
        removeEmpties();
        removeGotos();
        //order();
    }

    private void removeGotos() {
        for(Block block : blocks) {
            List<AbstractInsnNode> remove = new LinkedList<>();
            for(AbstractInsnNode node : block.nodes().values()) {
                if(node.getOpcode() == Opcodes.GOTO) {
                    remove.add(node);
                }
            }
            for(AbstractInsnNode node : remove) {
                block.nodes().remove(method.node().instructions.indexOf(node));
            }
        }
    }
    private AbstractInsnNode jumpTo(JumpInsnNode jump) {
        for (AbstractInsnNode node : method.node().instructions.toArray()) {
            if (node.getOpcode() == Opcodes.F_NEW &&
                    node.equals(jump.label)) {
                return node.getNext();
            }
        }
        return null;
    }

    private Block findBlockWithDestinationVisitCheck(AbstractInsnNode destination) {
        for(Block block : blocks) {
            if(block.destination().equals(destination) &&
                    !block.visited()) {
                return block;
            }
        }
        return null;
    }

    private Block findBlockWithDestination(AbstractInsnNode destination) {
        for(Block block : blocks) {
            if(block.destination().equals(destination)) {
                return block;
            }
        }
        return null;
    }

    private void order() {
        for(Block block : blocks) {
            Block current = new Block(method, this);
            current.nodes().putAll(block.nodes());
            current.setPosition(block.position());

        }
    }

    private void removeEmpty(Block empty, Block dest) {
        if(dest == null) {
            return;
        }
        empty.merge(dest);
        if (dest.nodes().size() == 1 &&
                dest.nodes().values().iterator().next().getOpcode() == Opcodes.GOTO) {
            removeEmpty(dest, findWithPosition(empty.destination()));
        }
    }

    private Block findWithPosition(AbstractInsnNode position) {
        for (Block block : blocks) {
            if (block.position().equals(position)) {
                return block;
            }
        }
        return null;
    }

    private Block findWithPositionVisitCheck(AbstractInsnNode position) {
        for (Block block : blocks) {
            if (block.position().equals(position) && !block.visited()) {
                return block;
            }
        }
        return null;
    }

    private void removeEmpties() {
        for (Block block : blocks) {
            if (block.nodes().size() == 1 &&
                    block.nodes().values().iterator().next().getOpcode() == Opcodes.GOTO) {
                removeEmpty(block, findWithPosition(block.destination()));
            }
        }
    }

    private void buildBlocks() {
        List<AbstractInsnNode> visited = new LinkedList<>();
        nodes:
        for (AbstractInsnNode node : method.instructionList()) {
            if (node.getOpcode() == Opcodes.F_NEW ||
                    visited.contains(node)) {
                continue;
            }
            Block current = new Block(method, this);
            current.add(method.node().instructions.indexOf(node), node);
            current.setPosition(node);
            AbstractInsnNode next = node;
            while ((next = next.getNext()) != null) {
                visited.add(next);
                switch (next.getOpcode()) {
                    case Opcodes.F_NEW:
                        if (!current.isEmpty()) {
                            blocks.add(current);
                        }
                        continue nodes;
                    case Opcodes.GOTO:
                        current.add(method.node().instructions.indexOf(next), next);
                        current.setDestination(jumpTo((JumpInsnNode) next));
                        blocks.add(current);
                        continue nodes;
                    case Opcodes.ARETURN:
                    case Opcodes.IRETURN:
                    case Opcodes.FRETURN:
                    case Opcodes.DRETURN:
                    case Opcodes.LRETURN:
                    case Opcodes.RETURN:
                        current.setDestination(null);
                        current.add(method.node().instructions.indexOf(next), next);
                        blocks.add(current);
                        continue nodes;
                    default:
                        current.add(method.node().instructions.indexOf(next), next);
                }
            }
        }
    }

    public MethodElement method() {
        return method;
    }
}
