package net.lemonrs.lemonpicker.identifier;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.util.Timer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : const_
 */
public abstract class AbstractClassIdentifier {

    private ClassElement identified;
    private List<AbstractFieldIdentifier> fieldIdentifiers = new LinkedList<>();
    private long fieldExec;

    public abstract ClassElement identify();

    public void run() {
        identified = identify();
    }

    public void add(AbstractFieldIdentifier fieldIdentifier) {
        fieldIdentifiers.add(fieldIdentifier);
    }

    public void runFields() {
        Timer timer = new Timer();
        timer.start();
        for (AbstractFieldIdentifier fieldIdentifier : fieldIdentifiers) {
            fieldIdentifier.run();
        }
        fieldExec = timer.clock();
    }

    public String format() {
        StringBuilder builder = new StringBuilder("\t\t\t ↔ ");
        builder.append(getClass().getSimpleName().substring(0, getClass().getSimpleName().indexOf("Identifier")));
        if (broken()) {
            return builder.append(" is broken").toString();
        }
        builder.append(" identified as ").append(identified.name()).append("\n");
        int found = 0;
        for (AbstractFieldIdentifier fieldIdentifier : fieldIdentifiers) {
            Main.totalFields++;
            if (!fieldIdentifier.broken()) {
                Main.foundFields++;
                found++;
            }
            builder.append(fieldIdentifier.format()).append("\n");
        }
        builder.append("\t\t\t\t\t ↔ Identified ").append(found).append(" out of ")
                .append(fieldIdentifiers.size()).append(" seeds in ").append(fieldExec).append("ms.\n");
        return builder.toString();
    }

    public ClassElement identified() {
        return identified;
    }

    public boolean broken() {
        return identified == null;
    }
}
