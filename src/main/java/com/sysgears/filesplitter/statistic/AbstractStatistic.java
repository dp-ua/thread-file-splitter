package com.sysgears.filesplitter.statistic;

import java.util.Map;

/**
 * Asbract statistics.
 *
 * Information about methods for working with statistics
 */
public interface AbstractStatistic {
    Map<String,String> getAll();
    boolean put(String key, String value);
    String get(String key);
    void clearAll();
    boolean isDone();
    void setDone(boolean done);
}
