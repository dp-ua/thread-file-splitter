package com.sysgears.filesplitter.statistic;

import java.util.Calendar;

/**
 * Time Controller for project.
 *
 * It is initialized at the time the project starts and calculates the time spent performing
 */
public class TimeController {

    /**
     * Instance time
     */
    private final Calendar startTime;

    /**
     * Set start time when instance object
     */
    public TimeController() {
        startTime = Calendar.getInstance();
    }

    /**
     * Returns time in sec betwen start and now
     *
     * @return distance in seconds from initialization
     */
    public long getRemainingInSec() {
        Calendar now = Calendar.getInstance();
        return (now.getTimeInMillis()-startTime.getTimeInMillis())/1000;
    }
}
