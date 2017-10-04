package net.lemonrs.lemonpicker.query;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public abstract class AbstractQuery<T, Q extends AbstractQuery> implements Iterable<T> {

    private List<Filter<T>> filters = new LinkedList<>();
    private List<Comparator<T>> comparators = new LinkedList<>();

    @Override
    public Iterator<T> iterator() {
        List<T> all = all();
        return all != null ? all.iterator() : new LinkedList<T>().iterator();
    }

    public boolean isAccepted(T element) {
        for (Filter<T> filter : filters) {
            if (filter.accept(element)) {
                continue;
            }
            return false;
        }
        return true;
    }

    public List<T> filterList(List<T> list) {
        List<T> results = new LinkedList<>();
        objects:
        for (T t : list) {
            if (isAccepted(t)) {
                results.add(t);
            }
        }
        return results;
    }

    public abstract List<T> all();

    public abstract T first();

    public Q filter(Filter<T> filter) {
        filters.add(filter);
        return (Q) this;
    }

    public Q comparate(Comparator<T> comparator) {
        comparators.add(comparator);
        return (Q) this;
    }
}
