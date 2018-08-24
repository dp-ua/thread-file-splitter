package com.sysgears.filesplitter.file.block.movers;

import com.sysgears.filesplitter.statistic.AbstractStatistic;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.*;

/**
 * A streaming method for writing a piece of data from one file to another.
 * <p>
 * Uses a Calabl interface to return information about the termination of a thread.
 * Displays information about the flow progress in the name of the thread
 * It can be used both for dividing a file into parts and for collecting a file into one whole
 */
public class BigBlockMover implements Callable<String> {

    /**
     * Statistic holder.
     */
    private final AbstractStatistic statistic;


    /**
     * Source file, we take the data from it
     */
    private final File sourceFile;

    /**
     * The recipient file, we put the data into it
     */
    private final File outputFile;

    /**
     * The initial position in the source file from where we start to take data
     */
    private final long startPosInSource;

    /**
     * The initial position in the output file from where we start to take data
     */
    private final long startPosInOutput;

    /**
     * Size of the data block to copy.
     * <p>
     * The block size can be specified more than the size of the available data in the file.
     */
    private final long size;

    /**
     * Thread name.
     */
    private final String threadName;

    /**
     * If the value is false, the streaming method does not work with the statistics and does not enter data into it
     */
    private boolean useStatistic;

    /**
     * Set basic params for work with statistic
     *
     * @param sourceFile       - source file
     * @param outputFile       - distanation file
     * @param startPosInSource - start posoition in source file
     * @param size             - size of block to copy.
     * @param threadName       - thread name
     * @param statistic        - statistic holder
     */
    public BigBlockMover(File sourceFile, File outputFile, long startPosInSource, long startPosInOutput, long size, String threadName,  AbstractStatistic statistic) {
        this.sourceFile = sourceFile;
        this.startPosInSource = startPosInSource;
        this.startPosInOutput = startPosInOutput;
        this.size = size;
        this.threadName = threadName;
        this.outputFile = outputFile;
        this.statistic = statistic;
        useStatistic = true;
    }

      public String call() throws Exception {
        statistic.put(threadName, "start");

        FileChannel inputChannel = new FileInputStream(sourceFile).getChannel();
        if (startPosInSource >= inputChannel.size())
            throw new IllegalArgumentException("Start positions is out of file");
        statistic.put(threadName, "0");

        int blockSize = 1024;
        long posInSource = startPosInSource;
        long posInOutput = startPosInOutput;
        long workSize = (startPosInSource + size) > inputChannel.size() ? inputChannel.size() - startPosInSource - 1 : size;
        long done = 0;

        SmallBlockMover smallBlockMover=new SmallBlockMover();
        int count=0;
        while (workSize > 0) {
            count++;
            blockSize = workSize < blockSize ? (int) workSize : 1024;
            int written = smallBlockMover.move(inputChannel, posInSource, blockSize, outputFile, posInOutput);
            done += written;
            statistic.put(threadName, String.valueOf((long)(((double)done/size)*100)));
            posInSource += blockSize;
            posInOutput += blockSize;
            workSize -= blockSize;
            Thread.yield();
        }
        inputChannel.close();
        statistic.put(threadName, "100");
        return threadName + ":done";
    }


}