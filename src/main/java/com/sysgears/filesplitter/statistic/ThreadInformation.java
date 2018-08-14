package com.sysgears.filesplitter.statistic;

import com.sysgears.filesplitter.user.UserInOut;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Save and load statistics about working Threads
 * <p>
 * Can run like Threads and show statistic in realtime
 */
public class ThreadInformation implements Runnable {
    private final TimeController timeController;
    private final UserInOut userInOut;

    /**
     * Concurrent Hash Map for storing thread statistics
     */
    private final Map<String, String> map = new ConcurrentHashMap<String, String>() {
    };

    /**
     * Set time control and user view interface
     *
     * @param timeController - time manager
     * @param userInOut - user view interface
     */
    public ThreadInformation(TimeController timeController, UserInOut userInOut) {
        this.userInOut = userInOut;
        this.timeController = timeController;
    }

    /**
     * Put information about Thread to map.
     *
     * @param thread handler of putting thread
     * @param value  information about thread
     */
    public void put(String thread, String value) {
        map.put(thread, value);
    }

    /**
     * Get map with all statistic. Sorted by Key
     *
     * @return Map, where key - thread name, value - info about this thread
     */
    private Map<String, String> getMap() {
        return new TreeMap<String, String>(map);
    }

    /**
     * Collects statistics about working threads.
     *
     * Data on the progress of the flow is taken from its name
     */
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                if (Thread.interrupted()) return;

                Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
                for (Thread t : threads.keySet()) {
                    String[] s = t.getName().split(":");
                    if (map.containsKey(s[0])) {
                        if (!"done".equals(map.get(s[0]))) map.put(s[0], s[1]);
                    }
                }
                StringBuilder out = new StringBuilder();
                for (Map.Entry<String, String> pair : getMap().entrySet()) {
                    out.append(pair.getKey()).append(":").append(pair.getValue()).append(", ");
                }
                out.append("time remaining:").append(timeController.getRemainingInSec()).append("s");
                userInOut.write(out.toString());
                Thread.yield();

            } catch (InterruptedException e) {
                return;
            }

        }

    }
}
