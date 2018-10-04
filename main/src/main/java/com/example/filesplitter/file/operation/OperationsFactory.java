package com.example.filesplitter.file.operation;

import com.example.filesplitter.statistic.AbstractStatistic;
import com.example.filesplitter.user.UserInOut;
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
    public AbstractOperation getOperation(OperationType type) throws OperationException {
        switch (type) {
            case SPLIT:
        return new Splitting(userInOut, statistic);
            case MERGE:
        return new Merging(userInOut, statistic);
            default:
                log.error("Wrong operation type" + type);
                throw new OperationException(OperationException.Type.WRONGOPERATION);
        }
    }
}