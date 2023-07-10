package io.github.imagineDevit.GWTUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public sealed class TestCaseContext<T, R> {

    private static final String RESULT = "###RESULT###";
    private static final String STATE = "###STATE###";

    public non-sealed class GCtx extends TestCaseContext<T, R> {

        private final Map<String, Object> context = new HashMap<>();

        protected GCtx() {
            super();
        }

        public void setState(T value) {
            context.put(STATE, value);
        }

        @SuppressWarnings("unchecked")
        public void mapState(UnaryOperator<T> mapper) {
            context.computeIfPresent(STATE, (k, v) -> mapper.apply((T) v));

        }
        public void setVar(String key, Object value) {
            context.put(key, value);
        }

        protected WCtx toWCtx() {
            return new WCtx(context);
        }

    }

    public non-sealed class WCtx extends TestCaseContext<T, R> {

        private final Map<String, Object> context = new HashMap<>();

        protected WCtx(Map<String, Object> context) {
            super();
            this.context.putAll(context);
        }

        public void stateToResult(Function<T,R> mapper) {
            setResult(getState().toResult(mapper).value());
        }

        protected TestCaseCtxState<T> getState() {
            return TestCaseCtxState.of(getVar(STATE));
        }

        public void setResult(R value) {
            context.put(RESULT, value);
        }

        public void setResult(Supplier<R> value) {
            context.put(RESULT, value.get());
        }

        @SuppressWarnings("unchecked")
        public <TT> TT getVar(String key) {
            return (TT) context.get(key);
        }

        protected TCtx toTCtx() {
            return new TCtx(context);
        }

    }

    public non-sealed class TCtx extends TestCaseContext<T, R> {

        private final Map<String, Object> context = new HashMap<>();


        protected TCtx(Map<String, Object> context) {
            super();
            this.context.putAll(context);
        }

        @SuppressWarnings("unchecked")
        public R getVar(String key) {
            return (R) context.get(key);
        }

        protected TestCaseResult<R> getResult() {
            return TestCaseResult.of(getVar(RESULT));
        }
    }
}
