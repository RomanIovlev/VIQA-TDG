package ru.viqa.test_data_generator.generator;

import ru.viqa.test_data_generator.funcInterfaces.FuncT;
import ru.viqa.test_data_generator.funcInterfaces.FuncTT;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.addAll;
import static ru.viqa.test_data_generator.generator.ValuesGroup.*;
import static ru.viqa.test_data_generator.generator.ValuesGroup.getFieldGroupsCombinationsFromAnnotations;
import static ru.viqa.test_data_generator.generator.ValuesGroup.getGroupsFromAnnotations;
import static java.lang.reflect.Array.newInstance;
import static java.util.Arrays.asList;

/**
 * Created by 12345 on 28.12.2014.
 */
public class VIDataGenerator<T> {
    private FuncT<T> createFunc;
    private Class<?> dataType;
    private FilterData fieldsFilter;
    private FuncTT<T, Boolean>[] dataRules;
    public VIDataGenerator<T> fieldsFilter(FuncTT<String, Boolean> fieldsRule) { fieldsFilter.fieldsRule = fieldsRule; return this; }
    public VIDataGenerator<T> fieldsToGenerate(String... value) { fieldsFilter.whiteList = value; return this; }
    public VIDataGenerator<T> addFieldsToGenerate(String... values) {
        List<String> list = new ArrayList<>();
        addAll(list, fieldsFilter.whiteList);
        addAll(list, values);
        fieldsFilter.whiteList = list.toArray(new String[list.size()]);
        return this; }
    public VIDataGenerator<T> blackList(String... value) { fieldsFilter.blackList = value; return this; }
    public VIDataGenerator<T> dataFilter(FuncTT<T, Boolean>... dataRule) { this.dataRules = dataRule; return this;}

    private List<FieldGroup> getGroups() throws Exception {
        try {
            List<FieldGroup> fieldGroups = new ArrayList<>();
            for (Field field : dataType.getFields()) {
                FieldGroup fieldGroup = getGroupsFromAnnotations(field, new FilterData(fieldsFilter, field.getName()));
                if (fieldGroup != null)
                    fieldGroups.add(fieldGroup);
            }
            return fieldGroups;
        } catch (Exception ex) { throw new Exception("Error in getting Groups:" + ex.getMessage()); }
    }

    private List<T> getFieldsGroups() throws Exception {
        try {
            List<T> result = new ArrayList<>();
            for (FieldGroup fieldGroup : getGroups())
                for (Object value : fieldGroup.values) {
                    T testInstance = createFunc.invoke();
                    Object fieldValue = dataType.getField(fieldGroup.fieldName).get(testInstance);
                    if (fieldValue != null && !fieldValue.equals(value)) {
                        dataType.getField(fieldGroup.fieldName).set(testInstance, value);
                        if (dataRules == null || meetDataRules(testInstance, dataRules))
                            result.add(testInstance);
                    }
                }
            return result;
        } catch (Exception ex) { throw new Exception("Error in getting Field Groups" + ex.getMessage()); }
    }

    private List<T> getFieldsCombinations() throws Exception {
        try {
            List<FieldGroup> fieldGroups = new ArrayList<>();
            for (Field field : dataType.getFields()) {
                FieldGroup fieldGroup = getFieldGroupsCombinationsFromAnnotations(field, new FilterData(fieldsFilter, field.getName()));
                if (fieldGroup != null && fieldGroup.values.size() > 0)
                    fieldGroups.add(fieldGroup);
            }
            List<T> result = new ArrayList<>();
            for (List<FieldGroup> projectFields : getCombinations(fieldGroups)) {
                T testInstance = createFunc.invoke();
                for (FieldGroup projectField : projectFields)
                    dataType.getField(projectField.fieldName).set(testInstance, projectField.values.get(0));
                if (dataRules == null || meetDataRules(testInstance, dataRules))
                    result.add(testInstance);
            }
            return result;
        } catch (Exception ex) { throw new Exception("Error in generating fields combinations:" + ex.getMessage()); }
    }

    private boolean meetDataRules(T testInstance, FuncTT<T, Boolean>[] dataRules) throws Exception {
        for(FuncTT<T, Boolean> dataRule : dataRules)
            if (!dataRule.invoke(testInstance))
                return false;
        return true;
    }

    private List<List<FieldGroup>> getCombinations(List<FieldGroup> fieldGroups) throws Exception {
        try {
            List<List<FieldGroup>> combinations;
            if (fieldGroups.size() == 0)
                return new ArrayList<>();
            FieldGroup firstGroup = fieldGroups.get(0);
            switch (fieldGroups.size()) {
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
        } catch (Exception ex) { throw new Exception("Error in generating combinations:" + ex.getMessage()); }
    }

    public List<T> generateValues(String... availableGroups) throws Exception {
        setAvailableGroups(availableGroups);
        return generator(this::getFieldsGroups);
    }

    public List<T> generateCombinations(String... availableGroups) throws Exception {
        setAvailableGroups(availableGroups);
        return generator(this::getFieldsCombinations);
    }

    private void setAvailableGroups(String[] availableGroups) {
        if (fieldsFilter.availableGroups != null ||
            (availableGroups.length == 1 && availableGroups[0].equals(DEFAULT_GROUP)))
            return;
        fieldsFilter.availableGroups = (availableGroups.length == 0)
                ? new String[] { ALL_GROUPS }
                : availableGroups;
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
                return asList(createFunc.invoke());
            return generator.invoke();
        }
        catch (Exception ex) { throw new Exception("Error in DataGeneration: " + ex.getMessage()); }
    }

    public VIDataGenerator(FuncT<T> createFunc, String... fieldsToGenerate) throws Exception {
        this.createFunc = createFunc;
        this.dataType = createFunc.invoke().getClass();
        fieldsFilter = new FilterData();
        fieldsToGenerate(fieldsToGenerate);
    }

    public VIDataGenerator(Class<?> dataType, FilterData fieldsFilter) throws Exception {
        createFunc = () -> (T)dataType.newInstance();
        this.dataType = dataType;
        this.fieldsFilter = fieldsFilter;
    }
}
