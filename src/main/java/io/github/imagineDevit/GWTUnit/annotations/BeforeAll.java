package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

/**
 * The {@code BeforeAll} annotation is used to mark a method that will be executed once before all test methods in a test class.
 * It is used in conjunction with the {@code TestFramework} class for test setup.
 * Only methods with the {@code BeforeAll} annotation will be considered as setup methods and will be executed before all other test methods.
 *
 * <p>The methods annotated with {@code BeforeAll} must have {@code public} access modifier and must not take any parameters.
 * These methods can be static or non-static.
 *
 * <p>The {@code BeforeAll} annotation can also specify the execution order of the setup methods using the {@code order} attribute.
 * Lower values for the {@code order} attribute will be executed first.
 * If not specified, the default order is 0.
 *
 * <p>Example usage:
 * <pre>{@code
 *   @BeforeAll
 *   public static void setUp() {
 *       // Perform setup logic here
 *   }
 *
 *   // other test methods
 * }</pre>
 *
 * @see BeforeEach
 * @see AfterAll
 * @see AfterEach
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface BeforeAll {
    /**
     * @return the callback invocation order
     */
    int order() default 0;
}
