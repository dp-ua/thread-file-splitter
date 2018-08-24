package com.sysgears.filesplitter.command;

/**
 * List of available commands
 */
public enum CommandTypes {
    SPLIT("split files"),
    EXIT("exit"),
    BLANK("empty string is entered"),
    ERROR("wrong command"),
    THREADS("threads list"),
    MERGE("merger files"),
    ;
    private String description;

    CommandTypes(String description) {
        this.description = description;
    }
}


