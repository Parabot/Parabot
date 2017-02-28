package org.parabot.core.exceptions;

/**
 *
 * @author MaurerThomas
 *
 */
public class FailToInjectHooksException extends Exception {
    public FailToInjectHooksException() {
        super();
    }

    public FailToInjectHooksException(String message) {
        super(message);
    }

    public FailToInjectHooksException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailToInjectHooksException(Throwable cause) {
        super(cause);
    }

    protected FailToInjectHooksException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
