import io.github.imaginedevit.testIt.annotations.TestIt;
import io.github.imaginedevit.testIt.annotations.TestItClass;
import io.github.imaginedevit.testIt.TestCase;
import org.junit.jupiter.api.Assertions;


@TestItClass
public class MyTest {

    @TestIt(name = "(1 * 2) + 1 should be equal to 3")
    public void test(TestCase<Integer,Integer> testCase){
        testCase
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", state -> state.map(i -> i * 2).orElse(0))
                .when("1 is added to the state", state -> state.map(i -> i + 1).orElse(0))
                .then("the result should be 3", result -> {
                    Assertions.assertTrue(result.isPresent());
                    Assertions.assertEquals(3, result.get());
                });

    }
}
