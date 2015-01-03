package TestsExample;

import ru.viqa.test_data_generator.annotations.VIAllData;
import ru.viqa.test_data_generator.annotations.VIIntGroupData;
import ru.viqa.test_data_generator.annotations.VIStringGroupData;

import static TestsExample.TestEnum.*;
import static ru.viqa.test_data_generator.utils.PrintUtils.printGroupValues;

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

    @Override
    public String toString() {
        try {
            return printGroupValues(this);
        } catch (Exception e) {
            return "Name generation failed";
        }
    }
}
