package com.example.filesplitter.command;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class CommandParserTest {

    @Test
    public void commandParseTestSplit() throws CommandException {
        CommandParser commandParser = new CommandParser();
        commandParser.setArgs("split");
        assertEquals(commandParser.getCommand(),CommandType.SPLIT);

    }

    @Test
    public void commandParseArgumentsTest() throws CommandException {
        CommandParser commandParser = new CommandParser();
        commandParser.setArgs("split -p file/file");
        Map<String,String> map = new HashMap<>();
        map.put("-p","file/file");
        assertEquals(commandParser.getArguments(),map);
}

    @Test (expectedExceptions = CommandException.class)
    public void commandParseError() throws CommandException {
        CommandParser commandParser = new CommandParser();
        commandParser.setArgs("splet -p file/file");
        assertEquals(commandParser.getCommand(),CommandType.THREADS);
    }
}