package io.github.imagineDevit.GWTUnit.annotations;

import io.github.imagineDevit.GWTUnit.TestConfiguration;

import java.lang.annotation.*;

/**
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
