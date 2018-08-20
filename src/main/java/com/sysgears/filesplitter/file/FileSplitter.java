package com.sysgears.filesplitter.file;

import com.sysgears.filesplitter.Exception.MyException;
import com.sysgears.filesplitter.Exception.TypeException;
import com.sysgears.filesplitter.statistic.ThreadsStatistic;
import com.sysgears.filesplitter.user.UserInOut;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Main splitting function.
 * <p>
 * Gets the command arguments and processes them.
 * Starts the process of dividing a file using several threads.
 * Checks the validity of the entered arguments, the presence of the file
 * and the correctness of the input of data for stakeout.
 */
public class FileSplitter {
    /**
     * user interface
     */
    private final UserInOut userInOut;

    /**
     * Statistic holder
     */
    private final ThreadsStatistic threadsStatistic;

    /**
     * Set user interface and statistic holder
     *
     * @param userInOut        user interface
     * @param threadsStatistic statistic holder
     */
    public FileSplitter(UserInOut userInOut, ThreadsStatistic threadsStatistic) {
        this.userInOut = userInOut;
        this.threadsStatistic = threadsStatistic;
    }

    /**
     * Parse arguments and start threads for splitting file
     *
     * @param arguments params for splitting
     * @throws MyException if there were any errors when parsing
     *                     the parameters or during the separation of files.
     */
    public void Split(Map<String, String> arguments) throws MyException {

        if (!arguments.containsKey("-p") | !arguments.containsKey("-s"))
            throw new MyException(TypeException.WRONGARG);

        BlockInfo blockInfo = new BlockInfo();
        OpportunityChecker checker = new OpportunityChecker();

        String sourceFileName = arguments.get("-p");

        File sourceFile = checker.fileSuitable(sourceFileName);
        long blockSize = blockInfo.parseSize(arguments.get("-s"));
        long fullSize = sourceFile.length();

        if (checker.checkSize(blockSize, fullSize)) {
            threadsStatistic.clearAll();

            ExecutorService executorService = Executors.newFixedThreadPool(4);
            ArrayList<Future<String>> result = new ArrayList<>();

            for (int i = 0; i < blockInfo.getCount(blockSize, fullSize); i++) {

                String partName = String.format("%0" + blockInfo.getDimension(blockSize, fullSize) + "d", i + 1);
                String threadName = "Thread-" + partName;
                threadsStatistic.put(threadName, "start");

                File outputFile = new File(sourceFileName + partName);
                result.add(executorService.submit(new CopyBlockData(sourceFile, outputFile, blockSize * i, blockSize, threadName, false, threadsStatistic)));
            }
            for (Future<String> fs : result)
                try {
                    String[] s = fs.get().split(":");
                    threadsStatistic.put(s[0], s[1]);

                } catch (InterruptedException | ExecutionException e) {
                    userInOut.write(e.getMessage());
                } finally {
                    executorService.shutdown();
                }
            threadsStatistic.setDone(true);
            userInOut.write("Splitting complete, parts:" + blockInfo.getCount(blockSize, fullSize));
            userInOut.write("Files name: " + sourceFileName + "[" + String.format("%0" + blockInfo.getDimension(blockSize, fullSize) + "d-%0" + blockInfo.getDimension(blockSize, fullSize) + "d]", 1, blockInfo.getCount(blockSize, fullSize)));


        }
    }
}

