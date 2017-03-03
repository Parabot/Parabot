package org.parabot.core.exceptions;

/**
 *
 * @author MaurerThomas
 *
 */
public class FieldNotFoundException extends Exception {

    public FieldNotFoundException() {
        super();
    }

    public FieldNotFoundException(String message) {
        super(message);
    }

    public FieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldNotFoundException(Throwable cause) {
        super(cause);
    }

    protected FieldNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
