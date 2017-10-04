package net.lemonrs.lemonpicker.node.impl.type;

import net.lemonrs.lemonpicker.node.impl.BasicNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * @author : const_
 */
public abstract class AbstractTypeNode extends BasicNode<TypeInsnNode> {

    public AbstractTypeNode(TypeInsnNode node) {
        super(node);
    }

    public String desc() {
        return node().desc;
    }
}
