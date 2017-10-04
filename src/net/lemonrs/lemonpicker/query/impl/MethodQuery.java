package net.lemonrs.lemonpicker.query.impl;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.query.AbstractQuery;
import net.lemonrs.lemonpicker.query.Filter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public class MethodQuery extends AbstractQuery<MethodElement, MethodQuery> {

    private List<MethodElement> methods;

    public MethodQuery() {
        methods = new LinkedList<>();
        for (ClassElement element : Main.elements) {
            methods.addAll(element.methods());
        }
    }

    public MethodQuery(ClassElement parent) {
        methods = parent.methods();
    }

    public MethodQuery(List<MethodElement> methods) {
        this.methods = methods;
    }

    public MethodQuery(ClassElement... parents) {
        methods = new LinkedList<>();
        for (ClassElement parent : parents) {
            methods.addAll(parent.methods());
        }
    }

    public MethodQuery member() {
        filter(new Filter<MethodElement>() {
            @Override
            public boolean accept(MethodElement obj) {
                return obj.member();
            }
        });
        return this;
    }

    public MethodQuery notMember() {
        filter(new Filter<MethodElement>() {
            @Override
            public boolean accept(MethodElement obj) {
                return !obj.member();
            }
        });
        return this;
    }

    public MethodQuery returns(final String desc) {
        filter(new Filter<MethodElement>() {
            @Override
            public boolean accept(MethodElement obj) {
                return obj.desc().split("\\)")[1].equals(desc);
            }
        });
        return this;
    }

    public MethodQuery hasCast(final String name) {
        filter(new Filter<MethodElement>() {
            @Override
            public boolean accept(MethodElement obj) {
                return obj.hasCast(name);
            }
        });
        return this;
    }

    public MethodQuery hasFieldDesc(final String desc, final boolean member) {
        filter(new Filter<MethodElement>() {
            @Override
            public boolean accept(MethodElement obj) {
                for(FieldElement element :obj.fields()) {
                    if(element.desc().equals(desc) && element.member() == member) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }
    public MethodQuery constant(final Number... constants) {
        filter(new Filter<MethodElement>() {
            @Override
            public boolean accept(MethodElement obj) {
                for (Number constant : constants) {
                    if (!obj.hasConstant(constant)) {
                        return false;
                    }
                }
                return true;
            }
        });
        return this;
    }

    public MethodQuery takes(final String desc) {
        filter(new Filter<MethodElement>() {
            @Override
            public boolean accept(MethodElement obj) {
                return obj.desc().substring(obj.desc().indexOf('('), obj.desc().lastIndexOf(')')).contains(desc);
            }
        });
        return this;
    }

    public MethodQuery references(final FieldElement element) {
        filter(new Filter<MethodElement>() {
            @Override
            public boolean accept(MethodElement obj) {
                return obj.fields().contains(element);
            }
        });
        return this;
    }
    @Override
    public List<MethodElement> all() {
        List<MethodElement> results = filterList(methods);
        return results.isEmpty() ? null : results;
    }

    @Override
    public MethodElement first() {
        List<MethodElement> results = all();
        if (results == null) {
            return null;
        }
        return results.get(0);
    }
}
