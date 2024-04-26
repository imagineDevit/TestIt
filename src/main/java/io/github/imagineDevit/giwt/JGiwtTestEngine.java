package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.GiwtTestEngine;

/**
 * This class extends the GiwtTestEngine class and is used to create a test engine for JGiwt.
 * It is a part of the Giwt testing framework.
 * It is used to execute test cases using the JGiwtTestExecutor.
 *
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 */
@SuppressWarnings({"rawtypes"})
public class JGiwtTestEngine extends GiwtTestEngine<TestCase, JGiwtTestExecutor> {

    /**
     * The constructor for the JGiwtTestEngine class.
     * It creates a new instance of the JGiwtTestExecutor and passes it to the superclass constructor.
     */
    public JGiwtTestEngine() {
        super(new JGiwtTestExecutor());
    }
}