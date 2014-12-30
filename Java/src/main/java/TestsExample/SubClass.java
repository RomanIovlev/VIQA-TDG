package TestsExample;

import annotations.VIAllData;
import annotations.VIIntGroupData;
import annotations.VIStringGroupData;

import static TestsExample.TestEnum.*;

/**
 * Created by 12345 on 29.12.2014.
 */
public class SubClass {
    @VIAllData
    @VIEnumGroupData(value = {Two, One}, group = "Test")
    public TestEnum Enums = One;

    @VIIntGroupData(1)
    public int OneInt = 0;

    @VIIntGroupData({1, 2})
    public int SomeInts = 4;

    @VIStringGroupData(value = {"string value", "", "1"}, group = "Test2")
    public String Strings = "Default String Value";
}
