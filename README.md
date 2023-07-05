# GWTUnit

---

[![minimum java version](https://img.shields.io/badge/java-17+-blue)](https://jdk.java.net/17/)
[![minimum java version](https://img.shields.io/badge/javadocs-📒-orange)](https://javadoc.io/doc/io.github.imagineDevit/GWTUnit/latest)
[![GitHub](https://img.shields.io/github/license/imagineDevit/edgedb?style=flat)](https://github.com/imagineDevit/edgedb/blob/main/License)
[![GitHub contributors](https://badgen.net/github/contributors/imagineDevit/GWTUnit)](https://github.com/imagineDevit/GWTUnit/graphs/contributors)
![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/imagineDevit/GWTUnit/Maven%20Package)

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





