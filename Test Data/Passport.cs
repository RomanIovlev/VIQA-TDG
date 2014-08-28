using Testing.ValuesGenerator;
using Testing.ValuesGenerator.Attributes;

namespace VIQA_TDG.Test_Data
{
    public class Passport
    {
        [VIDataGroups("Roman", "Alexander", GroupName = "Correct")]
        [VIDataGroups("", null, GroupName = "Incorrect")]
        public string Name = "Roman";

        [VIDataGroups("1234", GroupName = "Correct")]
        [VIDataGroups("12345", "123", GroupName = "Incorrect")]
        public string Num = "4003";

        public int Series = 643542;

        public override string ToString() { return this.PrintGroupValues(); }
    }
}
