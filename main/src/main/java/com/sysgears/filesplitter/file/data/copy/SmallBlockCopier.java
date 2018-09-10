package com.sysgears.filesplitter.file.data.copy;

import com.sysgears.filesplitter.file.operation.OperationException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Moves a block of the specified size from one file to another
 */
public class SmallBlockCopier {
    private final Logger log = Logger.getLogger(SmallBlockCopier.class);

    /**
     * Copies part of the content from the source data channel and places it in the  file
     * <p>
     * Data is taken in the source file from the specified position and the specified size
     * If the output file does not exist, a new file will be created.
     * If the size of the output file is smaller than the start position for copying data, the file will be
     * enlarged to the correct size.
     * If the output file in the specified location already has some data - the new data will be written on top of them
     *
     * @param source      - channel - data source for copying
     * @param startSourse - start position in source file
     * @param size        - data block size for copying
     * @param outputFile  - file where we need to put the data
     * @param startOutput - point in the output file where we need to put the data
     * @return number of bytes copied
     * @throws Exception All exceptions associated with the positions in the source and destination files
     *                   and the availability of files must be provided by the calling algorithm.
     *                   All other unplanned exceptions are discarded.
     */
    public int move(FileChannel source, long startSourse, int size, File outputFile, long startOutput) throws Exception {
        String infoAboutThread = " Source: " + source.toString() +
                " startSourse: " + startSourse +
                " size: " + size +
                " outputFile: " + outputFile.toPath() +
                " startOutput: " + startOutput;
        int written = 0;

        if (log.isTraceEnabled()) log.trace(infoAboutThread);
        try {
            FileChannel distanation = new RandomAccessFile(outputFile, "rw").getChannel();
            ByteBuffer buff = ByteBuffer.allocate(size);
            source.read(buff, startSourse);
            ByteBuffer wbuff = ByteBuffer.wrap(buff.array());
            written = distanation.write(wbuff, startOutput);
            distanation.close();

        } catch (Exception e) {
            log.error(e.getMessage() + infoAboutThread, e);
            throw new OperationException(OperationException.Type.SMALLBLOCKERR);
        }
        return written;
    }
}
