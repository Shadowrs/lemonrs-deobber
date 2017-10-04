package net.lemonrs.lemonpicker.bytecode.element;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.graph.hierarchy.HierarchyBranch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import javax.naming.directory.ModificationItem;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class ClassElement {

    private ClassNode node;
    private ClassReader reader;
    private List<MethodElement> methods;
    private List<FieldElement> fields;
    private List<HierarchyBranch> branches = new LinkedList<>();

    public ClassElement(ClassNode node, ClassReader reader) {
        this.node = node;
        this.reader = reader;
        fields();
        methods();
    }

    public String superName() {
        return node().superName;
    }

    public boolean hasMethod(String desc, boolean member) {
        for (MethodElement element : methods()) {
            if (element.desc().equals(desc) &&
                    element.member() == member) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMethodReturns(String desc, boolean member) {
        for (MethodElement element : methods()) {
            if (element.desc().split("\\)")[1].equals(desc) &&
                    element.member() == member) {
                return true;
            }
        }
        return false;
    }

    public MethodElement findMethod(String name, String desc) {
        for (MethodElement element : methods()) {
            if (element.desc().equals(desc) &&
                    element.name().equals(name)) {
                return element;
            }
        }
        return null;
    }

    public FieldElement findField(String name) {
        ClassElement super_ = this;
        while (super_ != null) {
            for (FieldElement field : super_.fields()) {
                if (field.name().equals(name)) {
                    return field;
                }
            }
            super_ = Main.get(super_.node().superName);
        }
        return null;
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(node().access);
    }

    public boolean hasField(String desc) {
        for (FieldElement element : fields()) {
            if (element.desc().equals(desc)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFieldLeast(String desc, int count) {
        int total = 0;
        for (FieldElement element : fields()) {
            if (element.desc().equals(desc)) {
                total++;
            }
        }
        return total >= count;
    }

    public boolean hasField(String desc, int count) {
        int total = 0;
        for (FieldElement element : fields()) {
            if (element.desc().equals(desc)) {
                total++;
            }
        }
        return total == count;
    }

    public boolean hasFieldLessThan(String desc, int count) {
        int total = 0;
        for (FieldElement element : fields()) {
            if (element.desc().equals(desc)) {
                total++;
            }
        }
        return total < count;
    }

    public boolean hasFieldLeast(String desc, boolean member, int count) {
        int total = 0;
        for (FieldElement element : fields()) {
            if (element.desc().equals(desc) &&
                    element.member() == member) {
                total++;
            }
        }
        return total >= count;
    }

    public boolean hasField(String desc, boolean member, int count) {
        int total = 0;
        for (FieldElement element : fields()) {
            if (element.desc().equals(desc) &&
                    element.member() == member) {
                total++;
            }
        }
        return total == count;
    }

    public boolean hasFieldLessThan(String desc, boolean member, int count) {
        int total = 0;
        for (FieldElement element : fields()) {
            if (element.desc().equals(desc) &&
                    element.member() == member) {
                total++;
            }
        }
        return total < count;
    }

    public boolean hasField(String desc, boolean member) {
        for (FieldElement element : fields()) {
            if (element.desc().equals(desc) &&
                    element.member() == member) {
                return true;
            }
        }
        return false;
    }

    public void addBranch(HierarchyBranch branch) {
        branches.add(branch);
    }

    public List<HierarchyBranch> branches() {
        return branches;
    }

    public HierarchyBranch branch() {
        return branches.get(0);
    }

    public ClassReader reader() {
        return reader;
    }

    public ClassNode node() {
        return node;
    }

    public String name() {
        return node().name;
    }

    public List<MethodElement> methods() {
        if (methods == null) {
            methods = new LinkedList<>();
            for (MethodNode mn : node().methods) {
                methods.add(new MethodElement(this, mn));
            }
        }
        return methods;
    }

    public List<FieldElement> fields() {
        if (fields == null) {
            fields = new LinkedList<>();
            for (FieldNode fn : node().fields) {
                fields.add(new FieldElement(this, fn));
            }
        }
        return fields;
    }
}
