package com.sysgears.filesplitter.file.operation.type;

import com.sysgears.filesplitter.file.BlockInfo;
import com.sysgears.filesplitter.file.OpportunityChecker;
import com.sysgears.filesplitter.file.data.copy.StreamDataCopying;
import com.sysgears.filesplitter.file.operation.AbstractOperation;
import com.sysgears.filesplitter.file.operation.exception.OperationExceptions;
import com.sysgears.filesplitter.statistic.AbstractStatistic;
import com.sysgears.filesplitter.user.UserInOut;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * The implementation of an abstract operation that performs splitting file
 */
public class Splitting implements AbstractOperation {
    private String partDelimeter = "Part";
    private final UserInOut userInOut;
    private final AbstractStatistic statistic;

    /**
     * Set user interface and statistic holder
     *
     * @param userInOut user interface
     * @param statistic statistic holder
     */
    public Splitting(UserInOut userInOut, AbstractStatistic statistic) {
        this.userInOut = userInOut;
        this.statistic = statistic;
    }

    /**
     * Get  a list of tasks, the result of which will be to split the source file into a specified number of parts
     *
     * @param arguments -that determine which tasks will be listed
     *                  - Arguments are specified in the form of a key, the value
     * @return list of operations
     * @throws OperationExceptions An exception related to checking for the availability of the source file,
     *                             the possibility of separation.
     */
    @Override
    public List<Callable<String>> getTaskMap(Map<String, String> arguments) throws OperationExceptions {

        if (!arguments.containsKey("-p"))
            throw new OperationExceptions(OperationExceptions.Type.WRONGARG);
        String sourceFileName = arguments.get("-p");
        BlockInfo blockInfo = new BlockInfo();
        OpportunityChecker checker = new OpportunityChecker();
        File sourceFile = checker.fileSuitable(sourceFileName);

        long blockSize;
        long fullSize = sourceFile.length();
        if (arguments.containsKey("-s")) {
            blockSize = blockInfo.parseSize(arguments.get("-s"));
        } else if (arguments.containsKey("-c")) {
            long count = blockInfo.parseSize(arguments.get("-c"));
            blockSize = fullSize / count;
            blockSize += fullSize % count == 0 ? 0 : 1;
        } else throw new OperationExceptions(OperationExceptions.Type.WRONGARG);

        List<Callable<String>> result = new ArrayList<>();

        userInOut.write("Splitting file to: " + sourceFileName + partDelimeter + "[" + String.format("%0" +
                blockInfo.getDimension(blockSize, fullSize) + "d-%0" +
                blockInfo.getDimension(blockSize, fullSize) + "d]", 1, blockInfo.getCount(blockSize, fullSize)));

        if (checker.checkSize(blockSize, fullSize)) {
            statistic.clearAll();

            for (int i = 0; i < blockInfo.getCount(blockSize, fullSize); i++) {

                String partName = String.format("%0" + blockInfo.getDimension(blockSize, fullSize) + "d", i + 1);
                String threadName = "Thread-" + partName;
                statistic.put(threadName, "start");

                File outputFile = new File(sourceFileName + partDelimeter + partName);
                if (outputFile.exists()) outputFile.delete();

                result.add(new StreamDataCopying(
                        sourceFile,
                        outputFile,
                        blockSize * i,
                        0,
                        blockSize,
                        threadName,
                        statistic));
            }
        }
        if (result.size() == 0) throw new OperationExceptions(OperationExceptions.Type.NOSPLITT);
        return result;
    }
}
