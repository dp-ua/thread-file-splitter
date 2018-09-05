package com.sysgears.filesplitter.file.operation;

/**
 * List of available operations
 */
public enum OperationType {
    SPLIT("split files"),
    MERGE("merger files"),;
    private String description;

    OperationType(String description) {
        this.description = description;
    }
}
