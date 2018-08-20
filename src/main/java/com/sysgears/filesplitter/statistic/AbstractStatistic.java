package com.sysgears.filesplitter.statistic;

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
     * Get trigger of done
     * @return true if all done
     */
    boolean isDone();

    /**
     * Set triiger done
     * @param done boolean
     */
    void setDone(boolean done);
}
