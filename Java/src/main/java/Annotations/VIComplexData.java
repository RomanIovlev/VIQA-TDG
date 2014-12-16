package Annotations;

import java.lang.annotation.*;

/**
 * Created by 12345 on 16.12.2014.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface VIComplexData {
}
