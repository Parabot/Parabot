package org.parabot.environment.handlers.exceptions;

/**
 * Class to be implemented that allows multiple types of exception handlers
 */
public abstract class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    /**
     * The name of the exception handler
     */
    private final String name;

    /**
     * The status of the exception handler; Defines if the exception handler is enabled or disabled
     */
    private boolean enabled = true;

    /**
     * The type the handler is meant for
     */
    private ExceptionType exceptionType;

    public ExceptionHandler(String name, ExceptionType exceptionType) {
        this.name = name;
        this.exceptionType = exceptionType;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.handle(e);
    }

    /**
     * Writes the exception to class extending this abstract class
     *
     * @param e
     */
    public abstract void handle(Throwable e);

    /**
     * Returns if the exception handler is enabled or disabled
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled status of the exception handler
     *
     * @param enabled
     *
     * @return
     */
    public ExceptionHandler setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public ExceptionHandler setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
        return this;
    }

    public String getName() {
        return name;
    }

    public enum ExceptionType {
        SERVER("Server"),
        SCRIPT("Script"),
        CLIENT("Client");

        private final String name;

        ExceptionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
