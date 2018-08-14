package com.sysgears.filesplitter.command;

/**
 * Translate the entered block size value into bytes.
 * <p>
 * Supports such size units as:
 * G - Gigabyte
 * M - megabyte
 * K - Kilobytes.
 */
public class BlockSize {

    /**
     * Input string with information about the size of the block.
     * <p>
     * Сan contain only  numbers or numbers with a letter at the end of the line.
     */
    private final String blockSize;

    public BlockSize(String blockSize) {
        this.blockSize = blockSize;
    }

    /**
     * Return size of block in bytes.
     *
     * @return long size of bytes
     * @throws NumberFormatException if cant parse input string
     */
    public long getSize() throws NumberFormatException {
        long size;
        try {
            size = Long.parseLong(blockSize);
            return size;
        } catch (NumberFormatException e) {
            try {
                size = Long.parseLong(blockSize.substring(0, blockSize.length() - 1));
            } catch (NumberFormatException ee) {
                throw new NumberFormatException("Ошибка ввода размера блока");
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
                throw new NumberFormatException("Не удалось распознать размерность блока");
        }
        return size * mult;
    }
}
