package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface ExtendWith {
    Class<?>[] value();
}
