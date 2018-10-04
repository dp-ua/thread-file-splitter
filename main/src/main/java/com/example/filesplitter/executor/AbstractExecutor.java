package com.example.filesplitter.executor;

import com.example.filesplitter.statistic.AbstractStatistic;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Abstract executor
 */
public interface AbstractExecutor {

    /**
     * Do some operations with List of tasks. Put result of operations to statistic
     *
     * @param todoList  list of Callable<String> tasks
     * @param statistic Statistic holder
     */
    void doTaskList(List<Callable<String>> todoList, AbstractStatistic statistic) throws Exception;
}
