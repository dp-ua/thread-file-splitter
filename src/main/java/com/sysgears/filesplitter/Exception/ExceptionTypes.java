package com.sysgears.filesplitter.Exception;

/**
 * Personal types of Exceptions
 */
public enum ExceptionTypes {
    NOFILE("file does not exist"),
    WRONGNAME("wrong file name"),
    NOSPACE("not enough free disk space"),
    WRONGBLOCKSIZE("split is not possible. the block size exceeds the file size"),
    WRONGENTERBLOCK("block size is incorrect"),
    WRONGARG("arguments are set incorrectly"),
    STANDART("standart error"),
    NOTDIR("wrong directory specified"),
    NOPARTSFILE("no files found for the merging in the specified directory")
    ;

    /**
     * Description of Exception
     */
    private final String description;

    /**
     * Get description
     *
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description
     *
     * @param description of Exception
     */
    ExceptionTypes(String description) {
        this.description = description;
    }
}
