package Tests;

import Data.DataValues;
import org.testng.annotations.Test;

/**
 * Created by 12345 on 18.12.2014.
 */

public class Tests {

    @Test
    public void someTest() throws Exception {
        TestEnum t = (TestEnum)DataValues.getEnums(new Example());
        int j = 2;
    }
}
