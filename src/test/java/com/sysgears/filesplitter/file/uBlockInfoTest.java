package com.sysgears.filesplitter.file;

import com.sysgears.filesplitter.file.operation.OperationException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class uBlockInfoTest {

    @Test
    public void testGetCount() {
        BlockInfo blockInfo = new BlockInfo();
        assertEquals(blockInfo.getCount(10,100),10);
    }

    @Test
    public void testGetDimension() {
        BlockInfo blockInfo = new BlockInfo();
        assertEquals(blockInfo.getDimension(10,100),3);
    }

    @Test
    public void testParseSize() throws OperationException {
        BlockInfo blockInfo = new BlockInfo();
        assertEquals(blockInfo.parseSize("5K"),5*1024);
    }
}