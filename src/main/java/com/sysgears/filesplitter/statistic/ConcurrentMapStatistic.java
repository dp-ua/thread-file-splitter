package com.sysgears.filesplitter.statistic;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of statistics based on ConcurrentHashMap
 */
public class ConcurrentMapStatistic implements AbstractStatistic {

    /**
     * Map for holding statistic
     */
    private final Map<String, String> map = new ConcurrentHashMap<>();

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
     *
     * @param key   of record
     * @param value record value
     * @return Returns the true if the operation was successful
     */
    public boolean put(String key, String value) {
        int size = map.size();
        map.put(key, value);
        return (size > map.size());
    }

    /**
     * Get value by key
     *
     * @param key name of thread
     * @return value. If key is not contains - returm null
     */
    public String get(String key) {
        if (map.containsKey(key)) return map.get(key);
        return null;

    }

    /**
     * Clear statistic map
     */
    public void clearAll() {
        map.clear();
    }
}
