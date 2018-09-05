package com.sysgears.filesplitter;

import com.sysgears.filesplitter.command.CommandParser;
import com.sysgears.filesplitter.executor.AbstractExecutor;
import com.sysgears.filesplitter.executor.ExecutorType;
import com.sysgears.filesplitter.executor.ThreadExecutor;
import com.sysgears.filesplitter.file.operation.AbstractOperation;
import com.sysgears.filesplitter.file.operation.Merging;
import com.sysgears.filesplitter.file.operation.Splitting;
import com.sysgears.filesplitter.statistic.ConcurrentMapStatistic;
import com.sysgears.filesplitter.user.ConsoleInOut;
import com.sysgears.filesplitter.user.UserInOut;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class iMainTest {
    String path = System.getProperty("user.dir");
    File dir = new File(path + "/testMain");
    File testFile = new File(dir.toPath() + "/testFile");
    File testResult = new File(dir.toPath() + "/testResult");

    @BeforeClass
    private void setUp() {
        dir.mkdirs();

        List<String> lines = new ArrayList<>();
        char c = 'A';
        while (c != 'F') {
            StringBuilder s = new StringBuilder(new String());
            for (int i = 0; i < 50; i++) {
                s.append(c);
            }
            c++;
            lines.add(s.toString());
        }
        try {
            Path p = Files.write(testFile.toPath(), lines, Charset.forName("UTF-8"));
            p = Files.write(testResult.toPath(), lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    void tearDown() {
        delete(dir);
    }

    private void delete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                delete(f);
            file.delete();
        } else {
            file.delete();
        }
    }


    @Test
    public void testMain() throws Exception {
        String test = "split -c 5 -p " + testFile;
        CommandParser commandParser = new CommandParser();
        commandParser.setArgs(test);
        ConcurrentMapStatistic statistic = new ConcurrentMapStatistic();
        UserInOut userInOut = new ConsoleInOut();

        AbstractOperation operation;
        AbstractExecutor executor = new ThreadExecutor(ExecutorType.Type.FIXED);;

        operation = new Splitting(userInOut, statistic);

        executor.doTaskList(operation.getTaskMap(commandParser.getArguments()), statistic);

        operation = new Merging(userInOut, statistic);
        test = "merge -p " + dir;
        commandParser.setArgs(test);
        executor.doTaskList(operation.getTaskMap(commandParser.getArguments()), statistic);

        Assert.assertEquals( FileUtils.readFileToString(testFile, "utf-8"), FileUtils.readFileToString(testResult, "utf-8"));
    }
}