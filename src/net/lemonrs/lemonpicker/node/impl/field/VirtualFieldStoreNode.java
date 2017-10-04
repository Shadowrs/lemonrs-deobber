package net.lemonrs.lemonpicker.node.impl.field;

import net.lemonrs.lemonpicker.node.AbstractNode;
import org.objectweb.asm.tree.FieldInsnNode;

/**
 * @author : const_
 */
public class VirtualFieldStoreNode extends AbstractFieldNode {

    public VirtualFieldStoreNode(FieldInsnNode node) {
        super(node);
    }

    @Override
    public int type() {
        return AbstractNode.VIRTUAL_FIELD_STORE_NODE;
    }

}
