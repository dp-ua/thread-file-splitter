package com.sysgears.filesplitter.command;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    String[] args;

    public CommandParser(String args) {
        this.args = args.split(" ");
    }


    public CommandType getCommand() {
        if (args.length==0) return CommandType.BLANK;

        try {
        CommandType type = CommandType.valueOf(args[0].toUpperCase());
        return type;
        }catch (IllegalArgumentException e) {
            return CommandType.ERROR;
        }
    }

    public Map<String,String> getArguments() throws NoSuchFieldException,IllegalArgumentException {
        Map <String, String> map = new HashMap<String, String>();
        if(args.length<=1) throw new NoSuchFieldException("Отсутствуют аргументы");
        if ((args.length-1)%2!=0)  throw new IllegalArgumentException("Неверно заданы аргументы");

        for (int i = 1; i < args.length; i=i+2) {
            if (args[i].charAt(0)!='-') throw new IllegalArgumentException("Неверно заданы аргументы");
            map.put(args[i],args[i+1]);

        }
        return map;
    }
}
