package io.github.imagineDevit.GWTUnit.annotations;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

/**
 * Indicate that the test is executed on different parameters
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Testable
@Documented
public @interface ParameterizedTest {

    /**
     * @return the test case name
     */
    String name();

    /**
     * @return the parameter source name
     */
    String source() default "";
}
