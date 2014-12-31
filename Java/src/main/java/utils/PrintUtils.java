package utils;

import annotations.VIComplexData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.join;
import static java.lang.reflect.Modifier.isStatic;
import static utils.ReflectionUtils.getField;
import static utils.ReflectionUtils.getFields;
import static utils.LinqUtils.*;
import static java.lang.String.format;

/**
 * Created by roman.i on 30.09.2014.
 */
public class PrintUtils {
    public static String print(Iterable<String> list) throws Exception { return print(list, ", ", "%s"); }
    public static String print(Iterable<String> list, String separator) throws Exception { return print(list, separator, "%s"); }
    public static String print(Iterable<String> list, String separator, String format) throws Exception {
        return (list != null) ? join(separator, select(list, el -> format(format, el))) : "";
    }
    public static String print(String[] list) throws Exception { return print(list, ", ", "%s"); }
    public static String print(String[] list, String separator) throws Exception { return print(list, separator, "%s"); }
    public static String print(String[] list, String separator, String format) throws Exception {
        return print(Arrays.asList(list), separator, format);
    }
    public static String printFields(Object obj) throws Exception { return printFields(obj, "; "); }
    public static String printFields(Object obj, String separator) throws Exception {
        String className = obj.getClass().getSimpleName();
        String params = print(select(getFields(obj, String.class),
            field -> field.getName() + ": '" + getField(field, obj) + "'"), separator, "%s");
        return format("%s(%s)", className, params);
    }

    public static String printGroupValues(Object obj) throws Exception {
        List<String> resultFields = new ArrayList<>();
        for (Field field : obj.getClass().getFields())
            if (!isStatic(field.getModifiers()))
                resultFields.add(field.getAnnotation(VIComplexData.class) != null
                    ? format("%s (%s)", field.getName(), field.get(obj).toString())
                    : format("%s: %s", field.getName(), field.get(obj)));
        return join("; ", resultFields);
    }

}
