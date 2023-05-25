package io.github.imagineDevit.GWTUnit;


import java.util.Map;

import static io.github.imagineDevit.GWTUnit.TestParameters.Parameter.*;

public class MyTestConfiguration implements TestConfiguration {
    @Override
    public Map<String, TestParameters<?>> parameterSources() {

        return Map.of(
              "getParams", TestParameters.of(
                        P2.of(1, 3),
                        P2.of(2, 4)
                )
        );
    }
}
