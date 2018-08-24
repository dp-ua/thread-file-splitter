package com.sysgears.filesplitter;


import com.sysgears.filesplitter.command.CommandExceptions;
import com.sysgears.filesplitter.command.CommandParser;
import com.sysgears.filesplitter.command.CommandTypes;
import com.sysgears.filesplitter.file.executor.ExecutorPool;
import com.sysgears.filesplitter.file.operation.AbstractOperation;
import com.sysgears.filesplitter.file.operation.OperationExceptions;
import com.sysgears.filesplitter.file.operation.OperationType;
import com.sysgears.filesplitter.file.operation.OperationsFactory;
import com.sysgears.filesplitter.statistic.ConcurrentMapStatistic;
import com.sysgears.filesplitter.statistic.StatisticViewer;
import com.sysgears.filesplitter.statistic.TimeController;
import com.sysgears.filesplitter.user.ConsoleInOut;
import com.sysgears.filesplitter.user.Messages;


public class Main {

/* строки для тестирования
split -p /home/pavel/IdeaProjects/test/test3.file -s 600M
split -p /home/pavel/IdeaProjects/test/test3.file -c 10
split -p /home/pavel/IdeaProjects/test/photo.jpg -c 10

split -p /home/pavel/IdeaProjects/test/photo.jpg -c 5
merge -p /home/pavel/IdeaProjects/test

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
                    if (commandParser.getCommand() == CommandTypes.THREADS) {messages.showThreads();
                    continue;}


                    TimeController timeController = new TimeController();
                    OperationType type;
                    switch (commandParser.getCommand()) {
                        case SPLIT:
                            type = OperationType.SPLIT;
                            break;
                        case MERGE:
                            type = OperationType.MERGE;
                            break;
                        default:
                            type = null;
                    }

                    ConcurrentMapStatistic statistic = new ConcurrentMapStatistic();

                    Thread statisticDemon = new Thread(new StatisticViewer(timeController, statistic, consoleInOut));
                    statisticDemon.setPriority(8);
                    statisticDemon.setDaemon(true);
                    statisticDemon.start();

                    AbstractOperation operation = new OperationsFactory(consoleInOut, statistic).getOperation(type);

                    ExecutorPool executorPool = new ExecutorPool(consoleInOut, statistic);
                    executorPool.doTaskList(operation.getTaskMap(commandParser.getArguments()));

                    statisticDemon.interrupt();
                    statisticDemon.join();

                    messages.showTimeRemanig(timeController.getRemainingInSec());
                } catch (OperationExceptions | CommandExceptions e) {
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