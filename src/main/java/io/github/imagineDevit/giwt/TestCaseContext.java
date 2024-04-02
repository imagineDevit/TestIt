package io.github.imagineDevit.giwt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static io.github.imagineDevit.giwt.core.utils.TextUtils.blue;
import static io.github.imagineDevit.giwt.core.utils.TextUtils.bold;

/**
 * A context for a test case
 *
 * @param <T> the state type
 * @param <R> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
@SuppressWarnings({"unused", "unchecked"})
public sealed class TestCaseContext<T, R> {

    final Map<String, Object> context = new HashMap<>();

    private static final String RESULT = "###RESULT###";

    private static final String STATE = "###STATE###";

    private TestCaseContext(Map<String, Object> context) {
        Optional.ofNullable(context).ifPresent(this.context::putAll);
    }

    /**
     * Set a context variable
     *
     * @param key   the variable key
     * @param value the variable value
     */
    public void setVar(String key, Object value) {
        if (key.equals(STATE) || key.equals(RESULT))
            throw new IllegalArgumentException(
                    """
                            %s or %s cannot be set by using %s method.
                            Please consider using the dedicated methods %s or %s instead.
                            """.formatted(
                            STATE,
                            RESULT,
                            bold(blue("setVar()")),
                            bold(blue("setState()")),
                            bold(blue("mapToResult()"))

                    ));


        context.put(key, value);
    }

    /**
     * Get a context variable
     *
     * @param key  the variable key
     * @param <TT> the variable type
     * @return the variable value
     */
    public <TT> TT getVar(String key) {
        Objects.requireNonNull(key);
        if (key.equals(STATE) || key.equals(RESULT))
            throw new IllegalArgumentException(
                    """
                            %s or %s cannot be retrieved by using %s method.
                            """.formatted(
                            STATE,
                            RESULT,
                            bold(blue("getVar()"))
                    ));
        return (TT) context.getOrDefault(key, null);
    }

    /**
     * Get a context variable safely
     * @param key the variable key
     * @return the variable value
     * @param <TT> the variable value type
     */
    protected  <TT> TT safeGetVar(String key) {
        Objects.requireNonNull(key);
        return (TT) context.getOrDefault(key, null);
    }

    /**
     * Get the context state
     */
    protected TestCaseCtxState<T> getState() {
        return (TestCaseCtxState<T>) Optional.ofNullable(safeGetVar(STATE)).orElse(TestCaseCtxState.empty());
    }

    public static non-sealed class GCtx<T, R> extends TestCaseContext<T, R> {

        protected GCtx() {
            super(null);
            setState(null);
        }

        /**
         * Set the context state
         * @param value the state value
         */
        public void setState(T value) {
            context.put(STATE, TestCaseCtxState.of(value));
        }

        /**
         * Map the context state
         * @param mapper the function to apply to the state
         */
        public void mapState(UnaryOperator<T> mapper) {
            context.computeIfPresent(STATE, (k, v) -> ((TestCaseCtxState<T>) v).map(mapper));
        }

        /**
         * Convert the context to a WCtx
         * @return a WCtx instance
         */
        protected WCtx<T, R> toWCtx() {
            return new WCtx<>(context);
        }

    }

    public static non-sealed class WCtx<T, R> extends TestCaseContext<T, R> {

        protected WCtx(Map<String, Object> context) {
            super(context);
            setResult(TestCaseCtxResult.empty());
        }

        /**
         * Apply a function to the context state and set the context result with the returned value
         *
         * @param mapper the function to apply to the state
         */
        public void mapToResult(Function<T, R> mapper) {
           setResult(getState().toResult(mapper));
        }

        /**
         * Apply an action on the context state
         *
         * @param consumer the action to apply
         */
        public WCtx<T, R> applyOnState(Consumer<T> consumer) {
           consumer.accept(getState().value());
            return this;
        }

        /**
         * Set the context state as the context result
         */
        public void setStateAsResult() {
           setResult(getState().toResult(t -> (R) t));
        }

        /**
         * Supply the context result
         * @param supplier the supplier to get the result value
         */
        public void supplyResult(Supplier<R> supplier) {
            setResult(TestCaseCtxResult.of(supplier.get()));
        }

        /**
         * Set the context result
         *
         * @param value the result value to add to the context
         */
        protected void setResult(TestCaseCtxResult<R> value) {
            context.put(RESULT, value);
        }

        /**
         * Convert the context to a TCtx
         * @return a TCtx instance
         */
        protected TCtx<T, R> toTCtx() {
            return new TCtx<>(context);
        }

    }

    public static non-sealed class TCtx<T, R> extends TestCaseContext<T, R> {

        protected TCtx(Map<String, Object> context) {
            super(context);
        }

        /**
         * Get the context result
         * If the result is not present, an empty result is returned
         * @return the context result
         */
        protected TestCaseResult<R> getResult() {
            return Optional.ofNullable(this.<TestCaseCtxResult<R>>safeGetVar(RESULT))
                    .map(TestCaseCtxResult::result)
                    .orElse(TestCaseResult.empty());
        }
    }
}
