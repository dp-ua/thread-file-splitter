package com.sysgears.filesplitter.command;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class uCommandParserTest {

    @org.testng.annotations.Test
    public void commandParseTestSplit() throws CommandException {
        CommandParser commandParser = new CommandParser();
        commandParser.setArgs("split");
        assertEquals(commandParser.getCommand(),CommandType.SPLIT);

    }

    @org.testng.annotations.Test
    public void commandParseArgumentsTest() throws CommandException {
        CommandParser commandParser = new CommandParser();
        commandParser.setArgs("split -p file/file");
        Map<String,String> map = new HashMap<>();
        map.put("-p","file/file");
        assertEquals(commandParser.getArguments(),map);
}

    @org.testng.annotations.Test (expectedExceptions = CommandException.class)
    public void commandParseError() throws CommandException {
        CommandParser commandParser = new CommandParser();
        commandParser.setArgs("splet -p file/file");
        assertEquals(commandParser.getCommand(),CommandType.THREADS);
    }
}