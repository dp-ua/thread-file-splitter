package com.sysgears.filesplitter.command;

import org.apache.log4j.Logger;

import java.util.Arrays;
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
     * Get the type of command
     * <p>
     * By default, the command must be specified by the first element in the argument list.
     * All that goes further is the arguments of the team.
     *
     * @return type of command in CommandType. If cant parsing command - return CommandType.ERROR
     */
    public CommandType getCommand() throws CommandException {
        log.debug("Try to parse command: " + this.toString());
        if (args.length == 0) throw new CommandException(CommandException.Type.NULL);
        try {
            CommandType result = CommandType.valueOf(args[0].toUpperCase());
            log.info("Command successfully parsed: " + result.toString() + " " + this.toString());
            return result;
        } catch (IllegalArgumentException e) {
            log.info("Wrong command entered: " + this.toString());
            throw new CommandException(CommandException.Type.WRONG);
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
     * @throws CommandException arguments are not specified,
     *                          the number of arguments is not even
     *                          keys must begin with a "-"
     */
    public Map<String, String> getArguments() throws CommandException {
        Map<String, String> map = new HashMap<>();
        try {
            if (args.length <= 1) {
                log.error("Error: wrong arguments: "+ this.toString() );
                throw new CommandException(CommandException.Type.WRONGARG);
            }
            if ((args.length - 1) % 2 != 0) {
                log.error("Error: wrong arguments: "+ this.toString() );
                throw new CommandException(CommandException.Type.WRONGARG);
            }

            for (int i = 1; i < args.length; i = i + 2) {
                if (args[i].charAt(0) != '-') {
                    log.error("Error: wrong arguments: "+ this.toString() );
                    throw new CommandException(CommandException.Type.WRONGARG);
                }
                map.put(args[i], args[i + 1]);
            }
        } catch (CommandException e) {
            log.debug(e.getMessage());
            throw e;
        }

        return map;
    }

    @Override
    public String toString() {
        return "CommandParser{" +
                "args=" + Arrays.toString(args) +
                '}';
    }
}
