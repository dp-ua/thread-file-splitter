package com.sysgears.filesplitter.command;

import com.sysgears.filesplitter.Exception.MyException;
import com.sysgears.filesplitter.Exception.TypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for input String line
 */
public class CommandParser {
    /**
     * Line for parse
     */
    private String[] args;

    /**
     * Set line for parsing
     *
     * @param input string for parse
     */
    public void setArgs(String input) {
        if (input == null) input = "";
        this.args = input.split(" ");
    }

    /**
     * Blank constructor
     */
    public CommandParser() {
    }

    /**
     * Initialize and set string for parsing
     *
     * @param args input string for parse
     */
    public CommandParser(String args) {
        this.args = args.split(" ");
    }

    /**
     * Get type of command
     *
     * @return type of command in CommandType. If cant parsing command - return CommandType.ERROR
     */
    public CommandType getCommand() {
        if (args.length == 0) return CommandType.BLANK;

        try {
            return CommandType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return CommandType.ERROR;
        }
    }

    /**
     * Get arguments from input string
     *
     * @return Map of arguments
     * @throws MyException if cant parse arguments
     */
    public Map<String, String> getArguments() throws MyException {
        Map<String, String> map = new HashMap<>();
        if (args.length <= 1) throw new MyException(TypeException.WRONGARG);
        if ((args.length - 1) % 2 != 0) throw new MyException(TypeException.WRONGARG);

        for (int i = 1; i < args.length; i = i + 2) {
            if (args[i].charAt(0) != '-') throw new MyException(TypeException.WRONGARG);
            map.put(args[i], args[i + 1]);

        }
        return map;
    }
}
