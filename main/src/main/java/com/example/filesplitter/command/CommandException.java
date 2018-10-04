package com.example.filesplitter.command;

/**
 * The types of exceptions that occur during the parsing of the passed command
 */
public class CommandException extends Exception {
    enum Type {
        NULL("no command entered"),
        WRONG("wrong command entered"),
        WRONGARG("arguments are set incorrectly"),
        ;
        private String desc;

        Type(String desc) {
            this.desc = desc;
        }
    }

    private Type type;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public CommandException(Type type) {
        this.type=type;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return type.desc;
    }
}
