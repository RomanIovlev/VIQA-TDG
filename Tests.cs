using System;
using NUnit.Framework;
using VIQA_TDG.Test_Data;

namespace VIQA_TDG
{
    [TestFixture]
    public class Tests
    {

        [Test]
        public void DefaulsScenario([ValueSource(typeof(DataProvider), "DefaultScenario")] TestData testData)
        {
            Console.Write("something");
        }

        [Test]
        public void SpecificTests([ValueSource(typeof(DataProvider), "SpecificData")] TestData testData)
        {
            Console.Write("something");
        }


        [Test]
        public void AllTests([ValueSource(typeof(DataProvider), "AllCombinations")] TestData testData)
        {
            Console.Write("something");
        }
    }
}
