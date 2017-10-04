package net.lemonrs.lemonpicker.identifier.impl;

import net.lemonrs.lemonpicker.Main;
import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import net.lemonrs.lemonpicker.bytecode.element.FieldElement;
import net.lemonrs.lemonpicker.bytecode.element.MethodElement;
import net.lemonrs.lemonpicker.identifier.AbstractClassIdentifier;
import net.lemonrs.lemonpicker.identifier.AbstractFieldIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.CacheIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.WidgetIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.WidgetNodeIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.ItemDefinitionIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.NpcDefinitionIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.cache.definition.ObjectDefinitionIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.BagIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.node.DequeIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.renderable.LootIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.renderable.NpcIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.renderable.PlayerIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.scene.CollisionMapIdentifier;
import net.lemonrs.lemonpicker.identifier.impl.scene.RegionIdentifier;
import net.lemonrs.lemonpicker.node.AbstractNode;
import net.lemonrs.lemonpicker.node.impl.arith.ArithmeticOperationNode;
import net.lemonrs.lemonpicker.node.impl.field.StaticFieldCallNode;
import net.lemonrs.lemonpicker.node.impl.method.StaticMethodCallNode;
import net.lemonrs.lemonpicker.node.impl.operand.LocalVariableStoreNode;
import net.lemonrs.lemonpicker.node.impl.operand.PushNode;
import net.lemonrs.lemonpicker.node.impl.type.CastNode;
import net.lemonrs.lemonpicker.query.impl.ClassQuery;
import net.lemonrs.lemonpicker.query.impl.FieldQuery;
import net.lemonrs.lemonpicker.query.impl.MethodQuery;
import net.lemonrs.lemonpicker.tree.MethodVisitor;
import net.lemonrs.lemonpicker.util.Value;

/**
 * @author : const_
 */
public class ClientIdentifier extends AbstractClassIdentifier {

    public AbstractFieldIdentifier widgetNodeBag, loopCycle;

    public ClientIdentifier() {
        add(new LocalPlayer());
        add(new Players());
        add(new Npcs());
        add(new ItemDefinitionCache());
        add(new ObjectDefinitionCache());
        add(new NpcDefinitionCache());
        add(new Region());
        add(new LootDeque());
        add(new Canvas());
        add(widgetNodeBag = new WidgetNodeBag());
        add(new Widgets());
        add(new CollisionMap());
        add(loopCycle = new LoopCycle());
        add(new CameraX());
        add(new CameraY());
        add(new CameraZ());
        add(new CameraPitch());
        add(new CameraYaw());
        add(new SineTable());
        add(new CosineTable());
        add(new Plane());
    }

    @Override
    public ClassElement identify() {
        ClassQuery query = new ClassQuery();
        return query.named("client").first();
    }

