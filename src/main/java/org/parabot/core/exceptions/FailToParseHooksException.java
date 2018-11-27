package org.parabot.core.exceptions;

/**
 * @author MaurerThomas
 */
public class FailToParseHooksException extends Exception {
    public FailToParseHooksException() {
        super();
    }

    public FailToParseHooksException(String message) {
        super(message);
    }

    public FailToParseHooksException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailToParseHooksException(Throwable cause) {
        super(cause);
    }

    protected FailToParseHooksException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
