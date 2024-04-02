package io.github.imagineDevit.giwt.tests;

import io.github.imagineDevit.giwt.core.GiwtTestEngine;
import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.core.annotations.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;

import static io.github.imagineDevit.giwt.assertions.ShouldMatch.matching;

public class TestEngine {

    @Test(value = "test the giwt-test engine")
    void verify(TestCase<Void, Events> tc) {

        tc
                .when("MyTest test class is launched", () ->
                        EngineTestKit.engine(GiwtTestEngine.ENGINE_ID)
                                .selectors(DiscoverySelectors.selectClass(MyTest.class))
                                .execute()
                                .testEvents())
                .then("list of resulting events should not be null",
                        result -> result.shouldBe().notNull())
                .and("""
                        list of resulting events should contain 8 events
                        9 of them should be successful
                        1 of them should be skipped
                        """, events ->
                        events.shouldMatch()
                                .all(
                                        matching("9 tests started", e -> {
                                            e.assertStatistics(stats -> stats.started(9));
                                            return true;
                                        }),
                                        matching("8 tests succeeded", e -> {
                                            e.assertStatistics(stats -> stats.succeeded(8));
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
