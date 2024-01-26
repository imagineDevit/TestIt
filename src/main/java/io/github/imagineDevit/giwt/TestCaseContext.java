package io.github.imagineDevit.giwt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
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
            if (key.equals(STATE) || key.equals(RESULT))
                throw new IllegalArgumentException("key cannot be %s or %s".formatted(STATE, RESULT));

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

        /**
         * Apply a function to the context state and set the context result with the returned value
         * @param mapper the function to apply to the state
         */
        public void mapToResult(Function<T,R> mapper) {
            setResult(getState().toResult(mapper).value());
        }

        /**
         * Apply an action on the context state
         * @param consumer the action to apply
         */
        public WCtx applyOnState(Consumer<T> consumer) {
            consumer.accept(getState().getValue());
            return this;
        }

        @SuppressWarnings("unchecked")
        public void setStateAsResult() {
            setResult(((R) getState().getValue()));
        }

        /**
         * Get the context state
         */
        protected TestCaseCtxState<T> getState() {
            return TestCaseCtxState.of(getVar(STATE));
        }

        /**
         * Set the context result
         * @param value the result value to add to the context
         */
        private void setResult(R value) {
            context.put(RESULT, value);
        }

        /**
         * Get a context variable
         * @param key the variable key
         * @param <TT> the variable type
         * @return the variable value
         */
        @SuppressWarnings("unchecked")
        public <TT> TT getVar(String key) {
            Objects.requireNonNull(key);
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
        public <V> V getVar(String key) {
            return (V) context.get(key);
        }

        public T getState() {
            return getVar(STATE);
        }

        protected TestCaseResult<R> getResult() {
            return TestCaseResult.of(getVar(RESULT));
        }
    }
}
