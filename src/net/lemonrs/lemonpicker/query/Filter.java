package net.lemonrs.lemonpicker.query;

/**
 * @author : const_
 */
public interface Filter<T> {

    public boolean accept(T obj);

}
