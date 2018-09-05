package com.sysgears.filesplitter.executor;

import com.sysgears.filesplitter.file.operation.OperationException;
import com.sysgears.filesplitter.statistic.AbstractStatistic;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Implementation of an Abstract executor on the basis of aThreadPool executor
 */
public class ThreadExecutor implements AbstractExecutor {
    private final Logger log = Logger.getLogger(ThreadExecutor.class);
    private final ExecutorType.Type type;

    public ThreadExecutor(ExecutorType.Type type) {
        this.type = type;
    }

    /**
     * Places the tasks in the Executorpool and waits for them to complete.
     * The results of their work put to statistic.
     *
     * @param todoList  List of tasks that the result of the execution
     *                  is returned in the form Callable<String>
     * @param statistic Statistic handler
     */
    public void doTaskList(List<Callable<String>> todoList, AbstractStatistic statistic) throws Exception {
        log.info("Start Pool with " + todoList.size() + " tasks");
        if (log.isTraceEnabled()) log.trace("Pool type: " + type);
        ExecutorService executorService = new ExecutorType().getExecutorService(type);
        ArrayList<Future<String>> result = new ArrayList<>();
        for (Callable<String> b : todoList) {
            result.add(executorService.submit(b));
        }
        boolean error = false;
        for (Future<String> fs : result)
            try {
                String[] s = fs.get().split(":");
                statistic.put(s[0], s[1]);
                if (log.isTraceEnabled()) log.trace("Task " + s[0] + " ended. result: " + s[1]);
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
                error = true;
            } finally {
                executorService.shutdown();
            }
        log.info("Shutdown Pool with " + todoList.size() + " tasks");
        if (error) throw new OperationException(OperationException.Type.MAINBLOCKERR);

    }
}
