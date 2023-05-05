import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TestingStat {

    @Test
    void test1(){}

    @Test
    void test2(){ throw  new RuntimeException("I must fail"); }

    @Test
    @Disabled
    void test3(){}

}
