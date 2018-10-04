package com.example.filesplitter;

import com.example.filesplitter.command.CommandException;
import com.example.filesplitter.command.CommandType;
import com.example.filesplitter.executor.AbstractExecutor;
import com.example.filesplitter.executor.ExecutorType;
import com.example.filesplitter.executor.ThreadExecutor;
import com.example.filesplitter.file.operation.OperationsFactory;
import com.example.filesplitter.statistic.ConcurrentMapStatistic;
import com.example.filesplitter.statistic.TimeController;
import com.example.filesplitter.user.Messages;
import com.example.filesplitter.user.UserInOut;
import com.example.filesplitter.command.CommandParser;
import com.example.filesplitter.file.operation.AbstractOperation;
import com.example.filesplitter.file.operation.OperationException;
import com.example.filesplitter.file.operation.OperationType;
import com.example.filesplitter.statistic.StatisticViewer;
import com.example.filesplitter.user.ConsoleInOut;
import org.apache.log4j.Logger;

import java.util.Map;


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
            UserInOut userInOut = new ConsoleInOut();
            CommandParser commandParser = new CommandParser();
            CommandType commandType;

            Messages messages = new Messages(userInOut);
            AbstractExecutor executor = new ThreadExecutor(ExecutorType.Type.FIXED);

            Thread statisticViwer = null;

            while (true) {
                try {
                    messages.showInput();
                    String input = userInOut.read().trim();
                    commandParser.setArgs(input);
                    commandType = commandParser.getCommand();

                    if (commandType == CommandType.EXIT) break;
                    if (commandType == CommandType.THREADS) {
                        messages.showThreads();
                        continue;
                    }
                    TimeController timeController = new TimeController();
                    OperationType type;
                    switch (commandType) {
                        case SPLIT:
                            type = OperationType.SPLIT;
                            break;
                        case MERGE:
                            type = OperationType.MERGE;
                            break;
                        default:
                            type = null;
                    }

                    Map<String, String> commandArgs = commandParser.getArguments();

                    ConcurrentMapStatistic statistic = new ConcurrentMapStatistic();

                    AbstractOperation operation = new OperationsFactory(userInOut, statistic).getOperation(type);

                    statisticViwer = new Thread(new StatisticViewer(timeController, statistic, userInOut));
                    statisticViwer.setPriority(8);
                    statisticViwer.setDaemon(true);
                    statisticViwer.start();

                    executor.doTaskList(operation.getTaskMap(commandArgs), statistic);

                    statisticViwer.interrupt();
                    statisticViwer.join();

                    messages.showTimeRemanig(timeController.getRemainingInSec());
                } catch (OperationException | CommandException e) {
                    log.info(e.getMessage());
                    messages.showError(e.getMessage());
                } catch (Exception e) {
                    messages.showError(e.getMessage());
                    log.error(e.getMessage(), e);
                } finally {
                    if (statisticViwer != null) statisticViwer.interrupt();
                }
            }
        } catch (Throwable e) {
            log.fatal(e.getMessage(), e);
        }
    }
}