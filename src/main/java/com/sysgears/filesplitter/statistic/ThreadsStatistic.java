package com.sysgears.filesplitter.statistic;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementing statistics on the base ConcurrentHashMap
 *
 */
public class ThreadsStatistic implements AbstractStatistic {

    /**
     * Map for holding statistic
     */
    private final Map<String, String> map = new ConcurrentHashMap<>();

    /**
     * Trigger used to control the completion of all operations and stop collecting statistics
     */
    private volatile boolean done;

    /**
     * Get done state
     *
     * @return true if done set true
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Set param done
     * @param done boolean
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Returns a list of all values in the map
     *
     * @return TreeMap of all values.
     */
    public Map<String, String> getAll() {
        return new TreeMap<>(map);
    }

    /**
     * Put record to map
     * @param key of record
     * @param value record value
     * @return Returns the true if the operation was successful
     */
    public boolean put(String key, String value) {
        int size = map.size();
        map.put(key, value);
        return (map.containsKey(key))&&(value.equals(map.get(key)));
    }

    /**
     * Get value by key
     * @param key name of thread
     * @return value. If key is not contains - returm null
     */
    public String get(String key) {
        if (map.containsKey(key)) return map.get(key);
        return null;
    }

    /**
     * Clear map and set done to false
     */
    public void clearAll() {
        map.clear();
        done = false;
    }


}
