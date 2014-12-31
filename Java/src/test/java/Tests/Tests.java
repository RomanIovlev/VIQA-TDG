package Tests;

import TestsExample.Example;
import TestsExample.SomeDataProvider;
import org.testng.annotations.Test;

/**
 * Created by 12345 on 18.12.2014.
 */

public class Tests {

    @Test(dataProviderClass = SomeDataProvider.class, dataProvider = "someDP")
    public void someTest(Example ex) throws Exception {
    }

    @Test(dataProviderClass = SomeDataProvider.class, dataProvider = "someDP2")
    public void someTest2(Example ex) throws Exception {
    }
}
