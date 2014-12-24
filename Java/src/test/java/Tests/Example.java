package Tests;

import Annotations.*;

/**
 * Created by 12345 on 18.12.2014.
 */
public class Example {
/*
    @VIDataValues(intValues = {1,2})
    public int Nums;
*/
    @EnumData(enumValues = TestEnum.One)
    public TestEnum Enums = TestEnum.Two;
}
