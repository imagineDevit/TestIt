package io.github.imaginedevit.testIt.annotations;

import io.github.imaginedevit.testIt.resolvers.TestCaseResolver;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Testable
@Documented
@ExtendWith(TestCaseResolver.class)
public @interface TestIt {
    String name();
}
