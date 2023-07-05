package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

/**
 * This annotation is used to mark a test class or method as skipped. It can be applied to a class or a method and provides a reason for why the test is being skipped.
 *
 *
 * <p>Usage:</p>
 *      <ul>
 *          <li>Apply this annotation to a test class to mark the entirety of the class as skipped.</li>
 *          <li>Apply this annotation to a test method to mark only that specific method as skipped.</li>
 *      </ul>
 *
 * <p> Example usage: </p>
 * <pre>
 * {@code
 *     // Skipping the entire test class
 *     @Skipped(reason = "Test is incomplete")
 *     public class MySkippedTestClass {
 *         // test methods...
 *     }
 *
 *     // Skipping a specific test method
 *     public class MyTest {
 *         @Skipped(reason = "Test is no longer relevant")
 *         public void testMethod() {
 *             // test logic...
 *         }
 *     }
 * }
 *</pre>
 *
 *
 * When a test class or method is marked as skipped using this annotation, it indicates that the test is intentionally excluded from execution due to a specific reason.
 * The reason for skipping the test can be provided using the "reason" attribute of the annotation.
 * Note: Skipped tests should not be considered as failures or ignored tests. They are intentionally excluded from execution and should not be run as part of the test suite.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Skipped {

    /**
     * @return the reason of skipping the test
     */
    String reason() default "";
}
