package com.sysgears.filesplitter.file.operation;

import com.sysgears.filesplitter.AbstractStatistic;
import com.sysgears.filesplitter.file.OpportunityChecker;
import com.sysgears.filesplitter.file.data.copy.MainDataCopier;
import com.sysgears.filesplitter.user.UserInOut;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * The implementation of an abstract operation that performs merging file
 */
public class Merging implements AbstractOperation {
    private static final Logger log = Logger.getLogger(Merging.class);
    private String partDelimeter = "Part";
    private final UserInOut userInOut;
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

    /**
     * Get  a list of tasks, the result of which will be merge the source files into a one output file
     *
     * @param arguments -that determine which tasks will be listed
     *                  - Arguments are specified in the form of a key, the value
     * @return list of operations
     * @throws OperationException An exception related to checking for the availability of the source files,
     *                             the possibility of merging.
     */
    @Override
    public List<Callable<String>> getTaskMap(Map<String, String> arguments) throws OperationException {
        List<Callable<String>> result = new ArrayList<>();
        try {
            if (!arguments.containsKey("-p"))
                throw new OperationException(OperationException.Type.WRONGARG);
            File sourceDirectory = new File(arguments.get("-p"));
            OpportunityChecker checker = new OpportunityChecker();
            if (!checker.checkMergDir(sourceDirectory))
                throw new OperationException(OperationException.Type.NOTDIR);

            for (Map.Entry<String, Integer> pair : checker.getAvailableFilesForMarging(sourceDirectory).entrySet()) {
                int markIndex = pair.getKey().lastIndexOf(":");
                String fileName = pair.getKey().substring(0, markIndex);
                int dimension = Integer.parseInt(pair.getKey().substring(markIndex + 1));
                File outputFile = new File(sourceDirectory.toPath() + "/" + fileName);
                userInOut.write("Merging " + pair.getValue() + " parts to:" + outputFile.toPath());

                // TODO: 24.08.18 Сделать? проверку агрумента, который будет или создавать новый файл или перезаписывать существующий
                // TODO: 24.08.18 Сейчас файл удаляется
                if (outputFile.exists()) outputFile.delete();

                long startPos = 0;
                for (int i = 1; i <= pair.getValue(); i++) {
                    File filePart = new File(sourceDirectory.toPath() + "/" + fileName + String.format(partDelimeter + "%0" + dimension + "d", i));
                    String threadName = "Thread-" + i;
                    statistic.put(threadName, "start");

                    result.add(
                            new MainDataCopier(
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
            if (result.size() == 0) throw new OperationException(OperationException.Type.NOPARTSFILE);
        } catch (Exception e) {
            statistic.interupt();
            log.debug(e.getMessage());
            throw e;
        }
        return result;
    }
}