package com.example.filesplitter.file.operation;

import com.example.filesplitter.statistic.ConcurrentMapStatistic;
import com.example.filesplitter.user.ConsoleInOut;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OperationsFactoryTest {

    @Test
    public void testGetOperation() throws OperationException {
        AbstractOperation operation = new OperationsFactory(new ConsoleInOut(), new ConcurrentMapStatistic())
                .getOperation(OperationType.SPLIT);

        Assert.assertEquals(operation.getClass(),new Splitting(new ConsoleInOut(),new ConcurrentMapStatistic()).getClass());

    }
}