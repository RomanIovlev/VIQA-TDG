package Annotations;

import Data.MyEnum;

import java.lang.annotation.*;

/**
 * Created by 12345 on 16.12.2014.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface VIDataValues {
    public String[] value() default {};
    public int[] intValues() default {};
    public boolean[] boolValues() default {};
    public MyEnum[] enumValues() default {};
    public Class<?> classVal();
}
