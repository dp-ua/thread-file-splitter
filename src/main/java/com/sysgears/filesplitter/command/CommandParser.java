package com.sysgears.filesplitter.command;

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
     * @return type of command in CommandTypes. If cant parsing command - return CommandTypes.ERROR
     */
    public CommandTypes getCommand() throws CommandExceptions {
        if (args.length == 0) throw new CommandExceptions(CommandExceptions.Type.NULL);

        try {
            return CommandTypes.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommandExceptions(CommandExceptions.Type.WRONG);
        }
    }

    /**
     * Get arguments from input string
     *
     * @return Map of arguments
     * @throws CommandExceptions if cant parse arguments
     */
    public Map<String, String> getArguments() throws CommandExceptions {
        Map<String, String> map = new HashMap<>();
        if (args.length <= 1) throw new CommandExceptions(CommandExceptions.Type.WRONGARG);
        if ((args.length - 1) % 2 != 0) throw new CommandExceptions(CommandExceptions.Type.WRONGARG);

        for (int i = 1; i < args.length; i = i + 2) {
            if (args[i].charAt(0) != '-') throw new CommandExceptions(CommandExceptions.Type.WRONGARG);
            map.put(args[i], args[i + 1]);

        }
        return map;
    }
}
