package com.sysgears.filesplitter.file.operation;

import com.sysgears.filesplitter.file.operation.type.Merging;
import com.sysgears.filesplitter.file.operation.type.OperationType;
import com.sysgears.filesplitter.file.operation.type.Splitting;
import com.sysgears.filesplitter.statistic.AbstractStatistic;
import com.sysgears.filesplitter.user.UserInOut;

/**
 * Factory operations. Defines which class will be responsible for performing the specified operation
 */
public class OperationsFactory {

    private final UserInOut userInOut;
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

    /**
     * Get the implementation responsible for performing the required operation
     *
     * @param type of operation
     * @return the necessary operation
     */
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