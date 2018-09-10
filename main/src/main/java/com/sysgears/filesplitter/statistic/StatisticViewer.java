package com.sysgears.filesplitter.statistic;

import com.sysgears.filesplitter.user.UserInOut;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Streaming statistics viewer
 * <p>
 * Reads data from data storage
 * Calculates the progress of each element and displays it on the screen.
 * To work with time uses a special class:
 *
 * @see TimeController
 * All information is displayed via the user interface:
 * @see UserInOut
 */
public class StatisticViewer implements Runnable {
    private static final Logger log = Logger.getLogger(StatisticViewer.class);

    private final long UPDATETIME = 1000;
    private final String NAME = String.valueOf(this.hashCode()) + "-" + System.currentTimeMillis();
    private final Map<String, Integer> PROGRESSWORDS;

    {
        PROGRESSWORDS = new HashMap<>();
        PROGRESSWORDS.put("done", 100);
        PROGRESSWORDS.put("start", 0);
    }

    private final int DISPLAYTYPE = 1;
    private final TimeController timeController;
    private final AbstractStatistic statistic;
    private final UserInOut userInOut;

    /**
     * Sets the basic interfaces for work.
     *
     * @param timeController Monitoring the execution time. Contains information
     *                       about the beginning of the operation and returns
     *                       the difference with the time at the moment
     * @param statistic      Statistic holder
     * @param userInOut      InOut user interface
     */
    public StatisticViewer(TimeController timeController, AbstractStatistic statistic, UserInOut userInOut) {
        this.timeController = timeController;
        this.statistic = statistic;
        this.userInOut = userInOut;
    }

    /**
     * The main flow for collecting and displaying statistics
     * <p>
     * Gets information about the progress of all the processes listed in the map
     * Information about the progress of statistics can contain several states:
     * number from 0 to 100 - percentage of completion
     * word "start" - the thread started, but has not yet received data for work
     * word "done" - process has completed the task
     */
    public void run() {
        String infoAboutThread = "Name: " + Thread.currentThread().getName() + "(" + NAME + ")" +
                " updateTime: " + UPDATETIME +
                " statisticHolder: " + statistic.hashCode()+ "-" + System.currentTimeMillis();

        log.info("Start statistic demon: " + infoAboutThread + " ");
        while (true) {
            try {
                Thread.sleep(UPDATETIME);
                if (Thread.interrupted())
                    throw new InterruptedException(Thread.currentThread().getName() + " interrupted");

                Map<String, String> map = statistic.getAll();

                int progress = 0;
                StringBuilder out = new StringBuilder();
                if (map.size() == 0) continue;
                for (Map.Entry<String, String> pair : map.entrySet()) {
                    int threadProgress;

                    if (PROGRESSWORDS.containsKey(pair.getValue())) threadProgress = PROGRESSWORDS.get(pair.getValue());
                    else try {
                        threadProgress = Integer.parseInt(pair.getValue());
                    } catch (Exception e) {
                        threadProgress = 0;
                    }
                    progress += threadProgress;
                    out
                            .append(pair.getKey())
                            .append(":")
                            .append(threadProgress)
                            .append("%, ");
                }
                progress /= map.size();
                out
                        .append("time remaining:")
                        .append(timeController.getRemainingInSec())
                        .append("s");

                StringBuilder progressBar = new StringBuilder();
                progressBar
                        .append("[");
                String symbol = ":";
                for (int i = 0; i < 100; i++) {
                    progressBar
                            .append(i == progress ? "<" + progress + ">" : symbol);
                }
                progressBar.append("]");

                if (DISPLAYTYPE == 1) userInOut.write("Total:" + progress + "%, " + out.toString());
                else if (DISPLAYTYPE == 2) userInOut.write(progressBar.toString());
                else {
                    userInOut.write("Total:" + progress + "%, " + out.toString());
                    userInOut.write(progressBar.toString());
                }

                Thread.yield();
            } catch (InterruptedException e) {
                log.info("Interrupted statistic demon: " + infoAboutThread);
                return;
            }
        }
    }
}
