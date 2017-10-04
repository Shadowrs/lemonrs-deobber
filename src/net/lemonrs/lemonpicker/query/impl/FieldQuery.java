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
public class FieldQuery extends AbstractQuery<FieldElement, FieldQuery> {

    private List<FieldElement> fields = new LinkedList<>();

    public FieldQuery() {
        for (ClassElement element : Main.elements) {
            fields.addAll(element.fields());
        }
    }

    public FieldQuery(ClassElement... elements) {
        for (ClassElement element : elements) {
            fields.addAll(element.fields());
        }
    }

    public FieldQuery(List<MethodElement> elements) {
        for (MethodElement element : elements) {
            fields.addAll(element.fields());
        }
    }

    public FieldQuery(ClassElement element) {
        fields.addAll(element.fields());
    }

    public FieldQuery(MethodElement element) {
        fields.addAll(element.fields());
    }

    public FieldQuery member() {
        filter(new Filter<FieldElement>() {
            @Override
            public boolean accept(FieldElement obj) {
                return obj.member();
            }
        });
        return this;
    }

    public FieldQuery notMember() {
        filter(new Filter<FieldElement>() {
            @Override
            public boolean accept(FieldElement obj) {
                return !obj.member();
            }
        });
        return this;
    }

    public FieldQuery desc(final String desc) {
        filter(new Filter<FieldElement>() {
            @Override
            public boolean accept(FieldElement obj) {
                return obj.desc().equals(desc);
            }
        });
        return this;
    }


    public FieldQuery named(final String name) {
        filter(new Filter<FieldElement>() {
            @Override
            public boolean accept(FieldElement obj) {
                return obj.name().equals(name);
            }
        });
        return this;
    }


    public FieldQuery notNamed(final String name) {
        filter(new Filter<FieldElement>() {
            @Override
            public boolean accept(FieldElement obj) {
                return !obj.name().equals(name);
            }
        });
        return this;
    }

    public FieldQuery owner(final String owner) {
        filter(new Filter<FieldElement>() {
            @Override
            public boolean accept(FieldElement obj) {
                return obj.parent().name().equals(owner);
            }
        });
        return this;
    }

    public FieldQuery inMethod(final MethodElement element) {
        filter(new Filter<FieldElement>() {
            @Override
            public boolean accept(FieldElement obj) {
                return element.fields().contains(obj);
            }
        });
        return this;
    }

    @Override
    public List<FieldElement> all() {
        List<FieldElement> results = filterList(fields);
        return results.isEmpty() ? null : results;
    }

    @Override
    public FieldElement first() {
        List<FieldElement> results = all();
        if (results == null) {
            return null;
        }
        return results.get(0);
    }
}
