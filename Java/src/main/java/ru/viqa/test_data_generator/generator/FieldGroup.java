package ru.viqa.test_data_generator.generator;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by 12345 on 16.12.2014.
 */
public class FieldGroup {
    public String fieldName;
    public List<Object> values;

    public FieldGroup(String fieldName, Object... values)
    {
        this.fieldName = fieldName;
        this.values = asList(values);
    }

    public List<List<FieldGroup>> splitValues()
    {
        List<List<FieldGroup>> result = new ArrayList<>();
        for (Object value : values) {
            List<FieldGroup> el = new ArrayList<>();
            el.add(new FieldGroup(fieldName, value));
            result.add(el);
        }
        return result;
    }

    public List<List<FieldGroup>> addCombinations(List<List<FieldGroup>> combinations)
    {
        List<List<FieldGroup>> result = new ArrayList<>();
        for (Object value : values)
            for (List<FieldGroup> listValues : combinations) {
                List<FieldGroup> list = new ArrayList<>();
                list.add(new FieldGroup(fieldName, value));
                list.addAll(listValues);
                result.add(list);
            }
        return result;
    }
}

