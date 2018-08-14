package com.sysgears.filesplitter;

import com.sysgears.filesplitter.command.BlockCount;
import com.sysgears.filesplitter.command.CommandParser;
import com.sysgears.filesplitter.command.CommandType;
import com.sysgears.filesplitter.command.BlockSize;
import com.sysgears.filesplitter.file.FileWriter;
import com.sysgears.filesplitter.statistic.ThreadInformation;
import com.sysgears.filesplitter.statistic.TimeController;
import com.sysgears.filesplitter.user.ConsoleInOut;
import com.sysgears.filesplitter.user.UserView;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        try {
            ConsoleInOut consoleInOut = new ConsoleInOut();
            UserView userView = new UserView(consoleInOut);

            while (true) try {
                userView.send("Input command:");
                String input = userView.get().trim();
                CommandParser commandParser = new CommandParser(input);
                if (commandParser.getCommand() == CommandType.EXIT) break;
                if (commandParser.getCommand() == CommandType.THREADS) {
                    userView.send("Потоков: " + Thread.getAllStackTraces().size());
                    for (Thread t : Thread.getAllStackTraces().keySet())
                        userView.send(t.toString());
                }
                if (commandParser.getCommand() == CommandType.SPLIT) {
                    Map<String, String> arguments = commandParser.getArguments();
                    if (!arguments.containsKey("-p") | !arguments.containsKey("-s"))
                        throw new IllegalArgumentException("Wrong arguments");

                    BlockSize blockSize = new BlockSize(arguments.get("-s"));
                    String sourceFileName = arguments.get("-p");
                    FileChannel inChannel;

                    try {
                        FileInputStream inputStream = new FileInputStream(sourceFileName);
                        inChannel = inputStream.getChannel();
                    } catch (FileNotFoundException e) {
                        throw new FileNotFoundException("Source file does not exist");
                    }

                    if (inChannel.size() < blockSize.getSize()) {
                        userView.send("The splitting is impossible. The block size is larger than the file size. ");
                        continue;
                    }
                    BlockCount blockCount = new BlockCount(blockSize.getSize(), inChannel.size());
                    inChannel.close();

                    /* строки для тестирования
                    split -p /home/pavel/IdeaProjects/test/test.file -s 200M
                    split -p /home/pavel/IdeaProjects/test/test3.file -s 600M
                    */

                    TimeController timeController = new TimeController();
                    ThreadInformation threadInformation = new ThreadInformation(timeController, consoleInOut);
                    Thread statistic = new Thread(threadInformation);
                    statistic.setPriority(8);
                    statistic.setDaemon(true);
                    statistic.start();

                    ExecutorService executorService = Executors.newFixedThreadPool(4);
                    ArrayList<Future<String>> result = new ArrayList<Future<String>>();

                    for (int i = 0; i < blockCount.getCount(); i++) {
                        String partName = String.format("%0" + blockCount.getDimension() + "d", i + 1);
                        String threadName = "Thread-" + partName;

                        threadInformation.put(threadName, "start");
                        File inputFile = new File(sourceFileName);
                        File outputFile = new File(sourceFileName + partName);
                        result.add(executorService.submit(new FileWriter(inputFile, outputFile, blockSize.getSize() * i, blockSize.getSize(), threadName,false)));
                    }
                    for (Future<String> fs : result)
                        try {
                            String[] s = fs.get().split(":");
                            threadInformation.put(s[0], s[1]);

                        } catch (InterruptedException e) {
                            userView.send(e.getMessage());
                        } catch (ExecutionException e) {
                            userView.send(e.getMessage());
                        } finally {
                            executorService.shutdown();
                        }
                    statistic.interrupt();

                    userView.send("Splitting complete, parts:"+ blockCount.getCount()+", time remaining: " + timeController.getRemainingInSec() + "s");
                    userView.send("Files name: "+sourceFileName+"["+String.format("%0" + blockCount.getDimension() + "d-%0" + blockCount.getDimension() + "d]",1,blockCount.getCount()));
                    inChannel.close();
                } else userView.send("Command not recognized");

            } catch (FileNotFoundException e) {
                userView.send("Error: " + e.getMessage());
            } catch (NoSuchFieldException e) {
                userView.send("Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                userView.send("Error: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
