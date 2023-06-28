package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

/**
 * Skip a test
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Skipped {

    /**
     * @return the reason of skipping the test
     */
    String reason() default "";
}
