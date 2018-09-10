package com.sysgears.filesplitter.file;

import com.sysgears.filesplitter.file.operation.OperationException;

import java.io.File;
import java.util.*;

/**
 * Verification of the possibility of executing the desired operations on files
 */
public class OpportunityChecker {
    private final String PART = "Part";

    /**
     * Checks the correctness of the source file
     * <p>
     * Verifies that the correct file name is specified,
     * that the file exists,
     * that there is enough space on the disk to split the file.
     *
     * @param fileName source file name in String
     * @return source file name in File
     * @throws OperationException if the specified file is not suitable
     *                            for partitioning or not enough free disk space
     */
    public File fileSuitable(String fileName) throws OperationException {
        File file = new File(fileName);
        if (!file.exists()) throw new OperationException(OperationException.Type.NOFILE);
        if (file.isDirectory()) throw new OperationException(OperationException.Type.WRONGNAME);
        if (file.length() > file.getFreeSpace()) throw new OperationException(OperationException.Type.NOSPACE);
        return file;
    }

    /**
     * Checks the block size and the size of the entire file
     *
     * @param blockSize  size of block data
     * @param fullLength full size of data
     * @return true if the block size is less than the whole data packet
     * @throws OperationException if the block size is larger fullLength
     */
    public boolean checkSize(long blockSize, long fullLength) throws OperationException {
        if (blockSize >= fullLength) throw new OperationException(OperationException.Type.WRONGBLOCKSIZE);
        return true;
    }

    /**
     * Checking  the File is a directory and whether there are files in it
     *
     * @param file The transmitted File, which must be checked for compliance with the required parameters
     * @return false if: File is not directory, File does not exist, does not contain other files
     */
    public boolean checkMergDir(File file) {
        if (!file.exists()) return false;
        if (!file.isDirectory()) return false;
        boolean result;
        try {
            result = file.listFiles().length > 0;
        } catch (NullPointerException e) {
            result = false;
        }
        return result;
    }

    /**
     * Get  a Map of file names ready to merging
     *
     * @param dir Directory where we take the files to merge
     * @return Map<String:       name   ,       Integer:       number> key: name of file, value: number of parts of file
     */
    public Map<String, Integer> getAvailableFilesForMarging(File dir) throws OperationException {
        Map<String, Integer> result = new TreeMap<>();

        Set<String> fileNames = new TreeSet<>();

        for (File f : dir.listFiles()) if (f.isFile()) fileNames.add(f.getName());

        for (String s : fileNames) {
            int index = s.lastIndexOf(PART);
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
                int count;
                try {
                    count = Integer.parseInt(pair.getKey().substring(pair.getKey().lastIndexOf(":") + 1));
                } catch (NumberFormatException e) {
                    result.remove(pair.getKey());
                    break;
                }

                if (!fileNames.contains(String.format("%s" + PART + "%0" + count + "d", name, count))) {
                    result.remove(pair.getKey());
                    break;
                }
            }

        }
        long mergeSize = 0;
        for (Map.Entry<String, Integer> pair : result.entrySet()) {
            File f = new File(pair.getKey());
            mergeSize += f.length();
        }
        if (mergeSize > dir.getFreeSpace()) throw new OperationException(OperationException.Type.NOSPACE);
        return result;
    }
}


