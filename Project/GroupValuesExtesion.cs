using System;
using System.Collections.Generic;
using System.Linq;
using Testing.ValuesGenerator.Attributes;

namespace Testing.ValuesGenerator
{
    public static class GroupValuesExtesion
    {
        public static string PrintGroupValues<T>(this T obj, bool inherit = true)
        {
            var fields = obj.GetType().GetFields().Where(_ =>
                (_.GetCustomAttributes(typeof (VIDataGroupsAttribute), false).Any())
                || (_.GetCustomAttributes(typeof (VIDataValuesAttribute), false).Any())
                || (inherit && _.GetCustomAttributes(typeof (VIComplexDataAttribute), false).Any()))
                .Select(field => new { field.Name, Value = field.GetValue(obj),
                    isComplex = field.GetCustomAttributes(typeof (VIComplexDataAttribute), false).Any()
                }).ToList();
            var props = obj.GetType().GetProperties().Where(_ =>
                (_.GetCustomAttributes(typeof(VIDataGroupsAttribute), false).Any())
                || (_.GetCustomAttributes(typeof(VIDataValuesAttribute), false).Any())
                || (inherit && _.GetCustomAttributes(typeof(VIComplexDataAttribute), false).Any()))
                .Select(prop => new { prop.Name, Value = prop.GetValue(obj),
                    isComplex = prop.GetCustomAttributes(typeof(VIComplexDataAttribute), false).Any()
                }).ToList();
            fields.AddRange(props);
            var resultFields = fields.Select(field => field.isComplex 
                ? string.Format("{0} ({1})", field.Name, field.Value)
                : string.Format("{0}: {1}", field.Name, field.Value)).ToList();
            return string.Join("; ", resultFields);
        }

        public static List<TestData> ApplyFilters<TestData>(this List<TestData> testDatas, params Func<TestData, bool>[] filters)
        {
            return filters.Aggregate(testDatas, ((current, filter) => current.Where((filter.Invoke)).ToList()));
        }
        
    }
}