package io.github.imagineDevit.giwt.tests;


import io.github.imagineDevit.giwt.core.TestConfiguration;
import io.github.imagineDevit.giwt.core.TestParameters;
import io.github.imagineDevit.giwt.core.annotations.ParameterSource;


public class MyTestConfiguration implements TestConfiguration {


    @ParameterSource("getParams")
    private TestParameters<TestParameters.Parameter.P2<Integer, Integer>> parameters() {
        return TestParameters.of(
                TestParameters.Parameter.P2.of(1, 3),
                TestParameters.Parameter.P2.of(2, 4)
        );
    }
}
