package com.sysgears.filesplitter.file.operation;

import com.sysgears.filesplitter.AbstractStatistic;
import com.sysgears.filesplitter.user.UserInOut;
import org.apache.log4j.Logger;

/**
 * Factory operations. Defines which class will be responsible for performing the specified operation
 */
public class OperationsFactory {
    private static final Logger log = Logger.getLogger(OperationsFactory.class);

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
        log.debug("Factory tries to detect operation by type: " + type.toString());
        switch (type) {
            case SPLIT:
                log.debug("Factory returns Splitting operation");
                return new Splitting(userInOut, statistic);
            case MERGE:
                log.debug("Factory returns Merging operation");
                return new Merging(userInOut, statistic);
            default:
                log.debug("The factory did not find the necessary operation");
                statistic.interupt();
                return null;
        }
    }
}