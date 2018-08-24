package com.sysgears.filesplitter.file.block.movers;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class SmallBlockMover {
    public int move(FileChannel source, long startSourse, int size, File distanationFile, long startDistanation) throws Exception {
        int written = 0;
        FileChannel distanation = new RandomAccessFile(distanationFile, "rw").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(size);
        source.read(buff, startSourse);
        ByteBuffer wbuff = ByteBuffer.wrap(buff.array());
        written = distanation.write(wbuff, startDistanation);
        distanation.close();
        return written;
    }
}
