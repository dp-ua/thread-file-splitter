package com.sysgears.filesplitter.file.executor;

import com.sysgears.filesplitter.statistic.AbstractStatistic;
import com.sysgears.filesplitter.user.UserInOut;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorPool {

    /**
     * user interface
     */
    private final UserInOut userInOut;

    /**
     * Statistic holder
     */
    private final AbstractStatistic statistic;

    public ExecutorPool(UserInOut userInOut, AbstractStatistic statistic) {
        this.userInOut = userInOut;
        this.statistic = statistic;
    }

    public void doTaskList(List<Callable <String>> todoList) {

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
                userInOut.write(e.getMessage());
            } finally {
                executorService.shutdown();
            }

    }
}
