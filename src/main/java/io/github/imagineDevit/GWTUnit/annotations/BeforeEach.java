package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

/**
 * Annotate a method that has to be called before each test execution
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface BeforeEach {
    /**
     * @return the callback invocation order
     */
    int order() default 0;
}
