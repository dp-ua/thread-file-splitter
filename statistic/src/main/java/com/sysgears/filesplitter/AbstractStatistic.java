package com.sysgears.filesplitter;

import java.util.Map;

/**
 * Asbract statistics.
 * <p>
 * Information about methods for working with statistics
 */
public interface AbstractStatistic {
    /**
     * Get all elements
     *
     * @return Map<String   ,   String>
     * key - name of thread
     * value - progress
     */
    Map<String, String> getAll();

    /**
     * Put some record to map
     *
     * @param key name of thread
     * @param value progress
     * @return true if operation was successful
     */
    boolean put(String key, String value);

    /**
     * Get value by key
     *
     * @param key name of thread
     * @return value progress of thread
     */
    String get(String key);

    /**
     * Clear map
     */
    void clearAll();

    /**
     * Send a signal that the statistics can be interrupted
     */
    void interupt();

    /**
     * Check if a signal was sent that the monitoring of statistics can be interrupted
     *
     * @return true if flag set true
     */
    boolean isInterupt();
    }
