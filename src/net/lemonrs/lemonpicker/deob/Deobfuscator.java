package net.lemonrs.lemonpicker.deob;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.deob.impl.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class Deobfuscator {

    private static final List<Transform> TRANSFORMS = new LinkedList<>();

    static {
        TRANSFORMS.add(new IllegalStateExceptionRemovalTransform());
     //   TRANSFORMS.add(new UnusedFieldRemovalTransform());
        TRANSFORMS.add(new UnusedClassRemovalTransform());
        // TRANSFORMS.add(new UnusedMethodRemovalTransform());
        TRANSFORMS.add(new ArithmeticStatementOrderTransform());
        //   TRANSFORMS.add(new OpaquePredicateRemovalTransform());
        //TRANSFORMS.add(new ControlFlowTransform());
    }

    public static void run(List<ClassElement> elements) {
        for (Transform transform : TRANSFORMS) {
            transform.execute(elements);
            System.out.println(transform.result());
        }
    }
}
