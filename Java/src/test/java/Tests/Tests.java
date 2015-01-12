package Tests;

import TestsExample.SomeDataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by 12345 on 18.12.2014.
 */

public class Tests {
    @Test//(dataProviderClass = SomeDataProvider.class, dataProvider = "someDP")
    public void someTest() throws Exception {
        Object[][] testData = SomeDataProvider.someDP();
        assertEquals(11664, testData[0].length);
        assertEquals(25, testData[1].length);
        assertEquals(11664, testData[2].length);
        assertEquals(3, testData[3].length);
        assertEquals(3888, testData[4].length);
        assertEquals(3, testData[5].length);
        assertEquals(3888, testData[6].length);
        assertEquals(1944, testData[7].length);
        assertEquals(1944, testData[8].length);
        assertEquals(2592, testData[9].length);
        assertEquals(9072, testData[10].length);
        assertEquals(25, testData[11].length);
        assertEquals(12, testData[12].length);
    }
}
