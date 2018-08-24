package com.sysgears.filesplitter.file;

import com.sysgears.filesplitter.Exception.ExceptionTypes;
import com.sysgears.filesplitter.Exception.SplitterExceptions;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Checks the ability to separate the specified file according to the specified parameters
 */
public class OpportunityChecker {

    /**
     * Checks the correctness of the source file
     * <p>
     * Verifies that the correct file name is specified,
     * that the file exists,
     * that there is enough space on the disk to separate the file.
     *
     * @param fileName source file name in String
     * @return source file name in File
     * @throws SplitterExceptions if the specified file is not suitable
     *                            for partitioning or not enough free disk space
     */
    public File fileSuitable(String fileName) throws SplitterExceptions {
        File file = new File(fileName);
        if (!file.exists()) throw new SplitterExceptions(ExceptionTypes.NOFILE);
        if (file.isDirectory()) throw new SplitterExceptions(ExceptionTypes.WRONGNAME);
        if (file.length() > file.getFreeSpace()) throw new SplitterExceptions(ExceptionTypes.NOSPACE);
        return file;
    }

    /**
     * Checks the block size and the size of the entire file
     *
     * @param blockSize  size of block data
     * @param fullLength full size of data
     * @return true if the block size is less than the whole data packet
     * @throws SplitterExceptions if the block size is larger fullLength
     */
    public boolean checkSize(long blockSize, long fullLength) throws SplitterExceptions {
        if (blockSize >= fullLength) throw new SplitterExceptions(ExceptionTypes.WRONGBLOCKSIZE);
        return true;
    }

    /**
     * Checking whether the transferred File is a directory and whether it has attached files
     *
     * @param file The transmitted File, which must be checked for compliance with the required parameters
     * @return
     */
    public boolean checkMergDir(File file) {
        if (!file.exists()) return false;
        if (!file.isDirectory()) return false;
        Set<File> files = new TreeSet<>();
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                files.add(f);
            }
        }
        if (files.size() == 0) return false;
        return true;
    }

    /**
     *
     * @param dir
     * @param markForPart
     * @return
     * @throws IOException
     */
    public Map<String, Integer> getAvailableFilesForMarging(File dir,String markForPart) throws IOException {

        Set<String> fileNames = new TreeSet<>();

        for (File f : dir.listFiles()) if (f.isFile()) fileNames.add(f.getName());
        Map<String, Integer> map = new TreeMap<>();
        for (String s : fileNames) {
            int index = s.lastIndexOf(markForPart);
            if (index == -1) continue;
            String fileName = s.substring(0, index);
            int count = s.length() - index - 4;
            if (map.containsKey(fileName + ":" + count)) {
                map.put(fileName + ":" + count, map.get(fileName + ":" + count) + 1);
            } else map.put(fileName + ":" + count, 1);
        }
        for (Map.Entry<String, Integer> pair : new HashMap<>(map).entrySet()) {
            for (int i = 1; i <= pair.getValue(); i++) {
                String name = pair.getKey().substring(0, pair.getKey().lastIndexOf(":"));
                int count = 0;
                try {
                    count = Integer.parseInt(pair.getKey().substring(pair.getKey().lastIndexOf(":") + 1));
                } catch (NumberFormatException e) {
                    map.remove(pair.getKey());
                    break;
                }

                if (!fileNames.contains(String.format("%s"+markForPart+"%0" + count + "d", name, count))) {
                    map.remove(pair.getKey());
                    break;
                }
            }

        }
        return map;
    }
}


