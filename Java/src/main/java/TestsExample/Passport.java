package TestsExample;

import ru.viqa.test_data_generator.annotations.VIAllData;
import ru.viqa.test_data_generator.annotations.VIIntGroupData;
import ru.viqa.test_data_generator.annotations.VIStringGroupData;

import static TestsExample.PassportType.*;
import static ru.viqa.test_data_generator.utils.PrintUtils.printGroupValues;

/**
 * Created by 12345 on 29.12.2014.
 */
public class Passport {
    @VIAllData
    @VIEnumGroupData(value = {CIS, RUSSIAN}, group = "Test")
    public PassportType type = RUSSIAN;

    @VIIntGroupData(4003)
    public int number = 0;

    @VIIntGroupData({123456, 2})
    public int series = 4;

    @VIStringGroupData(value = {"name Family_name", "", "1"}, group = "Test2")
    public String name = "Default String Value";

    @Override
    public String toString() {
        try {
            return printGroupValues(this);
        } catch (Exception e) {
            return "Passport name generation failed";
        }
    }

    public Passport() { }
    public Passport(PassportType type) {
        switch (type) {
            case RUSSIAN:
                number = 1;
                series = 2;
                break;
            case CIS:
                number = 51;
                series = 61;
                break;
            case OTHER:
                number = 323;
                series = 333;
                break;

        }
        name = type.toString();
    }
}
