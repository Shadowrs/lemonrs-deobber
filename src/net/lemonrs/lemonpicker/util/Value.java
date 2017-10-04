package net.lemonrs.lemonpicker.util;

/**
 * @author : const_
 */
public class Value<T> {

    private T value;
    private boolean set = false;

    public void set(T value) {
        this.value = value;
        set = true;
    }

    public boolean set() {
        return set;
    }

    public T value() {
        return value;
    }
}
