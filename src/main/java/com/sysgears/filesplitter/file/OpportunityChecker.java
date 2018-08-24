package com.sysgears.filesplitter.file;

import com.sysgears.filesplitter.file.operation.OperationExceptions;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Checks the ability to separate the specified file according to the specified parameters
 */
public class OpportunityChecker {
    private final String markForPart="Part";

    /**
     * Checks the correctness of the source file
     * <p>
     * Verifies that the correct file name is specified,
     * that the file exists,
     * that there is enough space on the disk to separate the file.
     *
     * @param fileName source file name in String
     * @return source file name in File
     * @throws OperationExceptions if the specified file is not suitable
     *                            for partitioning or not enough free disk space
     */
    public File fileSuitable(String fileName) throws OperationExceptions {
        File file = new File(fileName);
        if (!file.exists()) throw new OperationExceptions(OperationExceptions.Type.NOFILE);
        if (file.isDirectory()) throw new OperationExceptions(OperationExceptions.Type.WRONGNAME);
        if (file.length() > file.getFreeSpace()) throw new OperationExceptions(OperationExceptions.Type.NOSPACE);
        return file;
    }

    /**
     * Checks the block size and the size of the entire file
     *
     * @param blockSize  size of block data
     * @param fullLength full size of data
     * @return true if the block size is less than the whole data packet
     * @throws OperationExceptions if the block size is larger fullLength
     */
    public boolean checkSize(long blockSize, long fullLength) throws OperationExceptions {
        if (blockSize >= fullLength) throw new OperationExceptions(OperationExceptions.Type.WRONGBLOCKSIZE);
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
     * @return
     * @throws IOException
     */
    public Map<String, Integer> getAvailableFilesForMarging(File dir) {
        Map<String, Integer> result = new TreeMap<>();

        Set<String> fileNames = new TreeSet<>();

        for (File f : dir.listFiles()) if (f.isFile()) fileNames.add(f.getName());

        for (String s : fileNames) {
            int index = s.lastIndexOf(markForPart);
            if (index == -1) continue;
            String fileName = s.substring(0, index);
            int count = s.length() - index - 4;
            if (result.containsKey(fileName + ":" + count)) {
                result.put(fileName + ":" + count, result.get(fileName + ":" + count) + 1);
            } else result.put(fileName + ":" + count, 1);
        }
        for (Map.Entry<String, Integer> pair : new HashMap<>(result).entrySet()) {
            for (int i = 1; i <= pair.getValue(); i++) {
                String name = pair.getKey().substring(0, pair.getKey().lastIndexOf(":"));
                int count = 0;
                try {
                    count = Integer.parseInt(pair.getKey().substring(pair.getKey().lastIndexOf(":") + 1));
                } catch (NumberFormatException e) {
                    result.remove(pair.getKey());
                    break;
                }

                if (!fileNames.contains(String.format("%s"+markForPart+"%0" + count + "d", name, count))) {
                    result.remove(pair.getKey());
                    break;
                }
            }

        }
        return result;
    }
}


