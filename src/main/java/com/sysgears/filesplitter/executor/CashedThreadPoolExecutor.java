package com.sysgears.filesplitter.executor;

import com.sysgears.filesplitter.statistic.AbstractStatistic;
import com.sysgears.filesplitter.user.UserInOut;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Implementation of an Abstract executor on the basis of a CachedThreadPool executor
 */
public class CashedThreadPoolExecutor implements AbstractExecutor{

    public CashedThreadPoolExecutor() {
    }

    /**
     * Accepts a list of tasks that must return their result as a string.
     * Executes them and records the returned results of the work flows.
     *
     * @param todoList List of tasks that the result of the execution
     *                 is returned in the form Callable<String>
     * @param statistic A pointer to the statistics used.
     */
    public void doTaskList(List<Callable <String>> todoList, AbstractStatistic statistic) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<String>> result = new ArrayList<>();

        for (Callable<String> b : todoList) {
            result.add(executorService.submit(b));
        }
        for (Future<String> fs : result)
            try {
                String[] s = fs.get().split(":");
                statistic.put(s[0], s[1]);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }

    }
}
