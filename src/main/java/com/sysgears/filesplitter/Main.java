package com.sysgears.filesplitter;

import com.sysgears.filesplitter.command.CommandParser;
import com.sysgears.filesplitter.command.CommandType;
import com.sysgears.filesplitter.command.BlockSize;
import com.sysgears.filesplitter.statistic.ThreadInformation;
import com.sysgears.filesplitter.statistic.TimeController;
import com.sysgears.filesplitter.user.ConsoleInOut;
import com.sysgears.filesplitter.user.UserView;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        /*try {


            FileInputStream inputStream = new FileInputStream("src/main/resources/test.txt");
            FileOutputStream outputStream = new FileOutputStream("src/main/resources/out.txt",true);

            FileChannel chInput = inputStream.getChannel();
            System.out.println(chInput.size());

            FileChannel chOutput = outputStream.getChannel();
            chOutput.transferFrom(chInput,2,1);
            chOutput.transferFrom(chInput,1,1);
            chOutput.transferFrom(chInput,0,1);


            chOutput.close();
            chInput.close();
            outputStream.close();
            inputStream.close();




        } catch (IOException e) {
            e.printStackTrace();
        }

        if (true) return;
*/

        try {
            UserView userView = new UserView(new ConsoleInOut());

            while (true) {
                try {
                    String input = userView.get();
                    Thread thread = new Thread(new ThreadGroup("bla"), new Runnable() {
                        public void run() {
                            System.out.println("Ваще другой поток " + Thread.currentThread().getName());
                        }
                    }, input);
                    thread.start();

                    Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
                    System.out.println("Number of currently running threads: " + threads.size());
                    for (Thread t : threads.keySet()) {
                        System.out.println(t);
                        
                    }

                    CommandParser commandParser = new CommandParser(input);
                    if (commandParser.getCommand() == CommandType.EXIT) break;
                    if (commandParser.getCommand() == CommandType.SPLIT) {
                        Map<String, String> arguments = commandParser.getArguments();
                        if (!arguments.containsKey("-p") | !arguments.containsKey("-s"))
                            throw new IllegalArgumentException("Неправильно указаны аргументы");

                        BlockSize blockSize = new BlockSize(arguments.get("-s"));
                        String sourceFileName = arguments.get("-p");
                        FileChannel inChannel;

                        try {
                            FileInputStream inputStream = new FileInputStream(sourceFileName);
                            inChannel = inputStream.getChannel();
                        } catch (FileNotFoundException e) {
                            throw new FileNotFoundException("Исходный файл не существует");
                        }

                        if (inChannel.size() < blockSize.getSize()) {
                            userView.send("Разбивка невозможна. Размер блока больше размера файла. ");
                            continue;
                        }


                        ThreadInformation threadInformation = new ThreadInformation();
                        TimeController timeController = new TimeController();


                    }

                } catch (FileNotFoundException e) {
                    userView.send("Ошибка: " + e.getMessage());
                } catch (NoSuchFieldException e) {
                    userView.send("Ошибка: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    userView.send("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
