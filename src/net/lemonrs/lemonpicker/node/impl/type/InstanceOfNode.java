package net.lemonrs.lemonpicker.node.impl.type;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * @author : const_
 */
public class InstanceOfNode extends AbstractTypeNode {

    public InstanceOfNode(TypeInsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.INSTANCE_OF_NODE;
    }
}
