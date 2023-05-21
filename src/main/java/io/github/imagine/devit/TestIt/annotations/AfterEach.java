package io.github.imagine.devit.TestIt.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AfterEach {
    int order() default 0;
}
