package com.sysgears.filesplitter.file.operation;

import com.sysgears.filesplitter.file.BlockInfo;
import com.sysgears.filesplitter.file.OpportunityChecker;
import com.sysgears.filesplitter.file.data.copy.MainDataCopier;
import com.sysgears.filesplitter.statistic.AbstractStatistic;
import com.sysgears.filesplitter.user.UserInOut;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * The implementation of an abstract operation that performs splitting file
 */
public class Splitting implements AbstractOperation {
    private static final Logger log = Logger.getLogger(Splitting.class);
    private String PART = "Part";
    private final UserInOut userInOut;
    private final AbstractStatistic statistic;
    private final BlockInfo blockInfo = new BlockInfo();
    private final OpportunityChecker checker = new OpportunityChecker();

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
     * @throws OperationException An exception related to checking for the availability of the source file,
     *                            the possibility of separation.
     */
    @Override
    public List<Callable<String>> getTaskMap(Map<String, String> arguments) throws OperationException {
        String infoAboutThread = " Splitting("+this.hashCode()+ "-" + System.currentTimeMillis()+"): ";
        for (Map.Entry<String,String> pair: arguments.entrySet()             ) {
            infoAboutThread+=" key:[" + pair.getKey() + "] value:[" + pair.getValue() +"]"; }

        log.info("Start. Try to get tasks. " + infoAboutThread);
        List<Callable<String>> result = new ArrayList<>();
        try {
            if (!arguments.containsKey("-p")) {
                log.error("Error: Not file specified. "+ infoAboutThread);
                throw new OperationException(OperationException.Type.WRONGARG);
            }
            String sourceFileName = arguments.get("-p");

            File sourceFile = checker.fileSuitable(sourceFileName);

            long blockSize;
            long fullSize = sourceFile.length();
            if (arguments.containsKey("-s")) {
                blockSize = blockInfo.parseSize(arguments.get("-s"));
            } else if (arguments.containsKey("-c")) {
                long count = blockInfo.parseSize(arguments.get("-c"));
                blockSize = fullSize / count;
                blockSize += fullSize % count == 0 ? 0 : 1;
            } else {
                log.error("Error: Not specified number of parts. "+ infoAboutThread);
                throw new OperationException(OperationException.Type.WRONGARG);
            }
            log.debug("Splitting file to: " + sourceFileName + PART + "[" + String.format("%0" +
                    blockInfo.getDimension(blockSize, fullSize) + "d-%0" +
                    blockInfo.getDimension(blockSize, fullSize) + "d]", 1, blockInfo.getCount(blockSize, fullSize)) + infoAboutThread);

            userInOut.write("Splitting file to: " + sourceFileName + PART + "[" + String.format("%0" +
                    blockInfo.getDimension(blockSize, fullSize) + "d-%0" +
                    blockInfo.getDimension(blockSize, fullSize) + "d]", 1, blockInfo.getCount(blockSize, fullSize)));

            if (checker.checkSize(blockSize, fullSize)) {
                statistic.clearAll();

                for (int i = 0; i < blockInfo.getCount(blockSize, fullSize); i++) {

                    String partName = String.format("%0" + blockInfo.getDimension(blockSize, fullSize) + "d", i + 1);
                    String threadName = "Thread-" + partName;
                    statistic.put(threadName, "start");

                    File outputFile = new File(sourceFileName + PART + partName);
                    if (outputFile.exists()) outputFile.delete();

                    if (log.isTraceEnabled()) log.trace(
                            "Put " + threadName + " task in list: \n" +
                                    sourceFile.toPath() + " to " + outputFile.getName() + "\n" +
                                    "startPos in source file: " + blockSize*i +
                                    " size: " + blockSize + infoAboutThread
                    );

                    result.add(new MainDataCopier(
                            sourceFile,
                            outputFile,
                            blockSize * i,
                            0,
                            blockSize,
                            threadName,
                            statistic));
                }
            }
            if (result.size() == 0) throw new OperationException(OperationException.Type.NOSPLITT);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage() + " " + infoAboutThread);
            throw e;
        }
        log.info("Done. Get " + result.size() + " tasks(" + result.hashCode()+"). " + infoAboutThread );
        return result;
    }
}
