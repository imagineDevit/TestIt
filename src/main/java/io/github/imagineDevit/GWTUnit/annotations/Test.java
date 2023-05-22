package io.github.imagineDevit.GWTUnit.annotations;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Testable
@Documented
public @interface Test {
    String value() default "";
}
