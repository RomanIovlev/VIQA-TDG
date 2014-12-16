using System;

namespace Testing.ValuesGenerator.Attributes
{
    [AttributeUsage(AttributeTargets.All | AttributeTargets.Field, AllowMultiple = true, Inherited = false)]
    public class VIDataValuesAttribute : Attribute
    {
        public readonly ValuesGroup Values;

        public VIDataValuesAttribute(params Object[] values)
        {
            Values = new ValuesGroup("All", values);
        }
    }
}