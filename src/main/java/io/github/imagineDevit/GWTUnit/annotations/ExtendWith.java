package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

/**
 * Annotation that specifies the extensions classes for a particular class.
 * The annotated class can be configured with an instance of the specified extensions classes.
 *
 * @see io.github.imagineDevit.GWTUnit.callbacks.BeforeAllCallback
 * @see io.github.imagineDevit.GWTUnit.callbacks.BeforeEachCallback
 * @see io.github.imagineDevit.GWTUnit.callbacks.AfterEachCallback
 * @see io.github.imagineDevit.GWTUnit.callbacks.AfterAllCallback
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface ExtendWith {
    /**
     * @return array of extensions classes
     */
    Class<?>[] value();
}
