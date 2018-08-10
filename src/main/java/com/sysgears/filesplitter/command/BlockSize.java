package com.sysgears.filesplitter.command;

public class BlockSize {
    String blockSize;

    public BlockSize(String blockSize) {
        this.blockSize = blockSize;
    }

    public long getSize() throws NumberFormatException{
        long size;
        try {
            size = Long.parseLong(blockSize);
            return size;
        } catch (NumberFormatException e) {
        }
        try {
            size = Long.parseLong(blockSize.substring(0, blockSize.length() - 1));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка ввода размера блока");
        }


        long mult = 1;
        switch (blockSize.charAt(blockSize.length() - 1)) {
            case 'G':
                mult *= 1000;
            case 'M':
                mult *= 1000;
            case 'K':
                mult *= 1000;
                break;
                default:throw new NumberFormatException("Не удалось распознать размерность блока");
        }
        return size*mult;
    }
}
