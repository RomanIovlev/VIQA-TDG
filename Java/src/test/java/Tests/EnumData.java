package Tests;

import Tests.TestEnum;

/**
 * Created by 12345 on 18.12.2014.
 */
public @interface EnumData {
    public TestEnum[] enumValues() default {};
}
