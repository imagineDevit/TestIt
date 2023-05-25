package io.github.imagineDevit.GWTUnit.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ParameterSource {

    /**
     * @return the name of the parameter source
     */
    String value();
}
