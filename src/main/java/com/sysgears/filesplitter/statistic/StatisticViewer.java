package com.sysgears.filesplitter.statistic;

import com.sysgears.filesplitter.user.UserInOut;

import java.util.Map;

/**
 * Streaming statistics viewer
 *
 * Reads data from data storage
 * Calculates the progress of each element and displays it on the screen.
 * To work with time uses a special class:
 * @see TimeController
 * All information is displayed via the user interface:
 * @see UserInOut
 */
public class StatisticViewer implements Runnable {

    private final TimeController timeController;
    private final AbstractStatistic statistic;
    private final UserInOut userInOut;

    /**
     * Sets the basic interfaces for work.
     *
     * @param timeController Monitoring the execution time. Contains information
     *                       about the beginning of the operation and returns
     *                       the difference with the time at the moment
     * @param statistic Statistic holder
     * @param userInOut InOut user interface
     */
    public StatisticViewer(TimeController timeController, AbstractStatistic statistic, UserInOut userInOut) {
        this.timeController = timeController;
        this.statistic = statistic;
        this.userInOut = userInOut;
    }

    /**
     * The main flow for collecting and displaying statistics
     *
     * Gets information about the progress of all the processes listed in the map
     * Information about the progress of statistics can contain several states:
     * number from 0 to 100 - percentage of completion
     * word "start" - the process started, but it has not started yet
     * word "done" - process has completed the task
     */
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                if (Thread.interrupted()) return;
                Map<String, String> map = statistic.getAll();

                int progress = 0;
                StringBuilder out = new StringBuilder();
                if (map.size() == 0) continue;
                for (Map.Entry<String, String> pair : map.entrySet()) {
                    int threadProgress;

                    if ("done".equals(pair.getValue())) threadProgress = 100;
                    else if ("start".equals(pair.getValue())) threadProgress = 0;
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

                //userInOut.write("Total:" + progress + "%, " + out.toString());
                userInOut.write(progressBar.toString());

                Thread.yield();
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
