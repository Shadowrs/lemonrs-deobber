package net.lemonrs.lemonpicker.identifier;

import net.lemonrs.lemonpicker.bytecode.element.FieldElement;

/**
 * @author : const_
 */
public abstract class AbstractFieldIdentifier {

    private FieldElement identified;

    public abstract FieldElement identify();

    public void run() {
        identified = identify();
    }

    public FieldElement identified() {
        return identified;
    }

    public String format() {
        StringBuilder builder = new StringBuilder("\t\t\t\t ↔ ");
        builder.append(String.valueOf(getClass().getSimpleName().charAt(0)).toLowerCase())
                .append(getClass().getSimpleName().substring(1));
        if (broken()) {
            return builder.append(" is broken").toString();
        }
        builder.append(" identified as ").append(identified.parent().name()).append(".").append(identified.name());
        return builder.toString();
    }

    public boolean broken() {
        return identified == null;
    }
}
