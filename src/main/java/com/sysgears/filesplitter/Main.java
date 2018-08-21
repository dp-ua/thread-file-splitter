package com.sysgears.filesplitter;


import com.sysgears.filesplitter.Exception.MyException;
import com.sysgears.filesplitter.command.CommandParser;
import com.sysgears.filesplitter.command.CommandType;
import com.sysgears.filesplitter.file.FileSplitter;
import com.sysgears.filesplitter.statistic.StatisticViewer;
import com.sysgears.filesplitter.statistic.ThreadsStatistic;
import com.sysgears.filesplitter.statistic.TimeController;
import com.sysgears.filesplitter.user.ConsoleInOut;
import com.sysgears.filesplitter.user.Messages;


public class Main {

/* строки для тестирования
split -p /home/pavel/IdeaProjects/test/test.file -s 200M
split -p /home/pavel/IdeaProjects/test/test3.file -s 600M
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

                    if (commandParser.getCommand() == CommandType.EXIT) break;

                    switch (commandParser.getCommand()) {
                        case SPLIT:
                            TimeController timeController = new TimeController();
                            ThreadsStatistic statistic = new ThreadsStatistic();

                            Thread thread = new Thread(new StatisticViewer(timeController, statistic, consoleInOut));
                            thread.setPriority(8);
                            thread.setDaemon(true);
                            thread.start();

                            FileSplitter fileSplitter = new FileSplitter(consoleInOut, statistic);
                            fileSplitter.Split(commandParser.getArguments(), 2);

                            thread.interrupt();
                            thread.join();
                            messages.showTimeRemanig(timeController.getRemainingInSec());
                            break;
                        case THREADS:
                            messages.showThreads();
                            break;
                        case EXIT:
                        case BLANK:
                        case ERROR:
                        default:
                            messages.showCommandWrong();
                    }
                } catch (MyException e) {
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
