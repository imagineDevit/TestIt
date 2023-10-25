package io.github.imagineDevit.GWTUnit.tests;

import io.github.imagineDevit.GWTUnit.GwtUnitTestEngine;
import io.github.imagineDevit.GWTUnit.TestCase;
import io.github.imagineDevit.GWTUnit.annotations.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;

import static io.github.imagineDevit.GWTUnit.assertions.ShouldMatch.matching;

public class TestEngine {

    @Test(value = "test the gwt-unit-test engine")
    void verify(TestCase<Void, Events> tc) {

        tc
                .when("MyTest test class is launched", () ->
                        EngineTestKit.engine(GwtUnitTestEngine.ENGINE_ID)
                                .selectors(DiscoverySelectors.selectClass(MyTest.class))
                                .execute()
                                .testEvents())
                .then("list of resulting events should not be null",
                        result -> result.shouldBe().notNull())
                .and("""
                        list of resulting events should contain 8 events
                        7 of them should be successful
                        1 of them should be skipped
                        """, events ->
                        events.shouldMatch()
                                .all(
                                        matching("8 tests started", e -> {
                                            e.assertStatistics(stats -> stats.started(8));
                                            return true;
                                        }),
                                        matching("7 tests succeeded", e -> {
                                            e.assertStatistics(stats -> stats.succeeded(7));
                                            return true;
                                        }),
                                        matching("1 test skipped", e -> {
                                            e.assertStatistics(stats -> stats.skipped(1));
                                            return true;
                                        })
                                )
                );


    }

}
