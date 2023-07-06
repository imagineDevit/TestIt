# GWTUnit

---

[![minimum java version](https://img.shields.io/badge/java-17+-blue)](https://jdk.java.net/17/)
[![javadoc](https://javadoc.io/badge2/io.github.imagineDevit/GWTUnit/javadoc.svg)](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit)
[![GitHub](https://img.shields.io/github/license/imagineDevit/edgedb?style=flat)](https://github.com/imagineDevit/edgedb/blob/main/License)
[![GitHub contributors](https://badgen.net/github/contributors/imagineDevit/GWTUnit)](https://github.com/imagineDevit/GWTUnit/graphs/contributors)
![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/imagineDevit/GWTUnit/Maven%20Test)
![GitHub issues](https://img.shields.io/github/issues/imagineDevit/GWTUnit)

**GWTUnit** is a java test library based on [JUnit platform](https://junit.org/junit5/docs/current/user-guide/).
It gives developers the ability to write unit tests in the [GWT (Given-When-Then)](https://en.wikipedia.org/wiki/Given-When-Then) format.


_This is a simple usage example_ 👇
```java 
    class MyTest {

        @Test("1 + 1 should be 2")
        void test(TestCase<Integer, Integer> testCase) {
            testCase
                    .given("state is 1", 1)
                    .when("1 is added to the state", state -> state.onValue(i -> i + 1))
                    .then("the result should be equal to 3", result -> result.shouldBeEqualTo(2));
        }
    }
```

---
## TestCase

As seen in the example above, the test method takes a `TestCase<T,R>` as a parameter.

⚠️ It is a particularity of **GWTUnit** : ️ **_all test methods should have at least one parameter of type `TestCase<T,R>`_**.

TestCase is a generic Object that takes two types as parameters : `T` and `R`.

`T` represent the type of the state of the test. It can be any type. Use `Void` if you don't need a state. 

`R` represent the type of the result of the test. It can be any type. Use `Void` if you don't need a result.

`TestCase` and its linked statements ([GivenStmt<T,R>](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/TestCase.GivenStmt.html), [WhenStmt<T,R>](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/TestCase.WhenStmt.html) and [ThenStmt<T,R>](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/TestCase.ThenStmt.html) ) come with a set of methods that can be chained to write the test in the GWT format.

Each method takes a string as a parameter representing the statement description.


### Statement methods 
- #### given()
    This method sets the initial state of the test. It returns a [GivenStmt<T,R>](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/TestCase.GivenStmt.html) object.

- #### when()
    This method represents the action to be tested. It returns a [WhenStmt<T,R>](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/TestCase.WhenStmt.html) object. 

- #### then()
    This method allows the verification of the result of the test. It returns a [ThenStmt<T,R>](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/TestCase.ThenStmt.html) object.

- #### and()
    `GivenStmt<T,R>` and `ThenStmt<T,R>` classes have an `and()` method that allows to chain multiple statements of the same type.

---
## Annotations

**GWTUnit** provides a set of annotations that can be used to configure the test classes and methods.

- ### @Test

    **GWTUnit** provide a custom annotation `@Test` that can take a string as a parameter representing the test name. 

    If no parameter is provided, the test name will be the method name.

- ### @ParameterizedTest
    
    This annotation marks a test method as parameterized. It can take two parameters :
    - `name` : the test name. If not provided, the test name will be the method name.
  
    - `source` : the parameter source. Parameter source can be :
      - a method annotated [@ParameterSource](#ParameterSource) that returns a `TestParameters` object. (The source is the `@ParameterSource` value)) 
      - a entry of the Map<String, TestParameters> returned by the test configuration class (The source is the entry key)
 
    A parameterized test method must have all paremeters as arguments. The order of the parameters must be the same as the order of the parameters in the `TestParameters` object.


```java
    class MyTest {
    
        @ParameterizedTest(
                name = "Length of {0} should be equal to {1}",
                source = "getParams"
        )
        void test2(TestCase<String, Integer> testCase, String text, Integer expectedResult) {
            testCase
                    .given("state is %s".formatted(text), () -> text)
                    .when("state length is evaluated", state -> state.onValue(i -> i.length()))
                    .then("the result should be %d".formatted(expectedResult), result ->
                            result.shouldBeEqualTo(expectedResult)
                    );
        }

        @ParameterSource("getParams")
        private TestParameters<TestParameters.Parameter.P2<Integer, Integer>> getParams() {
            return TestParameters.of(
                    TestParameters.Parameter.P2.of("Hello", 5),
                    TestParameters.Parameter.P2.of("Bonjour", 7),
                    TestParameters.Parameter.P2.of("Good morning", 12)
            );
        }
    }
```
⚠️ _Please note that test parameters can be incorporated in the test name. The value of the parameter is denoted by placing its index within curly brackets_  👉 `Length of {0} should be equal to {1}`

- ### @Skipped
    This annotation marks a test method as skipped.

- ### @ParameterSource
    This annotation marks a method as a parameter source for a parameterized test method. It can take a string as a parameter representing the parameter source name. If no parameter is provided, the parameter source name will be the method name.

- ### @BeforeEach
    This annotation marks a method as a before each method. It will be executed before each test method.

- ### @AfterEach
    This annotation marks a method as a after each method. It will be executed after each test method.

- ### @BeforeAll
    This annotation marks a method as a before all method. It will be executed before all test methods.

- ### @AfterAll
    This annotation marks a method as a after all method. It will be executed after all test methods.

- ### @ExtendWith

  This annotation registers a list of callbacks for a test class.
  It can take a list of classes as a parameter. These classes must implement the [BeforeEachCallback](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/callbacks/BeforeEachCallback.html), 
  [BeforeAllCallback](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/callbacks/BeforeAllCallback.html),  
  [AfterEachCallback](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/callbacks/AfterEachCallback.html) 
  and/or [AfterAllCallback](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/callbacks/AfterAllCallback.html) interfaces.

- ### @ConfigureWith
  This annotation registers a class as the test class configuration. It take a class that must implement [TestConfiguration](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest/io/github/imagineDevit/GWTUnit/TestConfiguration.html) as parameter







