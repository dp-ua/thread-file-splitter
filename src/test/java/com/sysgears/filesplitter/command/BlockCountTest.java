package com.sysgears.filesplitter.command;

import org.junit.Assert;
import org.junit.Test;

public class BlockCountTest {

    @Test
    public void getCount_1000to10000() {
        BlockCount blockCount = new BlockCount(1000,10000);
        long res = blockCount.getCount();
        Assert.assertEquals(10,res);
    }

    @Test
    public void getCount_1000to10010() {
        BlockCount blockCount = new BlockCount(1000,10010);
        long res = blockCount.getCount();
        Assert.assertEquals(11,res);
    }


    @Test
    public void getDimenson_2() {
        BlockCount blockCount = new BlockCount(5000,10010);
        int res = blockCount.getDimension();
        Assert.assertEquals(2,res);

    }
}