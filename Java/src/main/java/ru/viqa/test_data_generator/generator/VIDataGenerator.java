package ru.viqa.test_data_generator.generator;

import ru.viqa.test_data_generator.funcInterfaces.FuncT;
import ru.viqa.test_data_generator.funcInterfaces.FuncTT;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static ru.viqa.test_data_generator.generator.ValuesGroup.getFieldGroupsCombinationsFromAttributes;
import static ru.viqa.test_data_generator.generator.ValuesGroup.getGroupsFromAnnotations;
import static java.lang.reflect.Array.newInstance;
import static java.util.Arrays.asList;

/**
 * Created by 12345 on 28.12.2014.
 */
public class VIDataGenerator<T> {
    private Class<?> dataType;
    private FilterData fieldsFilter;
    private FuncTT<T, Boolean>[] dataRules;
    public VIDataGenerator<T> fieldsFilter(FuncTT<String, Boolean> fieldsRule) { fieldsFilter.fieldsRule = fieldsRule; return this; }
    public VIDataGenerator<T> whiteList(String... value) { fieldsFilter.whiteList = value; return this; }
    public VIDataGenerator<T> blackList(String... value) { fieldsFilter.blackList = value; return this; }
    public VIDataGenerator<T> dataFilter(FuncTT<T, Boolean>... dataRule) { this.dataRules = dataRule; return this;}

    private List<FieldGroup> getGroups() throws Exception {
        List<FieldGroup> fieldGroups = new ArrayList<>();
        for (Field field : dataType.getFields()) {
            FieldGroup fieldGroup = getGroupsFromAnnotations(field, new FilterData(fieldsFilter, field.getName()));
            if (fieldGroup != null)
                fieldGroups.add(fieldGroup);
        }
        return fieldGroups;
    }

    private List<T> getFieldsGroups() throws Exception {
        List<T> result = new ArrayList<>();
        for (FieldGroup fieldGroup : getGroups())
            for (Object value : fieldGroup.values) {
                T testInstance = (T) dataType.newInstance();
                if (!dataType.getField(fieldGroup.fieldName).get(testInstance).equals(value)) {
                    dataType.getField(fieldGroup.fieldName).set(testInstance, value);
                    if (dataRules == null || meetDataRules(testInstance, dataRules))
                        result.add(testInstance);
                }
            }
        return result;
    }

    private List<T> getFieldsCombinations() throws Exception {
        List<FieldGroup> fieldGroups = new ArrayList<>();
        for (Field field : dataType.getFields()) {
            FieldGroup fieldGroup = getFieldGroupsCombinationsFromAttributes(field, new FilterData(fieldsFilter, field.getName()));
            if (fieldGroup != null && fieldGroup.values.size() > 0)
                fieldGroups.add(fieldGroup);
        }
        List<T> result = new ArrayList<>();
        for (List<FieldGroup> projectFields : getCombinations(fieldGroups)) {
            T testInstance = (T) dataType.newInstance();
            for (FieldGroup projectField : projectFields)
                dataType.getField(projectField.fieldName).set(testInstance, projectField.values.get(0));
            if (dataRules == null || meetDataRules(testInstance, dataRules))
                result.add(testInstance);
        }
        return result;
    }

    private boolean meetDataRules(T testInstance, FuncTT<T, Boolean>[] dataRules) throws Exception {
        for(FuncTT<T, Boolean> dataRule : dataRules)
            if (!dataRule.invoke(testInstance))
                return false;
        return true;
    }

    private List<List<FieldGroup>> getCombinations(List<FieldGroup> fieldGroups) {
        List<List<FieldGroup>> combinations;
        if (fieldGroups.size() == 0)
            return new ArrayList<>();
        FieldGroup firstGroup = fieldGroups.get(0);
        switch (fieldGroups.size())
        {
            case 1:
                return firstGroup.splitValues();
            case 2:
                combinations = fieldGroups.get(1).splitValues();
                break;
            default:
                fieldGroups.remove(0);
                combinations = getCombinations(fieldGroups);
            break;
        }
        return firstGroup.addCombinations(combinations);
    }

    public List<T> generateValues(String... availableGroups) throws Exception {
        fieldsFilter.availableGroups = (fieldsFilter.availableGroups != null)
            ? fieldsFilter.availableGroups
            : availableGroups;
        return generator(this::getFieldsGroups);
    }

    public List<T> generateCombinations(String... availableGroups) throws Exception {
        fieldsFilter.availableGroups = (fieldsFilter.availableGroups != null)
                ? fieldsFilter.availableGroups
                : availableGroups;
        return generator(this::getFieldsCombinations);
    }

    public static <T> T[][] to2DArray(List<T> values, Class<T> dataType) {
        T[][] arrayOfArrays = (T[][]) newInstance(dataType, values.size(), 0);
        for (int i = 0; i < values.size(); i++) {
            T[] array = (T[])newInstance(dataType, 1);
            array[0] = values.get(i);
            arrayOfArrays[i] = array;
        }
        return arrayOfArrays;
    }

    private List<T> generator(FuncT<List<T>> generator) throws Exception {
        try {
            if (fieldsFilter.availableGroups == null || fieldsFilter.availableGroups.length == 0)
                return asList((T) dataType.newInstance());
            return generator.invoke();
        }
        catch (Exception ex) { throw new Exception("Error in DataGeneration: " + ex.getMessage()); }
    }

    public VIDataGenerator(Class<T> type) throws Exception {
        this.dataType = type;
        fieldsFilter = new FilterData();
    }

    public VIDataGenerator(Class<T> type, FilterData fieldsFilter) throws Exception {
        dataType = type;
        this.fieldsFilter = fieldsFilter;
    }
}
