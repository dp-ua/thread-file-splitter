package com.sysgears.filesplitter.command;

/**
 * List of available commands
 */
public enum CommandTypes {
    SPLIT("split files"),
    EXIT("exit"),
    THREADS("threads list"),
    MERGE("merger files"),
    ;
    private String description;

    CommandTypes(String description) {
        this.description = description;
    }
}


