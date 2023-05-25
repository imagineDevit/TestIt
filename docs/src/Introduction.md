# Introduction

Testit is a simple unit testing library for Java.

It is designed to be easy to use, easy to read, and easy to write.

It is built on JUnit platform so have to be used with JUnit.

---
The main goal of this library is to allow developers to write tests in <strong>GIVEN-WHEN-THEN</strong> style.

```java

@TestIt(name="1 + 1 should be 2")
void test(TestCase<Integer, Integer> testCase) {
   testCase
      .given("state is 1", () -> 1))
      .when("1 is added to the state", (input) -> input + 1))
      .then("the result should be 2", result -> assertEquals(2, result));
}

```