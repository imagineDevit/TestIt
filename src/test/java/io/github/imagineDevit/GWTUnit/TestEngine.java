package io.github.imagineDevit.GWTUnit;

import io.github.imagineDevit.GWTUnit.annotations.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;

public class TestEngine {

    @Test(value = "test the test-it engine")
    void verify(TestCase<Void, Events> tc) {

        tc
                .when("I do something", () ->
                        EngineTestKit.engine("test-it-engine")
                                .selectors(DiscoverySelectors.selectClass(MyTest.class))
                                .execute()
                                .testEvents())
                .then("I should get a test event", (events) ->
                        events
                                .shouldBeNotNull()
                                .assertAll(e -> e.assertStatistics(
                                                stats -> stats
                                                        .started(5)
                                                        .succeeded(4)
                                                        .skipped(1))
                                )

                );

    }

}
