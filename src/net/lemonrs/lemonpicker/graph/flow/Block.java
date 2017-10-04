package net.lemonrs.lemonpicker.graph.flow;

import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.graph.ControlFlowGraph;
import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.util.ASMUtil;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.*;

/**
 * @author : const_
 */
public class Block {

    private Map<Integer, AbstractInsnNode> nodes = new HashMap<>();
    private AbstractInsnNode position;
    private AbstractInsnNode destination;
    private MethodElement method;
    private boolean visited;
    private List<AbstractNode> instructions;
    private ControlFlowGraph graph;

    public Block(MethodElement method, ControlFlowGraph graph) {
        this.method = method;
        this.graph = graph;
    }

    public List<AbstractNode> instructions() {
        if (instructions == null) {
            instructions = new LinkedList<>();
            for (AbstractInsnNode node : nodes.values()) {
                instructions.add(method().find(node));
            }
        }
        return instructions;
    }

    public <E extends AbstractNode> E next(E start, int type, int dist) {
        int location = instructions().indexOf(start) + 1;
        Block current = this;
        if (location >= instructions().size()) {
            Block follow = graph.follow(this);
            if (isEnd() || follow == null) {
                return null;
            }
            current = follow;
            location = 0;
        }
        switch (type) {
            case AbstractNode.BASIC_NODE:
                return (E) current.instructions().get(location);
            default:
                int total = 0;
                while (total < dist) {
                    if (total > dist) {
                        break;
                    }
                    if (location >= instructions().size()) {
                        Block follow = graph.follow(this);
                        if (isEnd() || follow == null) {
                            break;
                        }
                        current = follow;
                        location = 0;
                        continue;
                    }
                    if (current.instructions().get(location).type() == type) {
                        return (E) current.instructions().get(location);
                    }
                    location++;
                    total++;
                }
                break;
        }
        return null;
    }

    public <E extends AbstractNode> E next(E start, int type) {
        int location = instructions().indexOf(start) + 1;
        Block current = this;
        if (location >= instructions().size()) {
            Block follow = graph.follow(this);
            if (isEnd() || follow == null) {
                return null;
            }
            current = follow;
            location = 0;
        }
        switch (type) {
            case AbstractNode.BASIC_NODE:
                return (E) current.instructions().get(location);
            default:
                int total = 0;
                while (true) {
                    if (location >= instructions().size()) {
                        Block follow = graph.follow(this);
                        if (isEnd() || follow == null) {
                            break;
                        }
                        current = follow;
                        location = 0;
                        continue;
                    }
                    if (current.instructions().get(location).type() == type) {
                        return (E) current.instructions().get(location);
                    }
                    location++;
                    total++;
                }
                break;
        }
        return null;
    }

    public <E extends AbstractNode> E prev(E start, int type) {
        int location = instructions().indexOf(start) - 1;
        Block current = this;
        if (location < 0) {
            Block follow = graph.followBackwards(this);
            if (isEnd() || follow == null) {
                return null;
            }
            current = follow;
            location = 0;
        }
        switch (type) {
            case AbstractNode.BASIC_NODE:
                return (E) current.instructions().get(location);
            default:
                while (true) {
                    if (location < 0) {
                        Block follow = graph.followBackwards(this);
                        if (isEnd() || follow == null) {
                            break;
                        }
                        current = follow;
                        location = 0;
                        continue;
                    }
                    if (current.instructions().get(location).type() == type) {
                        return (E) current.instructions().get(location);
                    }
                    location--;
                }
                break;
        }
        return null;
    }

    public <E extends AbstractNode> E prev(E start, int type, int dist) {
        int location = instructions().indexOf(start) - 1;
        Block current = this;
        if (location < 0) {
            Block follow = graph.followBackwards(this);
            if (isEnd() || follow == null) {
                return null;
            }
            current = follow;
            location = 0;
        }
        switch (type) {
            case AbstractNode.BASIC_NODE:
                return (E) current.instructions().get(location);
            default:
                int total = 0;
                while (total < dist) {
                    if (total > dist) {
                        break;
                    }
                    if (location < 0) {
                        Block follow = graph.followBackwards(this);
                        if (isEnd() || follow == null) {
                            break;
                        }
                        current = follow;
                        location = 0;
                        continue;
                    }
                    if (current.instructions().get(location).type() == type) {
                        return (E) current.instructions().get(location);
                    }
                    location--;
                    total++;
                }
                break;
        }
        return null;
    }

    public void add(int idx, AbstractInsnNode node) {
        nodes.put(idx, node);
    }

    public void visit() {
        visited = true;
    }

    public boolean visited() {
        return visited;
    }

    public MethodElement method() {
        return method;
    }

    public void merge(Block block) {
        Map.Entry<Integer, AbstractInsnNode> last = null;
        Iterator<Map.Entry<Integer, AbstractInsnNode>> iterator = block.nodes().entrySet().iterator();
        while(iterator.hasNext()) {
            last = iterator.next();
        }
        if (last != null && last.getValue().getOpcode() == Opcodes.GOTO) {
            block.nodes().remove(last.getKey());
        }
        nodes.putAll(block.nodes);
        destination = block.destination;
    }

    public boolean isEnd() {
        return destination == null;
    }

    public String format() {
        if (position == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder("Position: ").append(method.node().instructions.indexOf(position)).append("\n");
        if (!isEnd()) {
            builder.append("Destination: ").append(method.node().instructions.indexOf(destination)).append("\n");
        }
        for (AbstractInsnNode ain : nodes.values()) {
            builder.append(ASMUtil.OPCODE_MAP.get(ain.getOpcode())).append("\n");
        }
        return builder.toString();
    }

    public void setPosition(AbstractInsnNode position) {
        this.position = position;
    }

    public void setDestination(AbstractInsnNode destination) {
        this.destination = destination;
    }

    public AbstractInsnNode position() {
        return position;
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public AbstractInsnNode destination() {
        return destination;
    }

    public Map<Integer, AbstractInsnNode> nodes() {
        return nodes;
    }
}
