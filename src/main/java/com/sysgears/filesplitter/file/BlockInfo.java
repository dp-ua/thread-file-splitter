package com.sysgears.filesplitter.file;

import com.sysgears.filesplitter.Exception.MyException;
import com.sysgears.filesplitter.Exception.TypeException;

/**
 * Calculate all info about block of data.
 *
 *
 */
public class BlockInfo {

    /**
     * Counts the required number of blocks when specifying the size and length of the block
     *
     * @return long number of blocks to which you want to split the file according
     * to the length and size of the block.
     * @throws ArithmeticException when incorrect numbers are entered
     *                             and arithmetic errors associated with division occur
     */
    public long getCount(long blockSize, long fullLength) throws ArithmeticException {
        long result = fullLength / blockSize;
        result += fullLength % blockSize > 0 ? 1 : 0;

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
        int dimension = 1;
        long tempLength = fullLength / blockSize;
        while (tempLength > 0) {
            tempLength /= 10;
            dimension++;
        }
        return dimension;
    }

    /**
     * Return size of block in bytes.
     *
     * @return long size of bytes
     * @throws NumberFormatException if cant parse input string
     */
    public long parseSize(String blockSize) throws MyException {
        long size;
        try {
            size = Long.parseLong(blockSize);
            return size;
        } catch (NumberFormatException e) {
            try {
                size = Long.parseLong(blockSize.substring(0, blockSize.length() - 1));
            } catch (NumberFormatException ee) {
                throw new MyException(TypeException.WRONGENTERBLOCK);
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
                throw new MyException(TypeException.WRONGENTERBLOCK);
        }
        return size * mult;
    }
}
