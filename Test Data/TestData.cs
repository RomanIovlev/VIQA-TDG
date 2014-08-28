using Testing.ValuesGenerator;
using Testing.ValuesGenerator.Attributes;

namespace VIQA_TDG.Test_Data
{
    public class TestData
    {
        [VIDataValues(true, false)]
        public bool HavePhone;

        [VIDataValues(UserStatus.Common, UserStatus.VIP)]
        public UserStatus Status = UserStatus.Common;

        [VIDataGroups("SimpleUser", "A", GroupName = "Correct")]
        [VIDataGroups("", null, "12345678910", "two words", GroupName = "Incorrect")]
        public string NickName = "Simple User";

        [VIDataGroups("", "some additional data", "one more data", GroupName = "Correct")]
        public string AdditionalData;

        [VIComplexData]
        public Passport PassportData = new Passport();

        public int SomeOtherData;
        public int SomeOtherData1;
        public int SomeOtherData2;

        public override string ToString() { return this.PrintGroupValues(); }
    }

    public enum UserStatus { Common, VIP }
}
