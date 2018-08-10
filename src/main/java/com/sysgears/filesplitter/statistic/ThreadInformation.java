package com.sysgears.filesplitter.statistic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Save and load statistics about working Threads
 *
 */
public class ThreadInformation {

    /**
     * Concurent Hash Map for storing thread statistics
     */
    private final Map<String,String> map= new ConcurrentHashMap<String, String>();

    /**
     * Put information about Thread to map.
     *
     * @param threadName unique thread name
     * @param value information about thread
     */
    public void put(String threadName, String value) {
        map.put(threadName,value);
    }

    /**
     * Get map with all statistic
     *
     * @return Map, where key - thread name, value - info about this thread
     */
    public Map<String, String> getMap() {
        return map;
    }
}
