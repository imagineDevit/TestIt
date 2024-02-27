package io.github.imagineDevit.giwt.callbacks;

public sealed interface Callback permits AfterAllCallback, AfterEachCallback, BeforeAllCallback, BeforeEachCallback {

    class Methods {
        public static final String BEFORE_EACH = "beforeEach";
        public static final String BEFORE_ALL = "beforeAll";
        public static final String AFTER_EACH = "afterEach";
        public static final String AFTER_ALL = "afterAll";
    }
}
