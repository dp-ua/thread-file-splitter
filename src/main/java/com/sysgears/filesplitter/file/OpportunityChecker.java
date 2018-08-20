package com.sysgears.filesplitter.file;

import com.sysgears.filesplitter.Exception.MyException;
import com.sysgears.filesplitter.Exception.TypeException;

import java.io.File;

/**
 * Checks the ability to separate the specified file according to the specified parameters
 */
public class OpportunityChecker {

    /**
     * Checks the correctness of the source file
     * <p>
     * Verifies that the correct file name is specified,
     * that the file exists,
     * that there is enough space on the disk to separate the file.
     *
     * @param fileName source file name in String
     * @return source file name in File
     * @throws MyException if the specified file is not suitable
     *                     for partitioning or not enough free disk space
     */
    public File fileSuitable(String fileName) throws MyException {
        File file = new File(fileName);
        if (!file.exists()) throw new MyException(TypeException.NOFILE);
        if (file.isDirectory()) throw new MyException(TypeException.WRONGNAME);
        if (file.length() > file.getFreeSpace()) throw new MyException(TypeException.NOSPACE);
        return file;
    }

    /**
     * Checks the block size and the size of the entire file
     *
     * @param blockSize size of block data
     * @param fullLength full size of data
     * @return true if the block size is less than the whole data packet
     * @throws MyException if the block size is larger fullLength
     */
    public boolean checkSize(long blockSize, long fullLength) throws MyException {
        if (blockSize >= fullLength) throw new MyException(TypeException.WRONGBLOCKSIZE);
        return true;
    }


}