    public class CameraX extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().notMember().returns("V")
                    .takes("I").constant(16, 128)) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.desc().equals("I")) {
                            LocalVariableStoreNode store = node.next(AbstractNode.LOCAL_VARIABLE_STORE_NODE, 6);
                            StaticMethodCallNode method = node.next(AbstractNode.STATIC_METHOD_CALL_NODE, 6);
                            if (store != null && method == null &&
                                    store.index() == 0 && store.desc().equals("I")) {
                                ArithmeticOperationNode sub = store.prev(AbstractNode.ARITHMETIC_OPERATION_NODE, 3);
                                if (sub != null && sub.operation() == ArithmeticOperationNode.Operation.SUBTRACT) {
                                    value.set(node.field());
                                }
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class CameraY extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().notMember().returns("V")
                    .takes("I").constant(16, 128)) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.desc().equals("I")) {
                            LocalVariableStoreNode store = node.next(AbstractNode.LOCAL_VARIABLE_STORE_NODE, 6);
                            StaticMethodCallNode method = node.next(AbstractNode.STATIC_METHOD_CALL_NODE, 6);
                            if (store != null && method == null &&
                                    store.index() == 1 && store.desc().equals("I")) {
                                ArithmeticOperationNode sub = store.prev(AbstractNode.ARITHMETIC_OPERATION_NODE, 3);
                                if (sub != null && sub.operation() == ArithmeticOperationNode.Operation.SUBTRACT) {
                                    value.set(node.field());
                                }
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class CameraZ extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().notMember().returns("V")
                    .takes("I").constant(16, 128)) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.desc().equals("I")) {
                            LocalVariableStoreNode store = node.next(AbstractNode.LOCAL_VARIABLE_STORE_NODE, 6);
                            StaticMethodCallNode method = node.next(AbstractNode.STATIC_METHOD_CALL_NODE, 6);
                            if (store != null && method == null &&
                                    store.index() == 3 && store.desc().equals("I")) {
                                ArithmeticOperationNode sub = store.prev(AbstractNode.ARITHMETIC_OPERATION_NODE, 3);
                                if (sub != null && sub.operation() == ArithmeticOperationNode.Operation.SUBTRACT) {
                                    value.set(node.field());
                                }
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class CameraPitch extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().notMember().returns("V")
                    .takes("I").constant(16, 128)) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.desc().equals("[I")) {
                            FieldElement sineTable = node.field();
                            StaticFieldCallNode pitch = node.next(AbstractNode.STATIC_FIELD_CALL_NODE, 3);
                            if (pitch != null && pitch.desc().equals("I")) {
                                LocalVariableStoreNode store = pitch.next(AbstractNode.LOCAL_VARIABLE_STORE_NODE, 6);
                                if (store != null && store.desc().equals("I") &&
                                        store.index() == 4) {
                                    value.set(pitch.field());
                                }
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class SineTable extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().notMember().returns("V")
                    .takes("I").constant(16, 128)) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.desc().equals("[I")) {
                            FieldElement sineTable = node.field();
                            StaticFieldCallNode pitch = node.next(AbstractNode.STATIC_FIELD_CALL_NODE, 3);
                            if (pitch != null && pitch.desc().equals("I")) {
                                LocalVariableStoreNode store = pitch.next(AbstractNode.LOCAL_VARIABLE_STORE_NODE, 6);
                                if (store != null && store.desc().equals("I") &&
                                        store.index() == 4) {
                                    value.set(sineTable);
                                }
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class CameraYaw extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().notMember().returns("V")
                    .takes("I").constant(16, 128)) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.desc().equals("[I")) {
                            FieldElement cosineTable = node.field();
                            StaticFieldCallNode yaw = node.next(AbstractNode.STATIC_FIELD_CALL_NODE, 3);
                            if (yaw != null && yaw.desc().equals("I")) {
                                LocalVariableStoreNode store = yaw.next(AbstractNode.LOCAL_VARIABLE_STORE_NODE, 6);
                                if (store != null && store.desc().equals("I") &&
                                        store.index() == 6) {
                                    value.set(yaw.field());
                                }
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class CosineTable extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().notMember().returns("V")
                    .takes("I").constant(16, 128)) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.desc().equals("[I")) {
                            FieldElement cosineTable = node.field();
                            StaticFieldCallNode yaw = node.next(AbstractNode.STATIC_FIELD_CALL_NODE, 3);
                            if (yaw != null && yaw.desc().equals("I")) {
                                LocalVariableStoreNode store = yaw.next(AbstractNode.LOCAL_VARIABLE_STORE_NODE, 6);
                                if (store != null && store.desc().equals("I") &&
                                        store.index() == 7) {
                                    value.set(cosineTable);
                                }
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class Plane extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().notMember().returns("V")
                    .takes("I").constant(16, 128)) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitStaticFieldCall(StaticFieldCallNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.desc().equals("I")) {
                            FieldElement plane = node.field();
                            StaticMethodCallNode method = node.next(AbstractNode.STATIC_METHOD_CALL_NODE, 6);
                            if (method != null && method.desc().endsWith(")I")) {
                                ArithmeticOperationNode operation = method.next(AbstractNode.ARITHMETIC_OPERATION_NODE, 5);
                                if (operation != null && operation.operation() == ArithmeticOperationNode.Operation.SUBTRACT) {
                                    value.set(plane);
                                }
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }


    public class LoopCycle extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().returns("V").member()) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitPush(PushNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.push() == 1000) {
                            StaticFieldCallNode call = node.prev(AbstractNode.STATIC_FIELD_CALL_NODE, 3);
                            ArithmeticOperationNode mul = node.next(AbstractNode.ARITHMETIC_OPERATION_NODE, 6);
                            if (mul != null && (mul.operation() == ArithmeticOperationNode.Operation.DIVIDE
                                    || mul.next(AbstractNode.ARITHMETIC_OPERATION_NODE, 6) != null &&
                                    ((ArithmeticOperationNode) mul.
                                            next(AbstractNode.ARITHMETIC_OPERATION_NODE, 6)).
                                            operation() == ArithmeticOperationNode.Operation.DIVIDE) &&
                                    call != null && call.desc().equals("I")) {
                                value.set(call.field());
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }

    public class LootDeque extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            ClassElement loot = Main.get(LootIdentifier.class).identified();
            ClassElement deque = Main.get(DequeIdentifier.class).identified();
            FieldQuery query = new FieldQuery(new MethodQuery().hasCast(loot.name()).
                    hasFieldDesc("[[[L" + deque.name() + ";", false).returns("V").all());
            return query.desc("[[[L" + deque.name() + ";").first();
        }
    }

    public class Widgets extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement widget = Main.get(WidgetIdentifier.class).identified();
            return new FieldQuery().notMember().desc("[[L" + widget.name() + ";").first();
        }
    }

    public class CollisionMap extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            ClassElement collisionMap = Main.get(CollisionMapIdentifier.class).identified();
            return new FieldQuery().notMember().desc("[L" + collisionMap.name() + ";").first();
        }
    }

    public class WidgetNodeBag extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            final ClassElement widgetNode = Main.get(WidgetNodeIdentifier.class).identified();
            final ClassElement bag = Main.get(BagIdentifier.class).identified();
            final Value<FieldElement> value = new Value<>();
            for (MethodElement element : new MethodQuery().hasCast(widgetNode.name()).
                    hasFieldDesc("L" + bag.name() + ";", false).returns("V")) {
                MethodVisitor visitor = new MethodVisitor(element) {
                    @Override
                    public void visitCast(CastNode node) {
                        if (value.set()) {
                            return;
                        }
                        if (node.desc().equals(widgetNode.name())) {
                            AbstractNode prev = node;
                            while ((prev = prev.prev(AbstractNode.STATIC_FIELD_CALL_NODE)) != null) {
                                StaticFieldCallNode call = (StaticFieldCallNode) prev;
                                if (call.desc().equals("L" + bag.name() + ";")) {
                                    value.set(call.field());
                                    break;
                                }
                            }
                        }
                    }
                };
                if (value.set()) {
                    return value.value();
                }
            }
            return null;
        }
    }


    public class Region extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            FieldQuery query = new FieldQuery();
            ClassElement region = Main.get(RegionIdentifier.class).identified();
            return query.desc("L" + region.name() + ";").notMember().first();
        }
    }

    public class LocalPlayer extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            FieldQuery query = new FieldQuery();
            ClassElement player = Main.get(PlayerIdentifier.class).identified();
            return query.desc("L" + player.name() + ";").notMember().first();
        }
    }

    public class Players extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            FieldQuery query = new FieldQuery();
            ClassElement player = Main.get(PlayerIdentifier.class).identified();
            return query.desc("[L" + player.name() + ";").notMember().first();
        }
    }

    public class Npcs extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            FieldQuery query = new FieldQuery();
            ClassElement npc = Main.get(NpcIdentifier.class).identified();
            return query.desc("[L" + npc.name() + ";").notMember().first();
        }
    }

    public class Canvas extends AbstractFieldIdentifier {
        @Override
        public FieldElement identify() {
            FieldQuery query = new FieldQuery();
            return query.desc("Ljava/awt/Canvas;").notMember().first();
        }
    }

    public class ItemDefinitionCache extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            MethodQuery methodQuery = new MethodQuery();
            ClassElement itemDef = Main.get(ItemDefinitionIdentifier.class).identified();
            ClassElement cache = Main.get(CacheIdentifier.class).identified();
            FieldQuery query = new FieldQuery(methodQuery.returns("L" + itemDef.name() + ";").all()).desc("L" + cache.name() + ";");
            return query.first();
        }
    }

    public class ObjectDefinitionCache extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            MethodQuery methodQuery = new MethodQuery();
            ClassElement objectDef = Main.get(ObjectDefinitionIdentifier.class).identified();
            ClassElement cache = Main.get(CacheIdentifier.class).identified();
            FieldQuery query = new FieldQuery(methodQuery.returns("L" + objectDef.name() + ";").all()).desc("L" + cache.name() + ";");
            return query.first();
        }
    }

    public class NpcDefinitionCache extends AbstractFieldIdentifier {

        @Override
        public FieldElement identify() {
            MethodQuery methodQuery = new MethodQuery();
            ClassElement npcDef = Main.get(NpcDefinitionIdentifier.class).identified();
            ClassElement cache = Main.get(CacheIdentifier.class).identified();
            FieldQuery query = new FieldQuery(methodQuery.returns("L" + npcDef.name() + ";").all()).desc("L" + cache.name() + ";");
            return query.first();
        }
    }
}
