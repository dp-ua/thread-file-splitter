package com.example.filesplitter.file;


import com.example.filesplitter.file.operation.OperationException;
import org.apache.log4j.Logger;

/**
 * Calculates all info about block of data.
 */
public class BlockInfo {
    private final Logger log = Logger.getLogger(BlockInfo.class);

    /**
     * Counts the required number of blocks when specifying the size and length of the block
     *
     * @return long number of blocks to which you want to split the file according
     * to the length and size of the block.
     * @throws ArithmeticException when incorrect numbers are entered
     *                             and arithmetic errors associated with division occur
     */
    public long getCount(long blockSize, long fullLength) throws ArithmeticException {
        if (log.isTraceEnabled()) log.trace("get block counts. blocksize:" + blockSize + " fullLenght:" + fullLength);
        long result = fullLength / blockSize;
        result += fullLength % blockSize > 0 ? 1 : 0;
        if (log.isTraceEnabled()) log.trace("get block counts. result:" + result);
        return result;
    }

    /**
     * Calculates how many characters will be in the number required to display the number of all blocks
     *
     * @return int Example: We have  9 blocks - the return value will be 2 characters.
     * This is enough to display nine different names: 01, 02, 03 ... 09
     * @throws ArithmeticException when incorrect numbers are entered
     *                             and arithmetic errors associated with division occur
     */
    public int getDimension(long blockSize, long fullLength) throws ArithmeticException {
        if (log.isTraceEnabled()) log.trace("get dimension. blocksize:" + blockSize + " fullLenght:" + fullLength);
        int dimension = 1;
        long tempLength = fullLength / blockSize;
        while (tempLength > 0) {
            tempLength /= 10;
            dimension++;
        }
        if (log.isTraceEnabled()) log.trace("get dimension. result:" + dimension);
        return dimension;
    }

    /**
     * Return size of block in bytes.
     * <p>
     * The block size can be specified as either a simple size or with an alphabetic notation: G, M, K
     *
     * @return long size of bytes
     * @throws OperationException if cant parse input string
     */
    public long parseSize(String blockSize) throws OperationException {
        if (log.isTraceEnabled()) log.trace("try to parse block size. input: " + blockSize);
        long size;
        try {
            size = Long.parseLong(blockSize);
            if (log.isTraceEnabled()) log.trace("size parsed. result: " + size);
            return size;
        } catch (NumberFormatException e) {
            try {
                size = Long.parseLong(blockSize.substring(0, blockSize.length() - 1));
            } catch (NumberFormatException ee) {
                if (log.isTraceEnabled()) log.trace("parse error. wrong date format. more than one letter in string");
                throw new OperationException(OperationException.Type.WRONGENTERBLOCK);
            }
        }

        long mult = 1;
        switch (blockSize.charAt(blockSize.length() - 1)) {
            case 'G':
                mult *= 1024;
            case 'M':
                mult *= 1024;
            case 'K':
                mult *= 1024;
                break;
            default:
                if (log.isTraceEnabled()) log.trace("parse error. wrong letter");
                throw new OperationException(OperationException.Type.WRONGENTERBLOCK);
        }
        if (log.isTraceEnabled()) log.trace("size parsed. result: " + size * mult);
        return size * mult;
    }
}
