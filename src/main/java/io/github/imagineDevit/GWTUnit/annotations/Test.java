package io.github.imagineDevit.GWTUnit.annotations;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

/**
 * Annotate a test method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Testable
@Documented
public @interface Test {
    /**
     * @return the test case name
     */
    String value() default "";
}
