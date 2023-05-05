import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.EventConditions;
import org.junit.platform.testkit.engine.Events;
import org.junit.platform.testkit.engine.TestExecutionResultConditions;

public class TestStats {

    @Test
    void verify() {
        Events events = EngineTestKit.engine("junit-jupiter")
                .selectors(DiscoverySelectors.selectClass(TestingStat.class))
                .execute()
                .testEvents();

        events.assertStatistics(stats -> stats
                .started(2)
                .succeeded(1)
                .failed(1)
                .skipped(1)
        );

        events.assertThatEvents()
                .haveExactly(1,
                        EventConditions.event(
                                EventConditions.test("test2"),
                                EventConditions.finishedWithFailure(
                                        TestExecutionResultConditions.instanceOf(RuntimeException.class),
                                        TestExecutionResultConditions.message("I must fail")

                                )
                        ));

    }
}
