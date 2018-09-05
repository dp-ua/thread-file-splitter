package com.sysgears.filesplitter.file.operation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Abstract operations for working with specified list of arguments
 */
public interface AbstractOperation {

    /**
     * Get  a list of tasks.
     *
     * @param arguments -that determine which tasks will be listed
     *                  - Arguments are specified in the form of a key, the value
     * @return Returns a list of Callable tasks
     * @throws OperationException Exceptions that occur during the operation
     */
    List<Callable<String>> getTaskMap(Map<String, String> arguments) throws OperationException;
}
