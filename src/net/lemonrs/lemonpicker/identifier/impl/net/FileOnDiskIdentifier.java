package net.lemonrs.lemonpicker.identifier.impl.net;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.NodeIdentifier;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;

/**
 * @author : const_
 */
public class FileOnDiskIdentifier extends AbstractClassIdentifier {
    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        return query.branchSize(1).hasField("Ljava/io/RandomAccessFile;", true, 1).hasField("J", true, 2).fieldCount(3).first();
    }
}
