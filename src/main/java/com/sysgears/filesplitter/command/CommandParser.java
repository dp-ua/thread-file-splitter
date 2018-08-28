package com.sysgears.filesplitter.command;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for input String line
 */
public class CommandParser {
    private final Logger log = Logger.getLogger(CommandParser.class);

    private String[] args;

    /**
     * Set the input string.
     * <p>
     * The command and all arguments must be separated by a space.
     *
     * @param input Accept the input line and split it into its component parts.
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
     * Get the type of command
     * <p>
     * By default, the command must be specified by the first element in the argument list.
     * All that goes further is the arguments of the team.
     *
     * @return type of command in CommandTypes. If cant parsing command - return CommandTypes.ERROR
     */
    public CommandTypes getCommand() throws CommandExceptions {
        if (args.length == 0) throw new CommandExceptions(CommandExceptions.Type.NULL);
        try {
            return CommandTypes.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            log.info("Wrong command ", e);
            throw new CommandExceptions(CommandExceptions.Type.WRONG);
        }
    }

    /**
     * Returns a list of arguments.
     * <p>
     * In the input line, the first argument is the command.
     * All that goes after - a list of the parameters of the form: "key" "value"
     * Always go together, separated by a space.
     *
     * @return Returns a Map of arguments <String: key>,<String: value>
     * @throws CommandExceptions arguments are not specified,
     *                           the number of arguments is not even
     *                           keys must begin with a "-"
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
