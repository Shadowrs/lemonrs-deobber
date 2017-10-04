package net.lemonrs.lemonpicker.graph.hierarchy;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author : const_
 */
public class HierarchyTree {

    private List<HierarchyBranch> branches = new LinkedList<>();
    private List<ClassElement> elements;
    private List<ClassElement> all;

    public HierarchyTree(List<ClassElement> elements) {
        this.elements = new LinkedList<>(elements);
        this.all = new LinkedList<>(elements);
    }

    public void build() {
        initBranches();
        buildBranches();
    }

    private void buildBranches() {
        for (HierarchyBranch branch : branches) {
            ClassElement next;
            while ((next = next(branch.last())) != null) {
                branch.add(next);
            }
        }
        for (HierarchyBranch branch : branches) {
            for (ClassElement element : branch.elements()) {
                element.addBranch(branch);
            }
        }
    }

    private ClassElement next(ClassElement element) {
        for (ClassElement _element : all) {
            if (element.superName().equals(_element.name())) {
                return _element;
            }
        }
        return null;
    }

    private void initBranches() {
        ListIterator<ClassElement> iterator = elements.listIterator();
        iterator:
        while (iterator.hasNext()) {
            ClassElement next = iterator.next();
            for (ClassElement element : all) {
                if (element.superName().equals(next.name())) {
                    continue iterator;
                }
            }
            branches.add(new HierarchyBranch(next));
            iterator.remove();
        }
    }

    @Override
    public String toString() {
        String content = branches.get(0).toString();
        for (int i = 1; i < branches.size(); i++) {
            content += "\n" + branches.get(i).toString();
        }
        return content;
    }

    public List<HierarchyBranch> branches() {
        return branches;
    }

    public List<ClassElement> elements() {
        return elements;
    }

    public List<ClassElement> all() {
        return all;
    }
}
