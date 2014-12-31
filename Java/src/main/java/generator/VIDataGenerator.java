package generator;

import funcInterfaces.FuncT;
import funcInterfaces.FuncTT;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static generator.ValuesGroup.getFieldGroupsCombinationsFromAttributes;
import static generator.ValuesGroup.getGroupsFromAttributes;
import static java.util.Arrays.asList;

/**
 * Created by 12345 on 28.12.2014.
 */
public class VIDataGenerator<T> {
    protected Class<?> dataType;
    protected FilterData filterData;
    public VIDataGenerator<T> setGenRule(FuncTT<String, Boolean> rule) { filterData.generationRule = rule; return this; }
    public VIDataGenerator<T> setWhiteList(String... value) { filterData.whiteList = value; return this; }
    public VIDataGenerator<T> setBlackList(String... value) { filterData.blackList = value; return this; }

    private List<FieldGroup> getGroups() throws Exception {
        List<FieldGroup> fieldGroups = new ArrayList<>();
        for (Field field : dataType.getFields()) {
            FieldGroup fieldGroup = getGroupsFromAttributes(field, new FilterData(filterData, field.getName()));
            if (fieldGroup != null)
                fieldGroups.add(fieldGroup);
        }
        return fieldGroups;
    }

    private List<T> processFieldGroups() throws Exception {
        List<T> result = new ArrayList<>();
        for (FieldGroup fieldGroup : getGroups())
            for (Object value : fieldGroup.values) {
                T testInstance = (T) dataType.newInstance();
                dataType.getField(fieldGroup.fieldName).set(testInstance, value);
                result.add(testInstance);
            }
        return result;
    }

    private List<T> getFieldsCombinations() throws Exception {
        List<FieldGroup> fieldGroups = new ArrayList<>();
        for (Field field : dataType.getFields()) {
            FieldGroup fieldGroup = getFieldGroupsCombinationsFromAttributes(field, new FilterData(filterData, field.getName()));
            if (fieldGroup != null && fieldGroup.values.size() > 0)
                fieldGroups.add(fieldGroup);
        }
        List<T> result = new ArrayList<>();
        for (List<FieldGroup> projectFields : getCombinations(fieldGroups)) {
            T testInstance = (T) dataType.newInstance();
            for (FieldGroup projectField : projectFields)
                dataType.getField(projectField.fieldName).set(testInstance, projectField.values.get(0));
            result.add(testInstance);
        }
        return result;
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
        filterData.availableGroups = (filterData.availableGroups != null)
            ? filterData.availableGroups
            : availableGroups;
        return generator(this::processFieldGroups);
    }

    public List<T> generateCombinations(String... availableGroups) throws Exception {
        filterData.availableGroups = (filterData.availableGroups != null)
                ? filterData.availableGroups
                : availableGroups;
        return generator(this::getFieldsCombinations);
    }

    private List<T> generator(FuncT<List<T>> generator) throws Exception {
        try {
            if (filterData.availableGroups == null || filterData.availableGroups.length == 0)
                return asList((T) dataType.newInstance());
            return generator.invoke();
        }
        catch (Exception ex) { throw new Exception("Error in DataGeneration: " + ex.getMessage()); }
    }

    public VIDataGenerator(Class<T> type) throws Exception {
        this.dataType = type;
        filterData = new FilterData();
    }

    public VIDataGenerator(Class<T> type, FilterData filterData) throws Exception {
        dataType = type;
        this.filterData = filterData;
    }
}
