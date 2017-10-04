package net.lemonrs.lemonpicker.graph.hierarchy;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class HierarchyBranch {

    private ClassElement root;
    private List<ClassElement> elements = new LinkedList<>();

    public HierarchyBranch(ClassElement root) {
        this.root = root;
        elements.add(root);
    }

    public void add(ClassElement element) {
        elements.add(element);
    }

    public List<ClassElement> elements() {
        return elements;
    }

    public boolean contains(ClassElement element) {
        return elements().contains(element);
    }

    public boolean contains(ClassElement element, int pos) {
        return get(pos).equals(element);
    }

    public ClassElement last() {
        return elements.get(elements.size() - 1);
    }

    public ClassElement get(int index) {
        return elements.get(index);
    }

    public ClassElement first() {
        return root;
    }

    public int size() {
        return elements.size();
    }

    @Override
    public String toString() {
        String content = elements.get(0).name();
        for(int i = 1; i < size(); i++) {
            content += " --> " + elements.get(i).name();
        }
        return content;
    }
}
