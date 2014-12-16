package Data;

import Annotations.VIDataValues;

/**
 * Created by 12345 on 16.12.2014.
 */
public class DataValues {
    @VIDataValues(enumValues = {MyEnum.One, MyEnum.Two}, classVal = MyEnum.class)
    private String groupName;
    private Object[] values;

    public DataValues(String groupName, Object[] values)     {
        this.groupName = groupName;
        this.values = values;
    }


    public static FieldGroup GetGroupsFromAttributes(FieldInfo field, FilterData filterData)
    {
        return GetFieldGroups(field, filterData, () => new VIDataGenerator(field.FieldType, filterData).GenerateValues());
    }

    public static FieldGroup GetFieldGroupsCombinationsFromAttributes(FieldInfo field, FilterData filterData)
    {
        return GetFieldGroups(field, filterData, () => new VIDataGenerator(field.FieldType, filterData).GenerateCombinations());
    }

    private static FieldGroup GetFieldGroups(FieldInfo field, FilterData filterData, Func<List<object>> recursive)
    {
        if (field.GetCustomAttribute<VIComplexDataAttribute>() != null)
            return new FieldGroup(field.Name, FieldType.Field, recursive().ToArray());
        if (!filterData.AllowGeneration())
            return null;
        var valGroups = new List<ValuesGroup>();
        valGroups.AddRange(field.GetCustomAttributes(typeof(VIDataGroupsAttribute), false).Select(attr => ((VIDataGroupsAttribute)attr).DataGroups));
        valGroups.AddRange(field.GetCustomAttributes(typeof(VIDataValuesAttribute), false).Select(attr => ((VIDataValuesAttribute)attr).Values));
        if (!filterData.AvailableGroups.Contains("All"))
            valGroups = valGroups.Where(_ => filterData.AvailableGroups.Contains(_._groupName)).ToList();
        return (valGroups.Any())
                ? new FieldGroup(field.Name, FieldType.Field, valGroups.SelectMany(_ => _._values).ToArray())
        : null;
    }
}
