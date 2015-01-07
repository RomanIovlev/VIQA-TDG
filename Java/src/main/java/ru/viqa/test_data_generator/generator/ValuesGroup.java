package ru.viqa.test_data_generator.generator;

import ru.viqa.test_data_generator.annotations.VIAllData;
import ru.viqa.test_data_generator.annotations.VIComplexData;
import ru.viqa.test_data_generator.funcInterfaces.FuncT;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.reflect.Array.newInstance;
import static java.util.Arrays.copyOf;
import static ru.viqa.test_data_generator.utils.ReflectionUtils.isClass;
import static org.apache.commons.lang3.ArrayUtils.toObject;

/**
 * Created by 12345 on 28.12.2014.
 */
public class ValuesGroup {
    private String groupName;
    private Object[] values;
    public static  final String ALL_GROUPS = "ALL";
    public static  final String DEFAULT_GROUP = "DEFAULT";

    public ValuesGroup(String groupName, Object[] values)
    {
        this.groupName = groupName;
        this.values = values;
    }

    public static FieldGroup getGroupsFromAnnotations(Field field, FilterData filterData) throws Exception {
        return getFieldGroups(field, filterData, () -> new VIDataGenerator(field.getType(), filterData).generateValues());
    }

    public static FieldGroup getFieldGroupsCombinationsFromAnnotations(Field field, FilterData filterData) throws Exception {
        return getFieldGroups(field, filterData, () -> new VIDataGenerator(field.getType(), filterData).generateCombinations());
    }

    private static FieldGroup getFieldGroups(Field field, FilterData filterData, FuncT<List<Object>> recursive) throws Exception {
        if (field.getAnnotation(VIComplexData.class) != null)
            return new FieldGroup(field.getName(), recursive.invoke().toArray());
        if (!filterData.allowGeneration())
            return null;

        List<ValuesGroup> valGroups = getGroupValuesFromAnnotations(field);

        List<String> availableGroupsLowerCase = new ArrayList<>();
        for (String str : filterData.availableGroups)
            availableGroupsLowerCase.add(str.toLowerCase());
        if (!availableGroupsLowerCase.contains(ALL_GROUPS.toLowerCase())) {
            List<ValuesGroup> suitableGroups = new ArrayList<>();
            for (ValuesGroup valGroup : valGroups)
                if (availableGroupsLowerCase.contains(valGroup.groupName.toLowerCase()))
                    suitableGroups.add(valGroup);
            valGroups = suitableGroups;
        }
        if (valGroups.size() > 0) {
            List<Object> fieldValues = new ArrayList<>();
            for (ValuesGroup valGroup : valGroups)
                if (valGroup.values != null)
                    for(Object value : valGroup.values)
                        if (!fieldValues.contains(value))
                            fieldValues.add(value);
            return new FieldGroup(field.getName(), fieldValues.toArray());
        }
        return null;
    }

    public static List<ValuesGroup> getGroupValuesFromAnnotations(Field field) throws Exception {
        List<ValuesGroup> result = new ArrayList<>();
        for (Annotation annotation : field.getAnnotations()) {
            ValuesGroup valGroup = null;
            if (annotation.annotationType() == VIAllData.class)
                valGroup = new ValuesGroup(ALL_GROUPS, getValuesFromType(field.getType()));
            else
                try {
                    Object valuesObj = annotation.getClass().getMethod("value").invoke(annotation);
                    if (!valuesObj.getClass().isArray()) continue;
                    Object[] values = null;
                    if (isClass(valuesObj.getClass(), int[].class))
                        values = copyOf(toObject((int[]) valuesObj), ((int[]) valuesObj).length, Integer[].class);
                        else { if (isClass(valuesObj.getClass(), boolean[].class))
                            values = copyOf(toObject((boolean[]) valuesObj), ((boolean[]) valuesObj).length, Boolean[].class);
                            else try {
                                values = (Object[]) valuesObj;
                            } catch (Exception ignored) {}
                        }
                    if (values != null && values.length > 0) {
                        String groupName = ALL_GROUPS;
                        try {
                            groupName = (String) annotation.getClass().getMethod("group").invoke(annotation);
                        } catch (Exception ignored) {}
                        if (field.getType().isArray()) {
                            Object[] newValues = new Object[values.length + 1];
                            for (int i = 0; i <= values.length; i++) {
                                Object[] array = (Object[]) newInstance(field.getType().getComponentType(), i);
                                for (int j = 0; j < i; j++)
                                    array[j] = values[j];
                                newValues[i] = array;
                            }
                            values = newValues;
                        }
                        valGroup = new ValuesGroup(groupName, values);
                    }
                } catch (Exception ignore) {}
            if (valGroup != null)
                result.add(valGroup);
        }
        return result;
    }

    private static Object[] getValuesFromType(Class<?> type) throws Exception {
        if (isClass(type, Enum.class))
            return type.getEnumConstants();
        if (isClass(type, boolean.class))
            return new Object[] {true, false};
        return null;
    }
}
