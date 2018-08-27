package com.sysgears.filesplitter.executor;

import com.sysgears.filesplitter.statistic.AbstractStatistic;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Abstract pool executor
 */
public interface AbstractExecutor {
    void doTaskList(List<Callable<String>> todoList, AbstractStatistic statistic);
}
