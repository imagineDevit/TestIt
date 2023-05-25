package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

/**
 * Annotate a method that has to be called after each test execution
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AfterEach {
    /**
     * @return the callback invocation order
     */
    int order() default 0;
}
