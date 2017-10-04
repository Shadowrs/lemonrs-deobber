package net.lemonrs.lemonpicker.util;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.operand.PushNode;
import net.lemonrs.lemonpicker.query.impl.MethodQuery;
import net.lemonrs.lemonpicker.tree.MethodVisitor;

/**
 * @author : const_
 */
public class RevisionFinder {

    public static int find() {
        MethodQuery query = new MethodQuery(Main.get("client"));
        final Value<Integer> value = new Value<>();
        for (MethodElement element : query.constant(765, 503)) {
            final Flag width = new Flag();
            MethodVisitor visitor = new MethodVisitor(element) {
                @Override
                public void visitPush(PushNode node) {
                    super.visitPush(node);
                    if (node.push() == 765) {
                        width.flag();
                    }
                    if (width.flagged() && node.push() == 503 && !value.set()) {
                        PushNode next = node.next(AbstractNode.PUSH_NODE);
                        if (next != null) {
                            value.set(next.push());
                        }
                    }
                }
            };
            if (value.set()) {
                return value.value();
            }
        }
        return value.set() ? value.value() : -1;
    }
}
