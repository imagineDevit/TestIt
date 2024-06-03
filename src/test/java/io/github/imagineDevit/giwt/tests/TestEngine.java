package io.github.imagineDevit.giwt.tests;

import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.core.GiwtTestEngine;
import io.github.imagineDevit.giwt.core.annotations.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;

import java.util.Map;

import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.notNull;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToMatch.all;

class TestEngine {


    @Test(value = "test the giwt-test engine")
    void verify(TestCase<Void, Events> tc) {

        tc
                .when("MyTest test class is launched", () ->
                        EngineTestKit.engine(GiwtTestEngine.ENGINE_ID)
                                .selectors(DiscoverySelectors.selectClass(MyTest.class))
                                .execute()
                                .testEvents())
                .then("list of resulting events should not be null",
                        result -> result.shouldBe(notNull()))
                .and("""
                                list of resulting events should contain 8 events
                                9 of them should be successful
                                1 of them should be skipped
                                """, events -> events.shouldMatch(all(
                                Map.of(
                                        "9 tests started", e -> e.started().count() == 9,
                                        "8 tests succeeded", e -> e.succeeded().count() == 8,
                                        "1 test skipped", e -> e.skipped().count() == 1
                                ))
                        )

                );


    }

}
