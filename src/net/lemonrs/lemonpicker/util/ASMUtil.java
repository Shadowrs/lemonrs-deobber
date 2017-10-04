package net.lemonrs.lemonpicker.util;

import net.lemonrs.lemonpicker.bytecode.element.ClassElement;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @author : const_
 */
public class ASMUtil {
    public static final Map<Integer, String> OPCODE_MAP = new HashMap<>();

    static {
        for (Field f : Opcodes.class.getFields()) {
            f.setAccessible(true);
            try {
                if (f.getName().contains("TOP") ||
                        f.getName().contains("INTEGER") ||
                        f.getName().contains("FLOAT") ||
                        f.getName().contains("DOUBLE") ||
                        f.getName().contains("LONG") ||
                        f.getName().contains("NULL") ||
                        f.getName().contains("THIS")) {
                    OPCODE_MAP.put((Integer) f.get(null), f.getName());
                    continue;
                }
                OPCODE_MAP.put(f.getInt(null), f.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDescFor(String desc) {
        char[] charArray = desc.toCharArray();
        int count = 0;
        for (char c : charArray) {
            if (c == ']') {
                count++;
            }
        }
        desc = desc.replaceAll("\\[\\]", "");
        String arrays = "";
        for (int i = 0; i < count; i++) {
            arrays += "[";
        }
        switch (desc) {
            case "float":
                return arrays + "F";
            case "int":
                return arrays + "I";
            case "boolean":
                return arrays + "Z";
            case "byte":
                return arrays + "B";
            case "short":
                return arrays + "S";
            case "long":
                return arrays + "J";
            case "double":
                return arrays + "D";
            case "void":
                return "V";
            case "char":
                return arrays + "C";
            case "String":
                return arrays + "Ljava/lang/String;";
            case "Object:":
                return arrays + "Ljava/lang/Object;";
        }
        //      if (Updater.getWrapper(desc) != null) {
        //        return arrays + "L" + desc + ";";
        //   }
        return null;
    }

    public static String stripDesc(String desc) {
        return desc.replaceAll("L", "").replaceAll(";", "").replaceAll("\\[", "");
    }

    public static void save(File jar, final List<ClassElement> nodes) {
        try {
            try (final JarOutputStream output = new JarOutputStream(new FileOutputStream(jar))) {
                for (ClassElement element : nodes) {
                    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    output.putNextEntry(new JarEntry(element.name().replaceAll("\\.", "/") + ".class"));
                    element.node().accept(writer);
                    output.write(writer.toByteArray());
                    output.closeEntry();
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ClassElement> load(File file) {
        try {
            JarFile jar = new JarFile(file);
            List<ClassElement> list = new ArrayList<>();
            Enumeration<JarEntry> enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry next = enumeration.nextElement();
                if (next.getName().endsWith(".class")) {
                    ClassReader reader = new ClassReader(jar.getInputStream(next));
                    ClassNode node = new ClassNode();
                    reader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    ClassElement element = new ClassElement(node, reader);
                    list.add(element);
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int count(final String string, final String substring) {
        int count = 0;
        int idx = 0;
        while ((idx = string.indexOf(substring, idx)) != -1) {
            idx++;
            count++;
        }
        return count;
    }

    public static boolean isStandard(String desc) {
        return desc.contains("java") || desc.contains("[") &&
                desc.substring(desc.lastIndexOf('[') + 2).length() == 0 ||
                desc.length() == 1 && Character.isUpperCase(desc.charAt(0));
    }
}
