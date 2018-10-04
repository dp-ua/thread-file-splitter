package com.example.filesplitter.command;

/**
 * Available command types
 */
public enum CommandType {
    SPLIT("split files"),
    MERGE("merger files"),
    EXIT("exit"),
    THREADS("threads list"),
    ;
    private String description;

    CommandType(String description) {
        this.description = description;
    }
}


