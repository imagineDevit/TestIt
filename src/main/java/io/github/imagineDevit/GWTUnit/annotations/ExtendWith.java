package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

/**
 * Give list of callback to be applied to the test class or method
 *
 * @see io.github.imagineDevit.GWTUnit.callbacks.BeforeAllCallback
 * @see io.github.imagineDevit.GWTUnit.callbacks.AfterAllCallback
 * @see io.github.imagineDevit.GWTUnit.callbacks.BeforeEachCallback
 * @see io.github.imagineDevit.GWTUnit.callbacks.AfterEachCallback
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
