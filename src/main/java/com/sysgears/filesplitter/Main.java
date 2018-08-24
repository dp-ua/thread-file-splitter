package com.sysgears.filesplitter;


import com.sysgears.filesplitter.Exception.SplitterExceptions;
import com.sysgears.filesplitter.command.CommandParser;
import com.sysgears.filesplitter.command.CommandTypes;
import com.sysgears.filesplitter.file.FileOperations;
import com.sysgears.filesplitter.statistic.StatisticInConcurrentMap;
import com.sysgears.filesplitter.statistic.StatisticViewer;
import com.sysgears.filesplitter.statistic.TimeController;
import com.sysgears.filesplitter.user.ConsoleInOut;
import com.sysgears.filesplitter.user.Messages;


public class Main {

/* строки для тестирования
split -p /home/pavel/IdeaProjects/test/test3.file -s 600M
split -p /home/pavel/IdeaProjects/test/test3.file -c 10
split -p /home/pavel/IdeaProjects/test/photo.jpg -c 10

merge -p /home/pavel/IdeaProjects/test/merging
*/

    public static void main(String[] args) {
        try {
            ConsoleInOut consoleInOut = new ConsoleInOut();
            CommandParser commandParser = new CommandParser();
            Messages messages = new Messages(consoleInOut);


            while (true) {
                try {
                    messages.showInput();
                    String input = consoleInOut.read().trim();
                    commandParser.setArgs(input);

                    if (commandParser.getCommand() == CommandTypes.EXIT) break;

                    TimeController timeController = new TimeController();
                    StatisticInConcurrentMap statistic = new StatisticInConcurrentMap();

                    Thread thread = new Thread(new StatisticViewer(timeController, statistic, consoleInOut));

                    switch (commandParser.getCommand()) {
                        case SPLIT:
                            thread.setPriority(8);
                            thread.setDaemon(true);
                            thread.start();

                            new FileOperations(consoleInOut, statistic).split(commandParser.getArguments());
                            thread.interrupt();
                            thread.join();
                            messages.showTimeRemanig(timeController.getRemainingInSec());
                            break;
                        case MERGE:
                            thread.setPriority(8);
                            thread.setDaemon(true);
                            thread.start();

                            new FileOperations(consoleInOut, statistic).merge(commandParser.getArguments());
                            thread.interrupt();
                            thread.join();
                            messages.showTimeRemanig(timeController.getRemainingInSec());
                            break;
                    }


                    switch (commandParser.getCommand()) {
                        case THREADS:
                            messages.showThreads();
                            break;
                        case EXIT:
                        case BLANK:
                        case ERROR:
                    }

                } catch (SplitterExceptions e) {
                    messages.showError(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (
                Throwable e) {
            e.printStackTrace();
        }
    }
}