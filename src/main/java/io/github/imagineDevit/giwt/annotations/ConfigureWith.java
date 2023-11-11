package io.github.imagineDevit.giwt.annotations;

import io.github.imagineDevit.giwt.TestConfiguration;

import java.lang.annotation.*;

/**
 * Annotation that specifies the test configuration class for a particular class.
 * The annotated class can be configured with an instance of the specified test configuration class.
 *
 * @see TestConfiguration
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ConfigureWith {
    /**
     * @return the test configuration class
     */
    Class<? extends TestConfiguration> value();
}
