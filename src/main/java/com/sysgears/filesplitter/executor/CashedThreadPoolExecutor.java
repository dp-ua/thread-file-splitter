package com.sysgears.filesplitter.executor;

import com.sysgears.filesplitter.AbstractStatistic;
import com.sysgears.filesplitter.file.operation.OperationException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Implementation of an Abstract executor on the basis of a CachedThreadPool executor
 */
public class CashedThreadPoolExecutor implements AbstractExecutor{
    private final Logger log = Logger.getLogger(CashedThreadPoolExecutor.class);

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
    public void doTaskList(List<Callable <String>> todoList, AbstractStatistic statistic) throws Exception{
        log.debug("Start Pool with "+todoList.size()+" tasks");
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
                statistic.interupt();
                log.error(e.getMessage(),e);
                throw new OperationException(OperationException.Type.MAINBLOCKERR);
            } finally {
            statistic.interupt();
                executorService.shutdown();
            }
        log.debug("Shutdown Pool with "+todoList.size()+" tasks");
    }
}
