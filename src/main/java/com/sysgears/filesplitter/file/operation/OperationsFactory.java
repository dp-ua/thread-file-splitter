package com.sysgears.filesplitter.file.operation;

import com.sysgears.filesplitter.statistic.AbstractStatistic;
import com.sysgears.filesplitter.user.UserInOut;

public class OperationsFactory {
    /**
     * user interface
     */
    private final UserInOut userInOut;

    /**
     * Statistic holder
     */
    private final AbstractStatistic statistic;

    /**
     * Set user interface and statistic holder
     *
     * @param userInOut user interface
     * @param statistic statistic holder
     */
    public OperationsFactory(UserInOut userInOut, AbstractStatistic statistic) {
        this.userInOut = userInOut;
        this.statistic = statistic;
    }


    public AbstractOperation getOperation(OperationType type) {
        switch (type) {
            case SPLIT:
                return new Splitting(userInOut, statistic);
            case MERGE:
                return new Merging(userInOut, statistic);
            default:
                return null;
        }


    }
}