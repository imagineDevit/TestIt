package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface BeforeAll {
    int order() default 0;
}
