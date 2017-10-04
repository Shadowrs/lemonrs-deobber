package net.lemonrs.lemonpicker.query.impl;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.graph.hierarchy.HierarchyBranch;
import net.lemonrs.lemonpicker.query.AbstractQuery;
import net.lemonrs.lemonpicker.query.Filter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class ClassQuery extends AbstractQuery<ClassElement, ClassQuery> {


    public ClassQuery abstractClass() {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.isAbstract();
            }
        });
        return this;
    }

    public ClassQuery fieldCount(final int count, final boolean member) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                int total = 0;
                for (FieldElement element : obj.fields()) {
                    if (element.member() == member) {
                        total++;
                    }
                }
                return total == count;
            }
        });
        return this;
    }

    public ClassQuery onBranch(final ClassElement element) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                for (HierarchyBranch branch : element.branches()) {
                    if (branch.contains(element)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    public ClassQuery onBranchAt(final ClassElement element, final int pos) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                for (HierarchyBranch branch : element.branches()) {
                    if (branch.contains(element, pos)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    public ClassQuery notNamed(final String name) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return !obj.name().equals(name);
            }
        });
        return this;
    }

    public ClassQuery named(final String name) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.name().equals(name);
            }
        });
        return this;
    }

    public ClassQuery branchSize(final int size) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                for (HierarchyBranch branch : obj.branches()) {
                    if (branch.size() == size) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    public ClassQuery hasField(final String desc, final boolean member) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasField(desc, member);
            }
        });
        return this;
    }

    public ClassQuery hasFieldDescStartWith(final String desc, final boolean member) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                for (FieldElement element : obj.fields()) {
                    if (element.desc().startsWith(desc) &&
                            element.member() == member) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    public ClassQuery classHasField(final ClassElement element, final String desc, final boolean member) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return element.hasField(desc + "L" + obj.name() + ";", member);
            }
        });
        return this;
    }


    public ClassQuery hasFieldLeast(final String desc, final boolean member, final int count) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasFieldLeast(desc, member, count);
            }
        });
        return this;
    }

    public ClassQuery hasField(final String desc, final boolean member, final int count) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasField(desc, member, count);
            }
        });
        return this;
    }

    public ClassQuery fieldCount(final int count) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                int total = 0;
                for (FieldElement element : obj.fields()) {
                    if (element.member()) {
                        total++;
                    }
                }
                return total == count;
            }
        });
        return this;
    }

    public ClassQuery extend(final String name) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.superName().equals(name);
            }
        });
        return this;
    }

    public ClassQuery implement(final String name) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.node().interfaces.contains(name);
            }
        });
        return this;
    }

    public ClassQuery hasMethod(final String desc, final boolean member) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasMethod(desc, member);
            }
        });
        return this;
    }

    public ClassQuery hasMethodReturns(final String desc, final boolean member) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasMethodReturns(desc, member);
            }
        });
        return this;
    }
    public ClassQuery notImplement(final String name) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return !obj.node().interfaces.contains(name);
            }
        });
        return this;
    }

    public ClassQuery hasFieldLeast(final String desc, final int count) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasFieldLeast(desc, count);
            }
        });
        return this;
    }

    public ClassQuery hasField(final String desc, final int count) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasField(desc, count);
            }
        });
        return this;
    }

    public ClassQuery hasFieldLessThan(final String desc, final boolean member, final int count) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasFieldLessThan(desc, member, count);
            }
        });
        return this;
    }

    public ClassQuery hasFieldLessThan(final String desc, final int count) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasFieldLessThan(desc, count);
            }
        });
        return this;
    }

    public ClassQuery hasField(final String desc) {
        filter(new Filter<ClassElement>() {
            @Override
            public boolean accept(ClassElement obj) {
                return obj.hasField(desc);
            }
        });
        return this;
    }

    public ClassElement firstOnBranch(int pos) {
        ClassElement first = first();
        if (first != null) {
            for (HierarchyBranch branch : first.branches()) {
                if (branch.size() > pos) {
                    if (isAccepted(branch.get(pos))) {
                        return branch.get(pos);
                    }
                }
            }
        }
        return null;
    }

    public List<ClassElement> allOnBranch(int pos) {
        List<ClassElement> all = all();
        List<ClassElement> results = new LinkedList<>();
        if (all != null) {
            for (ClassElement element : all) {
                for (HierarchyBranch branch : element.branches()) {
                    if (branch.size() > pos && isAccepted(branch.get(pos))) {
                        results.add(branch.get(pos));
                    }
                }
            }
        }
        return results.isEmpty() ? null : results;
    }

    @Override
    public List<ClassElement> all() {
        List<ClassElement> elements = filterList(Main.elements);
        return elements.isEmpty() ? null : elements;
    }

    @Override
    public ClassElement first() {
        List<ClassElement> all = all();
        return all != null ? all.get(0) : null;
    }
}
