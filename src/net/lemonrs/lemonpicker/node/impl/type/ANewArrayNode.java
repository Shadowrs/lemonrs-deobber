package net.lemonrs.lemonpicker.node.impl.type;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * @author : const_
 */
public class ANewArrayNode extends AbstractTypeNode {

    public ANewArrayNode(TypeInsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.ANEW_ARRAY_NODE;
    }
}
