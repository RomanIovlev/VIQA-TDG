using System.Collections.Generic;
using Testing.ValuesGenerator;

namespace VIQA_TDG.Test_Data
{
    public class DataProvider
    {
        public List<TestData> DefaultScenario = new VIDataGenerator<TestData>().GenerateCombinations();

        public List<TestData> AllCombinations = new VIDataGenerator<TestData>().GenerateCombinations("All");

        public List<TestData> AllPossibleValues = new VIDataGenerator<TestData>().GenerateValues("All");

        public List<TestData> CorrectCombinations = new VIDataGenerator<TestData>().GenerateValues("Correct");

        public List<TestData> CorrectIncorrectCombinations = new VIDataGenerator<TestData>().GenerateCombinations("Correct", "Incorrect");

        public List<TestData> SpecificData = new VIDataGenerator<TestData>().GenerateCombinations("All").ApplyFilters(
            td => td.Status == UserStatus.VIP,
            td => td.PassportData.Name != null && td.AdditionalData == "some additional data");
    }
}
