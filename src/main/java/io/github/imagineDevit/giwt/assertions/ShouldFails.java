package io.github.imagineDevit.giwt.assertions;

public sealed interface ShouldFails extends Expectation.Failed {

    static WihType withType(Class<?> clazz) {
        return new WihType(clazz);
    }

    static WithMessage withMessage(String message) {
        return new WithMessage(message);
    }

    record WihType(Class<?> clazz) implements ShouldFails {
        @Override
        public void verify(Exception e) {
            if (!clazz.isInstance(e)) {
                throw new AssertionError("Expected error to be of type <" + clazz.getName() + "> but got <" + e.getClass().getName() + ">");
            }
        }
    }

    record WithMessage(String message) implements ShouldFails {
        @Override
        public void verify(Exception e) {
            if (!e.getMessage().equals(message)) {
                throw new AssertionError("Expected error message to be <" + message + "> but got <" + e.getMessage() + ">");
            }

        }
    }
}
