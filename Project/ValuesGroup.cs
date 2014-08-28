using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Testing.ValuesGenerator.Attributes;

namespace Testing.ValuesGenerator
{
    public class ValuesGroup
    {
        private readonly string _groupName;
        private readonly Object[] _values;

        public ValuesGroup(string groupName, Object[] values)
        {
            _groupName = groupName;
            _values = values;
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

        public static FieldGroup GetGroupsFromAttributes(PropertyInfo prop, FilterData filterData)
        {
            return GetPropertyGroups(prop, filterData, () => new VIDataGenerator(prop.PropertyType, filterData).GenerateValues());
        }

        public static FieldGroup GetPropGroupsCombinationsFromAttributes(PropertyInfo prop, FilterData filterData)
        {
            return GetPropertyGroups(prop, filterData, () => new VIDataGenerator(prop.PropertyType, filterData).GenerateCombinations());
        }

        private static FieldGroup GetPropertyGroups(PropertyInfo property, FilterData filterData, Func<List<object>> recursive)
        {
            if (property.GetCustomAttribute<VIComplexDataAttribute>() != null)
                return new FieldGroup(property.Name, FieldType.Property, recursive().ToArray());
            if (!filterData.AllowGeneration())
                return null;
            var valGroups = new List<ValuesGroup>();
            valGroups.AddRange(property.GetCustomAttributes(typeof(VIDataGroupsAttribute), false).Select(attr => ((VIDataGroupsAttribute)attr).DataGroups));
            valGroups.AddRange(property.GetCustomAttributes(typeof(VIDataValuesAttribute), false).Select(attr => ((VIDataValuesAttribute)attr).Values));
            if (!filterData.AvailableGroups.Contains("All"))
                valGroups = valGroups.Where(_ => filterData.AvailableGroups.Contains(_._groupName)).ToList();
            return (valGroups.Any())
                ? new FieldGroup(property.Name, FieldType.Property, valGroups.SelectMany(_ => _._values).ToArray())
                : null;
        }
    }
}