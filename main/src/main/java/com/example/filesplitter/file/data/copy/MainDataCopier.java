package com.example.filesplitter.file.data.copy;

import com.example.filesplitter.statistic.AbstractStatistic;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;

/**
 * A streaming method for writing a piece of data from one file to another.
 * <p>
 * Uses a Calabl interface to return information about the termination of a thread.
 * Displays information about the flow progress in the name of the thread
 * It can be used both for dividing a file into parts and for collecting a file into one whole
 */
public class MainDataCopier implements Callable<String> {
    private final Logger log = Logger.getLogger(MainDataCopier.class);
    private final int BLOCKSIZE = 1024;

    private final AbstractStatistic statistic;
    private final File sourceFile;
    private final File outputFile;
    private final long startPosInSource;
    private final long startPosInOutput;
    private final long size;
    private final String threadName;
    private final SmallBlockCopier smallBlockCopier;

    /**
     * Set basic params for work with statistic
     *
     * @param sourceFile       - source file. From it we take the data
     * @param outputFile       - output file. In it we put the data
     * @param startPosInSource - start posoition in source file
     * @param size             - data size for copying
     * @param threadName       - thread name
     * @param statistic        - statistic holder
     */
    public MainDataCopier(File sourceFile, File outputFile, long startPosInSource, long startPosInOutput, long size, String threadName, AbstractStatistic statistic) {
        this.sourceFile = sourceFile;
        this.startPosInSource = startPosInSource;
        this.startPosInOutput = startPosInOutput;
        this.size = size;
        this.threadName = threadName;
        this.outputFile = outputFile;
        this.statistic = statistic;
        smallBlockCopier = new SmallBlockCopier();
    }

    /**
     * Copies a block of data from source file  to output file.
     * <p>
     * For the operation of statistics, the function of copying a large piece of data is divided into small parts.
     * As a result of the execution of the flow, the result is recorded in the statistics, that 100% of the data is copied.
     *
     * @return returns a string of type: "Threadename:done"
     * @throws Exception all exceptions related to the initial position, size and availability of the file
     *                   must be provided in the calling function.
     *                   If something went wrong - an exception will be thrown.
     */
    public String call() throws Exception {

        String infoAboutThread = threadName + "(" + this.hashCode() + "-" + System.currentTimeMillis() + ")" +
                " sourceFile:" + sourceFile.toPath() +
                " startPosInSource:" + startPosInSource +
                " size:" + size +
                " outputFile:" + outputFile.toPath() +
                " startPosInOutput:" + startPosInOutput +
                " fixedBlockSize: " + BLOCKSIZE;

        long done = 0;
        try {
            log.info("Start new Thread: " + infoAboutThread);
            statistic.put(threadName, "start");

            FileChannel inputChannel = new FileInputStream(sourceFile).getChannel();
            if (startPosInSource >= inputChannel.size()) {
                log.error("Start positions is out of file. " + infoAboutThread);
                throw new IllegalArgumentException("Start positions is out of file");
            }
            statistic.put(threadName, "0");

            long posInSource = startPosInSource;
            long posInOutput = startPosInOutput;
            long workSize = (startPosInSource + size) > inputChannel.size() ? inputChannel.size() - startPosInSource - 1 : size;

            log.debug("Try to perform [" + workSize / BLOCKSIZE + "]" + " smallblocks tasks" + infoAboutThread);

            while (workSize > 0) {
                int tempSize = workSize < BLOCKSIZE ? (int) workSize : BLOCKSIZE;
                int written = smallBlockCopier.move(inputChannel, posInSource, tempSize, outputFile, posInOutput);
                done += written;
                statistic.put(threadName, String.valueOf((long) (((double) done / size) * 100)));
                posInSource += tempSize;
                posInOutput += tempSize;
                workSize -= tempSize;


            }
            inputChannel.close();
        } catch (Exception e) {
            log.error(threadName + "(" + this.hashCode() + ") " + e.getMessage(), e);
            throw e;
        }
        statistic.put(threadName, "100");
        log.info("Done. Writen: " + done + "b" + infoAboutThread);
        return threadName + ":done";

    }

}