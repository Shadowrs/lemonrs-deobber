package net.lemonrs.lemonpicker;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.deob.Deobfuscator;
import net.lemonrs.lemonpicker.graph.hierarchy.HierarchyTree;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.ClientIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.FacadeIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.CacheIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.ModelIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.WidgetIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.WidgetNodeIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.ItemDefinitionIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.NpcDefinitionIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.ObjectDefinitionIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.PlayerDefinitionIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.input.KeyboardIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.input.MouseIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.net.FileOnDiskIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.net.SocketIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.net.StreamIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.*;
import net.lemonrs.lemonpicker.identifier.impl.renderable.*;
import net.lemonrs.lemonpicker.identifier.impl.scene.CollisionMapIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.scene.RegionIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.scene.TileIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.scene.object.*;
import net.lemonrs.lemonpicker.util.ASMUtil;
import net.lemonrs.lemonpicker.util.RevisionFinder;
import net.lemonrs.lemonpicker.util.Timer;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static List<ClassElement> elements;
    public static HierarchyTree hierarchyTree;
    public static List<AbstractClassIdentifier> classIdentifiers;
    public static int totalFields, foundFields;

    public static void main(String[] args) {
        Timer timer = new Timer();
        classIdentifiers = new LinkedList<>();
        initIdentifiers();
        System.out.println("\t\t↔ LemonPicker initialized...");
        System.out.println("\t\t↔ Finding tree...");
        timer.start();
        elements = ASMUtil.load(new File("./gamepack.jar"));
        System.out.println("\t\t↔ Located tree with " + elements.size() + " lemons in " + timer.clock() + "ms");
        int revision = RevisionFinder.find();
        System.out.println("\t\t↔ Measured tree with a height of " + revision + " meters");
        System.out.println("\t\t↔ Throwing away rotten lemons...");
        Deobfuscator.run(elements);
        System.out.println("\t\t↔ Finished throwing away rotten lemons...");
        ASMUtil.save(new File("./gamepackdeob.jar"), elements);
        System.out.println("\t\t↔ Replanting tree...");
        elements = ASMUtil.load(new File("./gamepackdeob.jar"));
        System.out.println("\t\t↔ Locating lemons...");
        timer.start();
        hierarchyTree = new HierarchyTree(elements);
        hierarchyTree.build();
       // System.out.println(hierarchyTree.toString());
        System.out.println("\t\t↔ Constructed graph of lemons in " + timer.clock() + "ms");
        System.out.println("\t\t↔ Beginning to name lemons and their seeds...");
        timer.start();
        for (AbstractClassIdentifier ident : classIdentifiers) {
            ident.run();
        }
        long finish = timer.clock();
        timer.start();
        for (AbstractClassIdentifier ident : classIdentifiers) {
            ident.runFields();
        }
        long fieldsFinish = timer.clock();
        int found = 0;
        for(AbstractClassIdentifier ident : classIdentifiers) {
            if(!ident.broken()) {
                found++;
            }
            System.out.println(ident.format());
        }
        System.out.println("\t\t↔ Named " + found + " out of " + classIdentifiers.size() + " lemons in " + finish + "ms.");
        System.out.println("\t\t↔ Named " + foundFields + " out of " + totalFields + " seeds in " + fieldsFinish + "ms.");
    }

    private static void initIdentifiers() {
        classIdentifiers.add(new ClientIdentifier());
        classIdentifiers.add(new NodeIdentifier());
        classIdentifiers.add(new CacheableNodeIdentifier());
        classIdentifiers.add(new DequeIdentifier());
        classIdentifiers.add(new QueueIdentifier());
        classIdentifiers.add(new BagIdentifier());
        classIdentifiers.add(new CacheIdentifier());
        classIdentifiers.add(new StreamIdentifier());
        classIdentifiers.add(new SocketIdentifier());
        classIdentifiers.add(new FileOnDiskIdentifier());
        classIdentifiers.add(new PlayerIdentifier());
        classIdentifiers.add(new NpcIdentifier());
        classIdentifiers.add(new ActorIdentifier());
        classIdentifiers.add(new RenderableIdentifier());
        classIdentifiers.add(new ModelIdentifier());
        classIdentifiers.add(new LootIdentifier());
        classIdentifiers.add(new WidgetIdentifier());
        classIdentifiers.add(new NpcDefinitionIdentifier());
        classIdentifiers.add(new PlayerDefinitionIdentifier());
        classIdentifiers.add(new ItemDefinitionIdentifier());
        classIdentifiers.add(new ObjectDefinitionIdentifier());
        classIdentifiers.add(new RegionIdentifier());
        classIdentifiers.add(new TileIdentifier());
        classIdentifiers.add(new GameObjectIdentifier());
        classIdentifiers.add(new WallObjectIdentifier());
        classIdentifiers.add(new BoundaryObjectIdentifier());
        classIdentifiers.add(new GroundObjectIdentifier());
        classIdentifiers.add(new GroundLayerIdentifier());
        classIdentifiers.add(new CollisionMapIdentifier());
        classIdentifiers.add(new LootIdentifier());
        classIdentifiers.add(new WidgetNodeIdentifier());
        classIdentifiers.add(new MouseIdentifier());
        classIdentifiers.add(new KeyboardIdentifier());
        classIdentifiers.add(new FacadeIdentifier());
    }

    public static <T extends AbstractClassIdentifier>  T get(Class<T> clazz) {
        for(AbstractClassIdentifier identifier : classIdentifiers) {
            if(identifier.getClass().equals(clazz)) {
                return (T) identifier;
            }
        }
        return null;
    }

    public static ClassElement get(String name) {
        for (ClassElement element : elements) {
            if (element.name().equals(name)) {
                return element;
            }
        }
        return null;
    }
}
