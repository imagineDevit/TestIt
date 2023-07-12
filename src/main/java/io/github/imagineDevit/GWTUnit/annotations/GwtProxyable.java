package io.github.imagineDevit.GWTUnit.annotations;


import java.lang.annotation.*;

/**
 * Annotation to indicate that a proxy test class should be generated for this class.
 * <br>
 * The proxy test class will be generated in the same package as the annotated class.
 * <br>
 * The proxy test class will have the same name as the annotated class with the suffix "TestProxy".
 * <br><br>
 * In order to facilitate testing of this class, a record is generated for each public method that has more than one argument.
 * <br>
 * By default, the record will have the same name as the method with the suffix "Params". <br>
 * If the method is annotated with {@link ParameterRecordName}, the value of the annotation will be used as the record name.
 * <br>
 * The record will have a constructor that takes the same arguments as the method.
 * <br>
 * <br>
 * Example:
 * <pre>
 *{@code
 *
 * @GwtProxyable
 * class Foo {
 *     public void bar(String a, String b) {}
 * }
 *
 * // The following record will be generated:
 *
 * class FooProxyTest {
 *
 *     private final Foo delegate;
 *
 *     FooProxyTest(Foo delegate) {
 *          this.delegate = delegate;
 *     }
 *
 *     record BarParams(String a, String b) {}
 *
 *     public void bar(BarParams param) {
 *         delegate.bar(param.a(), param.b());
 *     }
 * }
 *
 * // And can be used as follows in the test class
 *
 * class FooTest {
 *
 *    FooProxyTest proxy = new FooProxyTest(new Foo());
 *
 *    @Test
 *    void testBar(TestCase<FooProxyTest.BarParams, Void> testCase)  {
 *        testCase
 *          .given("", new FooProxyTest.BarParams("a", "b"))
 *          .when("bar is called", proxy::bar)
 *          .then("bar is called with the correct arguments", (result) -> {})
 *    }
 * }
 * }
 * </pre>
 *
 * @see ParameterRecordName
 * @see io.github.imagineDevit.GWTUnit.annotations.processors.GwtProxyableProcessor
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@Documented
public @interface GwtProxyable {}
