package io.github.imagineDevit.GWTUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public sealed class TestCaseContext<R> {

    private static final String RESULT = "###RESULT###";

    public non-sealed class GCtx extends TestCaseContext<R> {

        private final Map<String, Object> context = new HashMap<>();

        protected GCtx() {
            super();
        }

        public void set(String key, Object value) {
            context.put(key, value);
        }

        protected WCtx toWCtx() {
            return new WCtx(context);
        }

    }

    public non-sealed class WCtx extends TestCaseContext<R> {

        private final Map<String, Object> context = new HashMap<>();

        protected WCtx(Map<String, Object> context) {
            super();
            this.context.putAll(context);
        }

        public void setResult(R value) {
            context.put(RESULT, value);
        }

        public void setResult(Supplier<R> value) {
            context.put(RESULT, value.get());
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            return (T) context.get(key);
        }

        protected TCtx toTCtx() {
            return new TCtx(context);
        }

    }

    public non-sealed class TCtx extends TestCaseContext<R> {

        private final Map<String, Object> context = new HashMap<>();


        protected TCtx(Map<String, Object> context) {
            super();
            this.context.putAll(context);
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            return (T) context.get(key);
        }

        protected TestCaseResult<R> getResult() {
            return TestCaseResult.of(get(RESULT));
        }
    }
}
