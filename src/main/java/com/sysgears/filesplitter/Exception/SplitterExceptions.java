package com.sysgears.filesplitter.Exception;

/**
 * Own class for handling errors in the project
 */
public class SplitterExceptions extends Exception {

    /**
     * Type of Exception. @see ExceptionTypes
     */
    private final ExceptionTypes exceptionTypes;

    /**
     * Message of exception
     */
    private final String message;

    /**
     * Set type and message
     *
     * @param exceptionTypes @see ExceptionTypes
     */
    public SplitterExceptions(ExceptionTypes exceptionTypes) {
        this.exceptionTypes = exceptionTypes;
        this.message = exceptionTypes.getDescription();
    }

    /**
     * Set Standart type of Exception and write own message to it
     *
     * @param message of Exception
     */
    public SplitterExceptions(String message) {
        this.exceptionTypes = ExceptionTypes.STANDART;
        this.message = message;
    }

    /**
     * get message of Exception
     *
     * @return String message
     */
    @Override
    public String getMessage() {
        return message;
    }
}
