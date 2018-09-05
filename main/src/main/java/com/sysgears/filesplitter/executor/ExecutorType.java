package com.sysgears.filesplitter.executor;

import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Type of available executors
 */
public class ExecutorType {
    private final Logger log = Logger.getLogger(ExecutorType.class);

    /**
     * The multiplier associated with the number of processors
     */
    private final int poolMultiplier = 1;

    /**
     * Number of processors
     */
    private final int coresNumber = 4;

    /**
     * Types of Available Pools
     */
    public enum Type {
        CASHED, FIXED, SINGLE
    }

    /**
     * Returns the executor according to the specified type.
     *
     * @param type of requied executor
     * @return ExecutorService. If type not specified - returns newSingleThreadExecutor
     */
    public ExecutorService getExecutorService(Type type) {
        if (log.isTraceEnabled()) log.trace(
                "type: " + type +
                "poolMultiplier: " + poolMultiplier +
                " coresNumber:" + coresNumber);
        switch (type) {
            case FIXED:
                log.debug("Fixed pool select. coresNumber:" + coresNumber + "threads count: " + coresNumber * poolMultiplier);
                return Executors.newFixedThreadPool(coresNumber * poolMultiplier);
            case CASHED:
                log.debug("Cached pool selected");
                return Executors.newCachedThreadPool();
            case SINGLE:
            default:
                log.debug("Single Thread pool selected");
                return Executors.newSingleThreadExecutor();
        }
    }

}
