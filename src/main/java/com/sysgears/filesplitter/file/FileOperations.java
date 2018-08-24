package com.sysgears.filesplitter.file;

import com.sysgears.filesplitter.file.block.movers.BigBlockMover;
import com.sysgears.filesplitter.file.operation.OperationExceptions;
import com.sysgears.filesplitter.statistic.AbstractStatistic;
import com.sysgears.filesplitter.user.UserInOut;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main splitting function.
 * <p>
 * Gets the command arguments and processes them.
 * Starts the process of dividing a file using several threads.
 * Checks the validity of the entered arguments, the presence of the file
 * and the correctness of the input of data for stakeout.
 */
public class FileOperations {

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
     * @param userInOut                user interface
     * @param statistic statistic holder
     */
    public FileOperations(UserInOut userInOut, AbstractStatistic statistic) {
        this.userInOut = userInOut;
        this.statistic = statistic;
    }

    /**
     * Parse arguments and start threads for splitting file
     *
     * @param arguments params for splitting
     * @throws OperationExceptions if there were any errors when parsing
     *                            the parameters or during the separation of files.
     */
    public void split(Map<String, String> arguments) throws OperationExceptions {

        if (!arguments.containsKey("-p"))
            throw new OperationExceptions(OperationExceptions.Type.WRONGARG);

        BlockInfo blockInfo = new BlockInfo();
        OpportunityChecker checker = new OpportunityChecker();

        String sourceFileName = arguments.get("-p");

        File sourceFile = checker.fileSuitable(sourceFileName);
        long blockSize = 0;
        long fullSize = sourceFile.length();
        if (arguments.containsKey("-s")) {
            blockSize = blockInfo.parseSize(arguments.get("-s"));
        } else if (arguments.containsKey("-c")) {
            long count = blockInfo.parseSize(arguments.get("-c"));
            blockSize = fullSize / count;
            blockSize += fullSize % count == 0 ? 0 : 1;
        } else throw new OperationExceptions(OperationExceptions.Type.WRONGARG);

        if (checker.checkSize(blockSize, fullSize)) {
            statistic.clearAll();

            ExecutorService executorService = Executors.newCachedThreadPool();

            ArrayList<Future<String>> result = new ArrayList<>();

            for (int i = 0; i < blockInfo.getCount(blockSize, fullSize); i++) {

                String partName = String.format("%0" + blockInfo.getDimension(blockSize, fullSize) + "d", i + 1);
                String threadName = "Thread-" + partName;
                statistic.put(threadName, "start");

                File outputFile = new File(sourceFileName + "Part" + partName);
                if (outputFile.exists()) outputFile.delete();

                result.add(executorService.submit(
                        new BigBlockMover(
                                sourceFile,
                                outputFile,
                                blockSize * i,
                                0,
                                blockSize,
                                threadName,
                                statistic)));
            }
            for (Future<String> fs : result)
                try {
                    String[] s = fs.get().split(":");
                    statistic.put(s[0], s[1]);

                } catch (InterruptedException | ExecutionException e) {
                    userInOut.write(e.getMessage());
                } finally {
                    executorService.shutdown();
                }
            userInOut.write("Splitting complete, parts:" + blockInfo.getCount(blockSize, fullSize));
            userInOut.write("Files name: " + sourceFileName + "Part" + "[" + String.format("%0" +
                    blockInfo.getDimension(blockSize, fullSize) + "d-%0" +
                    blockInfo.getDimension(blockSize, fullSize) + "d]", 1, blockInfo.getCount(blockSize, fullSize)));
        }
    }


    public void merge(Map<String, String> arguments) throws OperationExceptions {
        if (!arguments.containsKey("-p"))
            throw new OperationExceptions(OperationExceptions.Type.WRONGARG);

        File sourceDirectory = new File(arguments.get("-p"));
        OpportunityChecker checker = new OpportunityChecker();
        if (!checker.checkMergDir(sourceDirectory)) throw new OperationExceptions(OperationExceptions.Type.NOTDIR);
        // TODO: 24.08.18 Сделать проверку агрумента, который будет или создавать новый файл или перезаписывать существующий

        for (Map.Entry<String, Integer> pair : checker.getAvailableFilesForMarging(sourceDirectory).entrySet()) {
            int markIndex = pair.getKey().lastIndexOf(":");
            String fileName = pair.getKey().substring(0, markIndex);
            int dimension = Integer.parseInt(pair.getKey().substring(markIndex + 1));
            File outputFile = new File(sourceDirectory.toPath() + "/" + fileName);
            userInOut.write("Merging " + pair.getValue() + " parts to:" + outputFile.toPath());

            ExecutorService executorService = Executors.newCachedThreadPool();
            ArrayList<Future<String>> result = new ArrayList<>();

            // TODO: 24.08.18 старый файл удаляется. И будет создан новый. Переделать?
            if (outputFile.exists()) outputFile.delete();

            long startPos = 0;
            for (int i = 1; i <= pair.getValue(); i++) {
                File filePart = new File(sourceDirectory.toPath() + "/" + fileName + String.format("Part%0" + dimension + "d", i));
                String threadName = "Thread-" + i;
                statistic.put(threadName, "start");

                result.add(executorService.submit(
                        new BigBlockMover(
                                filePart,
                                outputFile,
                                0,
                                startPos,
                                filePart.length(),
                                threadName,
                                statistic)));
                startPos += filePart.length();
            }

            for (Future<String> fs : result)
                try {
                    String[] s = fs.get().split(":");
                    statistic.put(s[0], s[1]);

                } catch (InterruptedException | ExecutionException e) {
                    userInOut.write(e.getMessage());
                } finally {
                    executorService.shutdown();
                }
            userInOut.write("Merging complete:" + outputFile.toPath());
        }


    }


}

