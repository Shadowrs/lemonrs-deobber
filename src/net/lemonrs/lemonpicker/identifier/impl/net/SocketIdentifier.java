package net.lemonrs.lemonpicker.identifier.impl.net;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.NodeIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;

/**
 * @author : const_
 */
public class SocketIdentifier extends AbstractClassIdentifier {
    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasField("Ljava/net/Socket;", true, 1).hasField("Ljava/io/OutputStream;",true, 1)
                .hasField("Ljava/io/InputStream;", true, 1).implement("java/lang/Runnable").firstOnBranch(0);
    }
}
