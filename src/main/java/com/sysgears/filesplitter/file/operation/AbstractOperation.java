package com.sysgears.filesplitter.file.operation;


import com.sysgears.filesplitter.file.block.movers.BigBlockMover;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public interface AbstractOperation {
    List<Callable<String>> getTaskMap(Map<String, String> arguments) throws OperationExceptions;
}
