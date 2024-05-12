package org.example;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ObjectDocumenter {

    private static Set<Class<?>> processedClasses = new HashSet<>();

    public static void document(Object obj, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<html><head><title>Object Documenter</title></head><body>");
            documentObject(obj, writer, "");
            writer.write("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void documentObject(Object obj, FileWriter writer, String indent) throws IOException {
        Class<?> clazz = obj.getClass();
        if (!processedClasses.contains(clazz)) {
            processedClasses.add(clazz);
            writer.write(indent + "<h1>Class: " + clazz.getName() + "</h1>");
            writer.write(indent + "<h2>Fields:</h2>");
            documentFields(obj, writer, indent);
            writer.write(indent + "<h2>Methods:</h2>");
            documentMethods(obj, writer, indent);
            writer.write(indent + "<h2>Referenced Classes:</h2>");
            documentReferencedClasses(obj, writer, indent);
        }
    }

    private static void documentFields(Object obj, FileWriter writer, String indent) throws IOException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            var msg = MessageFormat.format("<p><strong>Field:</strong> {0} <strong>Type:</strong> {1} </p>", 
                    field.getName(), field.getType().getSimpleName());
            writer.write(indent + msg);
        }
    }

    private static void documentMethods(Object obj, FileWriter writer, String indent) throws IOException {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            var msg = MessageFormat.format("<p><strong>Method:</strong> {0} <strong>Return type:</strong> {1} </p>",
                    method.getName(), method.getReturnType().getSimpleName());
            writer.write(indent + msg);
        }
    }

    private static void documentReferencedClasses(Object obj, FileWriter writer, String indent) throws IOException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        documentBriefReferenced(writer, indent, fields);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value != null && !field.getType().isPrimitive() && !field.getType().getName().startsWith("java.lang")) {
                    if (field.getType().isArray()) {
                        documentArray(value, writer, indent);
                    } else {
                        documentObject(value, writer, indent);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void documentBriefReferenced(FileWriter writer, String indent, Field[] fields) {
        Arrays.stream(fields).toList()
                .stream().filter(field -> !field.getType().isPrimitive() && !field.getType().getName().startsWith("java.lang"))
                .forEach(field -> {
                    try {
                        var msg = MessageFormat.format("<p><strong>Referenced Class:</strong> {0} </p>", field.getType().getSimpleName());
                        writer.write(indent + msg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static void documentArray(Object obj, FileWriter writer, String indent) throws IOException {
        for (int i = 0; i < Array.getLength(obj); i++) {
            Object arrayElement = Array.get(obj, i);
            if (arrayElement != null && !arrayElement.getClass().isPrimitive() && 
                    !arrayElement.getClass().getName().startsWith("java.lang")) {
                documentObject(arrayElement, writer, indent + "  ");
            }
        }
    }
}

