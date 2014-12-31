package TestsExample;

import annotations.*;

import static TestsExample.TestEnum.*;
import static utils.PrintUtils.printGroupValues;

/**
 * Created by 12345 on 18.12.2014.
 */
public class Example {

    @VIAllData
    @VIEnumGroupData(value = {One, Three}, group = "Test")
    public TestEnum Enums = Two;

    @VIIntGroupData(1)
    public int OneInt = 3;

    @NoGroup({1,3})
    @VIIntGroupData({1, 2})
    public int SomeInts = 5;

    @VIIntGroupData(value = {1, 2, 3}, group = "Test")
    public int SomeIntsGroup = -1;

    @VIStringGroupData(value = {"string value", "", "1"}, group = "Test2")
    public String Strings = "Default Example value";

    @VIAllData
    @VIBoolGroupData(value = true, group = "Test")
    public boolean BooleanAll = false;

    @VIComplexData
    public SubClass subClass = new SubClass();

    @Override
    public String toString() {
        try {
            return printGroupValues(this);
        } catch (Exception e) {
            return "Name generation failed";
        }
    }
}
