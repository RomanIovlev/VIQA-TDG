package ru.viqa.test_data_generator.generator;

import ru.viqa.test_data_generator.annotations.VIAllData;
import ru.viqa.test_data_generator.annotations.VIComplexData;
import ru.viqa.test_data_generator.annotations.VIFieldID;
import ru.viqa.test_data_generator.funcInterfaces.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
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

        Object[] values = fillFieldIDAnnotation(field);
        if (values != null) {
            result.add(new ValuesGroup(ALL_GROUPS, values));
            return result;
        }
        if (field.getType().isArray()) {
            Annotation[] annatations = field.getAnnotations();
            if (annatations.length != 1) return result;
            values = fillArrayWithValues(field, getValuesFromAnnatation(annatations[0]), o -> o);
            if (values != null)
                result.add(new ValuesGroup(ALL_GROUPS, values));
            return result;
        }
        for (Annotation annotation : field.getAnnotations())
            result.add((annotation.annotationType() == VIAllData.class)
                ? new ValuesGroup(ALL_GROUPS, getValuesFromType(field.getType()))
                : new ValuesGroup(getGroupName(annotation), getValuesFromAnnatation(annotation))
            );
        return result;
    }

    private static String getGroupName(Annotation annotation) {
        String groupName = ALL_GROUPS;
        try {
            groupName = (String) annotation.getClass().getMethod("group").invoke(annotation);
        } catch (Exception ignored) {}
        return groupName;
    }

    private static Object[] getValuesFromAnnatation(Annotation annotation) throws Exception {
        try {
            Object valuesObj = annotation.getClass().getMethod("value").invoke(annotation);
            if (valuesObj == null)
                return null;
            if (isClass(valuesObj.getClass(), int[].class))
                return copyOf(toObject((int[]) valuesObj), ((int[]) valuesObj).length, Integer[].class);
            else {
                if (isClass(valuesObj.getClass(), boolean[].class))
                    return copyOf(toObject((boolean[]) valuesObj), ((boolean[]) valuesObj).length, Boolean[].class);
                else try {
                    return (Object[]) valuesObj;
                } catch (Exception ignored) { }
            }
        } catch (Exception ignore) {}
        return null;
    }

    private static Object[] fillFieldIDAnnotation(Field field) throws Exception {
        VIFieldID fieldIDAnnotation = field.getAnnotation(VIFieldID.class);
        if (fieldIDAnnotation == null || fieldIDAnnotation.id().equals(""))
            return null;
        String fieldId = fieldIDAnnotation.id();
        Object[] annatationValues = getValuesFromAnnatation(field);
        if (annatationValues == null || annatationValues.length == 0)
            return null;
        return fillArrayWithValues(field, annatationValues, value -> {
            Object newValue;
            try {
                Class<?> newValueType = field.getType().getComponentType();
                newValue = newValueType.newInstance();
                Field arrayField = newValueType.getField(fieldId);
                arrayField.set(newValue, value);
            } catch (Exception ex) {
                throw new Exception("Wrong FieldID: " + fieldId + "for field" + field.getName());
            }
            return newValue;
        });
    }

    private static Object[] fillArrayWithValues(Field field, Object[] values, FuncTT<Object, Object> fillRule) throws Exception {
        if (values == null || values.length ==0)
            return null;
        Class<?> type = field.getType().getComponentType();
        Object[] result = new Object[values.length + 1];
        for (int i = 0; i <= values.length; i++) {
            Object[] array = (Object[]) newInstance(type, i);
            for (int j = 0; j < i; j++)
                array[j] = fillRule.invoke(values[j]);
            result[i] = array;
        }
        return result;
    }

    private static Object[] getValuesFromAnnatation(Field field) throws Exception {
        Object[] result;
        for (Annotation annotation : field.getAnnotations()) {
            result = getValuesFromAnnatation(annotation);
            if (result != null)
                return result;
        }
        return null;
    }


    private static Object[] getValuesFromType(Class<?> type) throws Exception {
        if (isClass(type, Enum.class))
            return type.getEnumConstants();
        if (isClass(type, boolean.class))
            return new Object[] {true, false};
        return null;
    }
}
