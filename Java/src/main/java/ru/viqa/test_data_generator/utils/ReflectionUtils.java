package ru.viqa.test_data_generator.utils;

import java.lang.reflect.Field;

/**
 * Created by roman.i on 25.09.2014.
 */
public class ReflectionUtils {
    public static boolean isClass(Field field, Class<?> expected) throws Exception {
        return isClass(field.getType(), expected);
    }
    public static boolean isClass(Class<?> type, Class<?> expected) throws Exception {
        while (type != null && type != Object.class)
            if (type == expected) return true; else type = type.getSuperclass();
        return false;
    }
}
