package com.sysgears.filesplitter.executor;

import com.sysgears.filesplitter.AbstractStatistic;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Abstract pool executor
 */
public interface AbstractExecutor {

    /**
     * Takes a list of tasks that implement Callable interface and return the result in String
     *
     * Processes the task list and returns the result to the statistics
     *
     * @param todoList  list of Callable<String> tasks
     * @param statistic Statistic holder
     */
    void doTaskList(List<Callable<String>> todoList, AbstractStatistic statistic) throws Exception;
}
