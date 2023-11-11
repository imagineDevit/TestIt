package io.github.imagineDevit.giwt.annotations;

import java.lang.annotation.*;

/**
 * An annotation used to mark methods that should be executed before each test case in a testing framework.
 * <p>
 * This annotation can be applied to methods within a test class, and the annotated methods will be invoked
 * before each test case in the class. The methods marked with this annotation should not have any arguments
 * and they should not return any value.
 * <p>
 * The order of callback invocation can be specified using the {@link #order()} attribute. By default, the
 * order is set to 0, which means that the methods will be invoked in the order they are defined in the class.
 * Higher order values will result in the methods being invoked later.
 * <p>
 * This annotation is only relevant at runtime and can be accessed via reflection.
 *
 * @since 1.0
 * @see AfterEach
 * @see BeforeAll
 * @see AfterAll
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
