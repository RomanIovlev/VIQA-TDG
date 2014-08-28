using System;
using System.Collections.Generic;
using System.Linq;

namespace Testing.ValuesGenerator
{
    public class VIDataGenerator<T>
    {
        protected Func<T> DefaultTestData;
        protected Type DataType;
        protected FilterData FilterData;
        public string[] WhiteList { set { FilterData.WhiteList = value; } }
        public string[] BlackList { set { FilterData.BlackList = value; } }

        private List<FieldGroup> GetGroups()
        {
            var fieldGroups = DataType.GetFields()
                .Select(field => ValuesGroup.GetGroupsFromAttributes(field, new FilterData(FilterData, field.Name)))
                .Where(fieldGroup => fieldGroup != null).ToList();
            var propGroups = DataType.GetProperties()
                .Select(prop => ValuesGroup.GetGroupsFromAttributes(prop, new FilterData(FilterData, prop.Name)))
                .Where(fieldGroup => fieldGroup != null).ToList();
            fieldGroups.AddRange(propGroups);
            return fieldGroups;
        }

        private List<T> ProcessFieldGroups()
        {
            var fieldGroups = GetGroups();
            return fieldGroups.SelectMany(fieldGroup => fieldGroup.Values.Select(value =>
            {
                var testInstance = DefaultTestData();
                if (fieldGroup.FieldType == FieldType.Field)
                    DataType.GetField(fieldGroup.FieldName).SetValue(testInstance, value);
                else
                    DataType.GetProperty(fieldGroup.FieldName).SetValue(testInstance, value);
                return testInstance;
            })).ToList();
        }
        
        private List<T> GetFieldsCombinations()
        {
            var fieldGroups = DataType.GetFields()
                .Select(field => ValuesGroup.GetFieldGroupsCombinationsFromAttributes(field, new FilterData(FilterData, field.Name))).ToList();
            var propGroups = DataType.GetProperties()
                .Select(prop => ValuesGroup.GetPropGroupsCombinationsFromAttributes(prop, new FilterData(FilterData, prop.Name))).ToList();
            fieldGroups.AddRange(propGroups);
            fieldGroups = fieldGroups.Where(fieldGroup => fieldGroup != null).ToList();
            return GetCombinations(fieldGroups).Select(projectFields=> 
            {
                var testInstance = DefaultTestData();
                foreach (var projectField in projectFields)
                    if (projectField.FieldType == FieldType.Field)
                        DataType.GetField(projectField.FieldName).SetValue(testInstance, projectField.Values.First());
                    else
                        DataType.GetProperty(projectField.FieldName).SetValue(testInstance, projectField.Values.First());
                return testInstance;
            }).ToList();
        }

        private List<List<FieldGroup>> GetCombinations(List<FieldGroup> fieldGroups)
        {
            List<List<FieldGroup>> combinations;
            if (!fieldGroups.Any())
                return new List<List<FieldGroup>>();
            var firstGroup = fieldGroups.First();
            switch (fieldGroups.Count)
            {
                case 1:
                    return firstGroup.SplitValues();
                case 2: 
                    var restGroup = fieldGroups.Except(new[] {firstGroup}).First();
                    combinations = restGroup.SplitValues();
                    break;
                default:
                    combinations = GetCombinations(fieldGroups.Except(new[] {firstGroup}).ToList());
                    break;
            }
            return firstGroup.AddCombinations(combinations);
        }

        public List<T> GenerateValues(params string[] availableGroups)
        {
            FilterData.AvailableGroups = FilterData.AvailableGroups ?? availableGroups;
            return Generator(ProcessFieldGroups);
        }

        public List<T> GenerateCombinations(params string[] availableGroups)
        {
            FilterData.AvailableGroups = FilterData.AvailableGroups ?? availableGroups;
            return Generator(GetFieldsCombinations);
        }

        private List<T> Generator(Func<List<T>> generator)
        {
            try
            {
                if (FilterData.AvailableGroups == null || !FilterData.AvailableGroups.Any())
                    return new List<T> { Activator.CreateInstance<T>() };
                return generator();
            }
            catch (Exception ex) { throw new Exception(string.Format("Error in DataGeneration: " + ex.Message)); }
            
        }

        public VIDataGenerator()
        {
            DefaultTestData = Activator.CreateInstance<T>;
            DataType = DefaultTestData().GetType();
            FilterData = new FilterData();
        }

        public VIDataGenerator(Func<T> defaultTestData) : this()
        {
            DefaultTestData = defaultTestData;
        }
    }

    public class VIDataGenerator : VIDataGenerator<Object>
    {
        public VIDataGenerator(Type classType, FilterData filterData)
        {
            DefaultTestData = () => Activator.CreateInstance(classType); 
            DataType = classType;
            FilterData = filterData;
        }
    }
}