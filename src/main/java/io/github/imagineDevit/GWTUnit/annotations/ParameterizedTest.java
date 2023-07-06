package io.github.imagineDevit.GWTUnit.annotations;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

/**
 * A custom annotation used to mark a test method as a parameterized test.
 * The test case name and parameter source name can be specified using this annotation.
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
    String source();
}
