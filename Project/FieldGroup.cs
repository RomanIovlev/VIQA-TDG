using System;
using System.Collections.Generic;
using System.Linq;

namespace Testing.ValuesGenerator
{
    public class FieldGroup
    {
        public readonly string FieldName;
        public readonly FieldType FieldType;
        public readonly List<Object> Values;

        public FieldGroup(string fieldName, FieldType fieldType, params Object[] values)
        {
            FieldName = fieldName;
            FieldType = fieldType;
            Values = values.Distinct().ToList();
        }

        public List<List<FieldGroup>> SplitValues()
        {
            return Values.Select(value => new List<FieldGroup> { new FieldGroup(FieldName, FieldType, value) }).ToList();
        }

        public List<List<FieldGroup>> AddCombinations(List<List<FieldGroup>> combinations)
        {
            return Values.SelectMany(value =>
                combinations.Select(listValues =>
                {
                    var result = new List<FieldGroup> { new FieldGroup(FieldName, FieldType, value) };
                    result.AddRange(listValues);
                    return result;
                }).ToList()).ToList();
        }
    }
    public enum FieldType { Field, Property }
}