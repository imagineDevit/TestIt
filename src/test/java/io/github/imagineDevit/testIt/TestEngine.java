package io.github.imagineDevit.testIt;

import io.github.imaginedevit.testIt.TestCase;
import io.github.imaginedevit.testIt.TestIt;
import io.github.imaginedevit.testIt.TestItClass;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;

@TestItClass
public class TestEngine {

    @TestIt(name = "test the test-it engine")
    void verify(TestCase<Void, Events> tc) {

        tc
                .when("I do something", () ->
                        EngineTestKit.engine("test-it-engine")
                                .selectors(DiscoverySelectors.selectClass(MyTest.class))
                                .execute()
                                .testEvents())
                .then("I should get a test event", (events) -> {
                    Assertions.assertTrue(events.isPresent());
                    events.get().assertStatistics(stats -> stats
                            .started(1)
                            .succeeded(1)
                    );
                });

    }

}
