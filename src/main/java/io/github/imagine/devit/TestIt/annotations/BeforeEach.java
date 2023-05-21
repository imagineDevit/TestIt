package io.github.imagine.devit.TestIt.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface BeforeEach {
    int order() default 0;
}
