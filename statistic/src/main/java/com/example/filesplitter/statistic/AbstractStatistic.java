package com.example.filesplitter.statistic;

import java.util.Map;

/**
 * Statistic API.
 */
public interface AbstractStatistic {

    /**
     * Get all elements
     *
     * @return Map<String , String>
     * key - name of thread
     * value - progress
     */
    Map<String, String> getAll();

    /**
     * Put some record to statistic
     *
     * @param key   name of thread
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
     * Clear statistic
     */
    void clearAll();
}
