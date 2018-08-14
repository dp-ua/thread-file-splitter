package com.sysgears.filesplitter.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.NonWritableChannelException;
import java.util.concurrent.Callable;

/**
 * A streaming method for writing a piece of data from one file to another.
 * <p>
 * Uses a Calabl interface to return information about the termination of a thread.
 * Displays information about the flow progress in the name of the thread
 * It can be used both for dividing a file into parts and for collecting a file into one whole
 */
public class FileWriter implements Callable<String> {

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
    private final long startPos;

    /**
     * Size of the data block to copy.
     * <p>
     * The block size can be specified more than the size of the available data in the file.
     */
    private final long size;

    /**
     * Add data to an existing file or create a new one.
     * <p>
     * true - append data, false - make new file
     */
    private final boolean append;

    /**
     * Thread name.
     */
    private final String threadName;

    /**
     * Set basic params for work
     *
     * @param sourceFile - source file
     * @param outputFile - distanation file
     * @param startPos   - start posoition in source file
     * @param size       - size of block to copy.
     * @param threadName - thread name
     * @param append     - false - make new file, true - append data to exist file
     */
    public FileWriter(File sourceFile, File outputFile, long startPos, long size, String threadName, boolean append) {
        this.sourceFile = sourceFile;
        this.startPos = startPos;
        this.size = size;
        this.threadName = threadName;
        this.outputFile = outputFile;
        this.append = append;
    }

    /**
     * Moves a block of data from one file to another in a stream. The result and progress outputs in the thread name
     *
     * @return Future string. Say "done + Thread name" If the result of the work was successful
     * @throws Exception throw IllegalArgument if params is wrong
     */
    public String call() throws Exception {
        Thread.currentThread().setName(threadName + ":start");

        FileChannel outputChannel;
        if (!append && outputFile.exists()) {
            outputChannel = new FileOutputStream(outputFile, false).getChannel();
            outputChannel.close();
        }
        FileChannel inputChannel = new FileInputStream(sourceFile).getChannel();
        if (startPos >= inputChannel.size()) throw new IllegalArgumentException("Start positions is out of file");
        outputChannel = new FileOutputStream(outputFile, true).getChannel();
        Thread.currentThread().setName(threadName + ":0%");
        long workSize = (startPos + size) > inputChannel.size() ? inputChannel.size() - startPos - 1 : size;
        if (workSize > 100) {
            long pos = startPos;
            long splitSize = workSize / 100;
            long allSize = workSize;
            int percent = 0;
            while (allSize > 0) {
                inputChannel.transferTo(pos, splitSize, outputChannel);
                pos += splitSize;
                if (allSize >= splitSize)
                    allSize -= splitSize;
                else splitSize = allSize;

                Thread.currentThread().setName(threadName + ":" + percent + "%");
                percent++;
                Thread.yield();
            }
        } else inputChannel.transferTo(startPos, workSize, outputChannel);
        outputChannel.close();
        inputChannel.close();
        Thread.currentThread().setName(threadName + ":100%");
        return threadName + ":done";
    }
}
