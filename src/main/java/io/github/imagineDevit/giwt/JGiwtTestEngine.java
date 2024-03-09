package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.GiwtTestEngine;

/**
 * Giwt test engine
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 */
@SuppressWarnings({"rawtypes"})
public class JGiwtTestEngine extends GiwtTestEngine<TestCase, JGiwtTestExecutor> {
    public JGiwtTestEngine() {
        super(new JGiwtTestExecutor());
    }
}
