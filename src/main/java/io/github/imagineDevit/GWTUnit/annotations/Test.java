package io.github.imagineDevit.GWTUnit.annotations;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

/**
 * The {@code Test} annotation is used to mark a method as a test case. This allows the test case to be
 * executed by a testing framework or other test runner.
 *
 * <p>By default, the test case name is optionally specified using the {@code value} parameter of the
 * annotation. If not explicitly specified, the test case name will be the test method simple name.
 *
 * <p>Note that this annotation is retained at runtime and can be accessed via reflection.
 *
 * @see Testable
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
