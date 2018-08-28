package com.sysgears.filesplitter;


import com.sysgears.filesplitter.command.CommandExceptions;
import com.sysgears.filesplitter.command.CommandParser;
import com.sysgears.filesplitter.command.CommandTypes;
import com.sysgears.filesplitter.executor.AbstractExecutor;
import com.sysgears.filesplitter.executor.CashedThreadPoolExecutor;
import com.sysgears.filesplitter.file.operation.AbstractOperation;
import com.sysgears.filesplitter.file.operation.OperationsFactory;
import com.sysgears.filesplitter.file.operation.exception.OperationExceptions;
import com.sysgears.filesplitter.file.operation.type.OperationType;
import com.sysgears.filesplitter.statistic.ConcurrentMapStatistic;
import com.sysgears.filesplitter.statistic.StatisticViewer;
import com.sysgears.filesplitter.statistic.TimeController;
import com.sysgears.filesplitter.user.ConsoleInOut;
import com.sysgears.filesplitter.user.Messages;
import com.sysgears.filesplitter.user.UserInOut;
import org.apache.log4j.Logger;

import static com.sysgears.filesplitter.file.operation.type.OperationType.SPLIT;


public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

/* строки для тестирования
split -p /home/pavel/IdeaProjects/test/test3.file -s 600M
split -p /home/pavel/IdeaProjects/test/test3.file -c 10
split -p /home/pavel/IdeaProjects/test/photo.jpg -c 10

split -p /home/pavel/IdeaProjects/test/photo.jpg -c 5
merge -p /home/pavel/IdeaProjects/test

*/

    public static void main(String[] args) {
        try {
            UserInOut consoleInOut = new ConsoleInOut();
            CommandParser commandParser = new CommandParser();

            Messages messages = new Messages(consoleInOut);
            AbstractExecutor executor = new CashedThreadPoolExecutor();

            while (true) {
                try {
                    messages.showInput();
                    String input = consoleInOut.read().trim();
                    commandParser.setArgs(input);

                    if (commandParser.getCommand() == CommandTypes.EXIT) break;
                    if (commandParser.getCommand() == CommandTypes.THREADS) {
                        messages.showThreads();
                        continue;
                    }
                    TimeController timeController = new TimeController();
                    OperationType type;
                    switch (commandParser.getCommand()) {
                        case SPLIT:
                            type = SPLIT;
                            break;
                        case MERGE:
                            type = OperationType.MERGE;
                            break;
                        default:
                            type = null;
                    }

                    ConcurrentMapStatistic statistic = new ConcurrentMapStatistic();

                    Thread statisticViwer = new Thread(new StatisticViewer(timeController, statistic, consoleInOut));
                    statisticViwer.setPriority(8);
                    statisticViwer.setDaemon(true);
                    statisticViwer.start();

                    AbstractOperation operation = new OperationsFactory(consoleInOut, statistic).getOperation(type);
                    executor.doTaskList(operation.getTaskMap(commandParser.getArguments()), statistic);

                    statisticViwer.interrupt();
                    statisticViwer.join();

                    messages.showTimeRemanig(timeController.getRemainingInSec());
                } catch (OperationExceptions | CommandExceptions e) {
                    messages.showError(e.getMessage());
                    log.info(e.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        } catch (
                Throwable e) {
            log.fatal(e.getMessage(), e);
        }
    }
}