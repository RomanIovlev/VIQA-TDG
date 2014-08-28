using System;

namespace Testing.ValuesGenerator.Attributes
{
    [AttributeUsage(AttributeTargets.Property | AttributeTargets.Field, AllowMultiple = true, Inherited = false)]
    public class VIDataGroupsAttribute : Attribute
    {
        private readonly Object[] _values;
        public string GroupName;

        public VIDataGroupsAttribute(params Object[] values) { _values = values; }

        public ValuesGroup DataGroups { get { return new ValuesGroup(GroupName, _values); } }
    }
}