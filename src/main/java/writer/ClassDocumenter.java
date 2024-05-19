package writer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassDocumenter {

    private static final Set<Class<?>> processedClasses = new HashSet<>();

    public static void document(Class<?> clazz, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<html><head><title>Object Documenter</title></head><body>");
            documentClass(clazz, writer, "");
            writer.write("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void documentClass(Class<?> clazz, FileWriter writer, String indent) throws IOException {
        if (!processedClasses.contains(clazz)) {
            processedClasses.add(clazz);
            writer.write(indent + "<h1>Class: " + clazz.getName() + "</h1>");
            writer.write(indent + "<h2>Fields:</h2>");
            documentFields(clazz, writer, indent);
            writer.write(indent + "<h2>Constructors: </h2>");
            documentConstructors(clazz, writer, indent);
            writer.write(indent + "<h2>Methods:</h2>");
            documentMethods(clazz, writer, indent);
            writer.write(indent + "<h2>Referenced Classes:</h2>");
            documentReferencedClasses(clazz, writer, indent);
        }
    }

    private static void documentFields(Class<?> clazz, FileWriter writer, String indent) throws IOException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            var msg = MessageFormat.format("<p>{0} {1} : <b>{2}</b></p>", 
                    getVisibilitySign(field.getModifiers()), field.getName(), 
                    field.getType().getSimpleName());
            writer.write(indent + msg);
        }
    }

    private static void documentMethods(Class<?> clazz, FileWriter writer, String indent) throws IOException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            var paramTypes = getAggregatedTypes(method.getParameterTypes());
            var msg = MessageFormat.format("<p>{0} {1}(<i>{2}</i>) : <b>{3}</b></p>",
                    getVisibilitySign(method.getModifiers()), method.getName(), 
                    paramTypes.toString(), method.getReturnType().getSimpleName());
            writer.write(indent + msg);
        }
    }

    private static void documentConstructors(Class<?> clazz, FileWriter writer, String indent) throws IOException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            var inputTypes = getAggregatedTypes(constructor.getParameterTypes());
            var msg = MessageFormat.format("<p>{0} {1}(<i>{2}</i>)</p>",
                    getVisibilitySign(constructor.getModifiers()), 
                    constructor.getDeclaringClass().getSimpleName(), inputTypes.toString());
            writer.write(indent + msg);
        }
    }
    
    private static void documentReferencedClasses(Class<?> clazz, FileWriter writer, String indent) 
            throws IOException {
        Field[] fields = clazz.getDeclaredFields();
        documentBriefReferenced(writer, indent, fields);
        for (Field field : fields) {
            var fieldType = field.getType();
            if (fieldType.isPrimitive() || 
                    fieldType.getName().startsWith("java.lang")) continue;
            if (fieldType.isArray())
                documentClass(fieldType.getComponentType(), writer, indent);
            else
                documentClass(fieldType, writer, indent);
        }
    }

    private static void documentBriefReferenced(FileWriter writer, String indent, Field[] fields) {
        Arrays.stream(fields).toList()
                .stream().filter(field -> !field.getType().isPrimitive() &&
                        !field.getType().getName().startsWith("java.lang"))
                .forEach(field -> {
                    try {
                        var msg = MessageFormat.format("<p>{0}</p>",
                                field.getType().getSimpleName());
                        writer.write(indent + msg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    
    private static String getVisibilitySign(int modifiers) {
        if (Modifier.isPublic(modifiers)) return "+";
        if (Modifier.isProtected(modifiers)) return "#";
        if (Modifier.isPrivate(modifiers)) return "-";
        return "";
    }
    
    private static StringBuilder getAggregatedTypes(Class<?>[] types) {
        var paramTypes = new StringBuilder();
        for (Class<?> param : types) {
            paramTypes.append(param.getSimpleName()).append(", ");
        }
        if (!paramTypes.isEmpty()) {
            var indexToDelete = paramTypes.lastIndexOf(",");
            paramTypes.delete(indexToDelete, indexToDelete + 2);
        }
        return paramTypes;
    }
}

