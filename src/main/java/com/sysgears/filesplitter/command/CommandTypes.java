package com.sysgears.filesplitter.command;

/**
 * Available command types
 */
public enum CommandTypes {
    SPLIT("split files"),
    MERGE("merger files"),
    EXIT("exit"),
    THREADS("threads list"),
    ;
    private String description;

    CommandTypes(String description) {
        this.description = description;
    }
}


