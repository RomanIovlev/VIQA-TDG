package TestsExample;

import ru.viqa.test_data_generator.annotations.*;

import static TestsExample.PassportType.*;
import static ru.viqa.test_data_generator.utils.PrintUtils.printGroupValues;

/**
 * Created by 12345 on 18.12.2014.
 */
public class Example {

    @Bad
    public String[] aStrings = new String[]{};

    @VIStringGroupData({"RUSSIAN", "CIS", "OTHER"})
    public String[] arrayStrings = new String[]{};

    @VIAllData
    @VIEnumGroupData(value = {RUSSIAN, OTHER}, group = "Test")
    public PassportType type = CIS;

    @VIIntGroupData(1)
    public int number = 3;

    @NoGroup({1,3})
    @VIIntGroupData({1, 2})
    public int сode = 5;

    @VIIntGroupData(value = {1, 2, 3}, group = "Test")
    public int testCode = -1;

    @VIStringGroupData(value = {"string value", "", "1"}, group = "Test2")
    public String name = "Default Example value";

    @VIAllData
    @VIBoolGroupData(value = true, group = "Test")
    public boolean isRussian = false;

    @VIComplexData
    public Passport passport = new Passport();

    @Override
    public String toString() {
        try {
            return printGroupValues(this);
        } catch (Exception e) {
            return "name generation failed";
        }
    }
}
