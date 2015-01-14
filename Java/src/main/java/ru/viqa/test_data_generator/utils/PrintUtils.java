package ru.viqa.test_data_generator.utils;

import ru.viqa.test_data_generator.annotations.VIComplexData;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;
import static java.lang.System.nanoTime;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.String.format;
import static ru.viqa.test_data_generator.utils.StringUtils.LineBreak;

/**
 * Created by roman.i on 30.09.2014.
 */
public class PrintUtils {
    public static String printGroupValues(Object obj) throws Exception {
        List<String> resultFields = new ArrayList<>();
        for (Field field : obj.getClass().getFields())
            if (!isStatic(field.getModifiers()))
                resultFields.add(printField(field, obj));
        return join("; ", resultFields);
    }

    private static <T> String printField(Field field, T obj) throws Exception {
        if (field.getAnnotation(VIComplexData.class) != null)
            return format("%s(%s)", field.getName(), field.get(obj).toString());
        return format("%s: %s", field.getName(),
                (field.getType().isArray())
                        ? printArray(", ", (Object[]) field.get(obj))
                        : field.get(obj));
    }

    private static String printArray(String delimiter, Object[] array) {
        if (array == null || array.length == 0)
            return "";
        String result = "";
        for(int i = 0; i < array.length - 1; i++)
            result += array[i] + delimiter;
        return result + array[array.length - 1];
    }

    public static <T> String printTestData(List<T> list) throws Exception {
        String result = getFieldsNames(list.get(0).getClass()) + LineBreak;
        for (T element : list)
            result += getFieldsValues(element) + LineBreak;
        return result;
    }

    public static <T> void printTestData(List<T> list, String fileName, boolean unique) throws Exception {
        String result = printTestData(list);
        File file = new File(format("testDataPrint\\%s%s.csv", fileName, unique ? "_" + nanoTime() : ""));
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(file, true);
        fw.write(result);
        fw.close();
    }

    private static String getFieldsNames(Class<?> type) throws Exception {
        return getFieldsNames("", type);
    }

    private static String getFieldsNames(String prefix, Class<?> type) throws Exception {
        String result = "";
        for (Field field : type.getFields())
            if (!isStatic(field.getModifiers()))
                result += ((field.getAnnotation(VIComplexData.class) != null)
                    ? getFieldsNames(prefix + field.getName() + ".", field.getType())
                        : prefix + field.getName())
                        + ";";
        return result.substring(0, result.length() - 1);
    }

    private static String getFieldsValues(Object obj) throws Exception {
        String result = "";
        for (Field field : obj.getClass().getFields())
            if (!isStatic(field.getModifiers()))
                result += ((field.getAnnotation(VIComplexData.class) != null)
                        ? getFieldsValues(field.get(obj))
                        : (field.getType().isArray())
                        ? printArray(",", (Object[]) field.get(obj))
                        : printFieldValue(field.get(obj)))
                        + ";";
        return result.substring(0, result.length()-1);
    }

    private static String printFieldValue(Object fieldValue) throws IllegalAccessException {
        if (fieldValue != null)
            return fieldValue.toString();
        else return "null";
    }
}
