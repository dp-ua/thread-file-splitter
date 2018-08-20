package com.sysgears.filesplitter.statistic;

import com.sysgears.filesplitter.user.UserInOut;

import java.util.Map;

/**
 * Stream demon for statistics collection
 */
public class StatisticViewer implements Runnable {
    /**
     * Time controllee
     */
    private final TimeController timeController;

    /**
     * Statistic holder
     */
    private final AbstractStatistic statistic;

    /**
     * User in out
     */
    private final UserInOut userInOut;

    /**
     * Set time controller, statistic holder and user in\out interfaces
     * @param timeController time controller
     * @param statistic holder
     * @param userInOut in out to user
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
     * considers overall progress
     * displays information on the screen
     */
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                if (Thread.interrupted()) return;
                if (statistic.isDone()) return;
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
                    out.append(pair.getKey()).append(":").append(threadProgress).append("%, ");
                }
                progress /= map.size();
                out.append("time remaining:").append(timeController.getRemainingInSec()).append("s");
                userInOut.write("Total:" + progress + "%, " + out.toString());
                StringBuilder progressBar = new StringBuilder();
                progressBar.append("[");
                String symbol = ":";
                for (int i = 0; i < 100; i++) {
                    progressBar.append(i == progress ? "<" + progress + ">" : symbol);
                }
                progressBar.append("]");
                userInOut.write(progressBar.toString());
                Thread.yield();
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
