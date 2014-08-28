using System;

namespace Testing.ValuesGenerator.Attributes
{
    [AttributeUsage(AttributeTargets.Property | AttributeTargets.Field, AllowMultiple = false, Inherited = false)]
    public class VIComplexDataAttribute : Attribute
    {
    }
}