package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AfterAll  {
    int order() default 0;
}
