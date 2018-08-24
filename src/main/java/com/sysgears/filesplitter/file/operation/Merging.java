package com.sysgears.filesplitter.file.operation;

import com.sysgears.filesplitter.file.OpportunityChecker;
import com.sysgears.filesplitter.file.block.movers.BigBlockMover;
import com.sysgears.filesplitter.statistic.AbstractStatistic;
import com.sysgears.filesplitter.user.UserInOut;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


public class Merging implements AbstractOperation {

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
    public Merging(UserInOut userInOut, AbstractStatistic statistic) {
        this.userInOut = userInOut;
        this.statistic = statistic;
    }

    @Override
    public List<Callable<String>> getTaskMap(Map<String, String> arguments) throws OperationExceptions {
        if (!arguments.containsKey("-p"))
            throw new OperationExceptions(OperationExceptions.Type.WRONGARG);

        File sourceDirectory = new File(arguments.get("-p"));
        OpportunityChecker checker = new OpportunityChecker();
        if (!checker.checkMergDir(sourceDirectory)) throw new OperationExceptions(OperationExceptions.Type.NOTDIR);
        // TODO: 24.08.18 Сделать проверку агрумента, который будет или создавать новый файл или перезаписывать существующий

        List<Callable<String>> result = new ArrayList<>();

        for (Map.Entry<String, Integer> pair : checker.getAvailableFilesForMarging(sourceDirectory).entrySet()) {
            int markIndex = pair.getKey().lastIndexOf(":");
            String fileName = pair.getKey().substring(0, markIndex);
            int dimension = Integer.parseInt(pair.getKey().substring(markIndex + 1));
            File outputFile = new File(sourceDirectory.toPath() + "/" + fileName);
            userInOut.write("Merging " + pair.getValue() + " parts to:" + outputFile.toPath());

            if (outputFile.exists()) outputFile.delete();

            long startPos = 0;
            for (int i = 1; i <= pair.getValue(); i++) {
                File filePart = new File(sourceDirectory.toPath() + "/" + fileName + String.format("Part%0" + dimension + "d", i));
                String threadName = "Thread-" + i;
                statistic.put(threadName, "start");

                result.add(
                        new BigBlockMover(
                                filePart,
                                outputFile,
                                0,
                                startPos,
                                filePart.length(),
                                threadName,
                                statistic));
                startPos += filePart.length();
            }

        }
        if (result.size()==0) throw new OperationExceptions(OperationExceptions.Type.NOPARTSFILE);
        return result;

    }
}
