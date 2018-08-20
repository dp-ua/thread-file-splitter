package com.sysgears.filesplitter.Exception;

/**
 * Own class for handling errors in the project
 */
public class MyException extends Exception {

    /**
     * Type of Exception. @see TypeException
     */
    private final TypeException typeException;

    /**
     * Message of exception
     */
    private final String message;

    /**
     * Set type and message
     *
     * @param typeException @see TypeException
     */
    public MyException(TypeException typeException) {
        this.typeException = typeException;
        this.message = typeException.getDescription();
    }

    /**
     * Set Standart type of Exception and write own message to it
     *
     * @param message of Exception
     */
    public MyException(String message) {
        this.typeException = TypeException.STANDART;
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
