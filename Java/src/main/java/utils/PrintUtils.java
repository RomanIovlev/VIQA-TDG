package utils;

import annotations.VIComplexData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.String.format;

/**
 * Created by roman.i on 30.09.2014.
 */
public class PrintUtils {
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
