package io.github.imagineDevit.giwt.annotations;

import java.lang.annotation.*;

/**
 * Represents an annotation used to annotate methods that should be executed after each test method in a test class.
 * This annotation is used in conjunction with test frameworks that support test lifecycle hooks.
 *
 * <p>
 * The <code>@AfterEach</code> annotation can be placed on a method that should be executed after each test method in a test class.
 * The annotated method will be executed after each individual test method is executed.
 * </p>
 *
 * <p>
 * The <code>@AfterEach</code> annotation can also have an optional <code>order</code> parameter that specifies the callback invocation order.
 * The default value of the <code>order</code> parameter is 0, meaning that the annotated method will be invoked after other methods with the same order value,
 * and before methods with a higher order value.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>{@code
 *
 * import org.junit.jupiter.api.AfterEach;
 * import org.junit.jupiter.api.Test;
 *
 * class MyTest {
 *
 *     @AfterEach(order = 1)
 *     void cleanUpDatabase() {
 *         // Code to clean up the database
 *     }
 *
 *     @Test
 *     void testMethod1() {
 *         // Code for test method 1
 *     }
 *
 *     @Test
 *     void testMethod2() {
 *         // Code for test method 2
 *     }
 *
 * }}
 *
 * </pre>
 *
 * @see BeforeAll
 * @see BeforeEach
 * @see AfterAll
 * @since 1.0
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
