package com.sysgears.filesplitter.command;


/**
 * Counts the required number of blocks when specifying the size and length of the block.
 *
 * In addition, it calculates how many characters will be enough to number all the blocks.
 */
public class BlockCount {

    /**
     * Size of blocks
     */
    private final long blockSize;

    /**
     * Full lenght
     */
    private final long lenght;

    /**
     * Set the block size and full lenght
     *
     * @param blockSize size of blocks
     * @param lenght full len of part
     */
    public BlockCount(long blockSize, long lenght) {
        this.blockSize = blockSize;
        this.lenght = lenght;
    }

    /**
     * Counts the required number of blocks when specifying the size and length of the block
     *
     * @return long number of blocks to which you want to split the file according
     * to the length and size of the block.
     */
    public long getCount() {
        long result = lenght / blockSize;
        result += lenght % blockSize > 0 ? 1 : 0;

        return result;
    }

    /**
     * Calculates how many characters will be in the number required to display the number of all blocks
     *
     * @return int. Example: We have  9 blocks - the return value will be 2 characters: 01, 02, 03 ... 09
     */
    public int getDimenson(){
        int dimenson=1;
        long tempLenght = lenght/blockSize;
        while (tempLenght>0) {
            tempLenght/=10;
            dimenson++;
        }
        return dimenson;
    }
}
