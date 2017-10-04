package net.lemonrs.lemonpicker.node.impl.type;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * @author : const_
 */
public class NewNode extends AbstractTypeNode {

    public NewNode(TypeInsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.NEW_NODE;
    }
}
