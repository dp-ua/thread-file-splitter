package com.sysgears.filesplitter.command;

/**
 * List of available commands
 */
public enum CommandType {
    SPLIT("разбивка файла на части"),
    EXIT("выход"),
    BLANK("введена пустая строка"),
    ERROR("команда задана не верно"),
    THREADS("список потоков"),
    ;
    private String description;

    CommandType(String description) {

        this.description = description;
    }
}


