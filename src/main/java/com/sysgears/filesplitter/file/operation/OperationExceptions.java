package com.sysgears.filesplitter.file.operation;

public class OperationExceptions extends Exception{
    public enum Type {
        NOFILE("file does not exist"),
        WRONGNAME("wrong file name"),
        NOSPACE("not enough free disk space"),
        WRONGBLOCKSIZE("split is not possible. the block size exceeds the file size"),
        WRONGENTERBLOCK("block size is incorrect"),
        WRONGARG("arguments are set incorrectly"),
        NOTDIR("wrong directory specified"),
        NOPARTSFILE("no files found for the merging in the specified directory"),
        NOSPLITT("error while parsing block. No File splitting")
        ;
        private String desc;

        Type(String desc) {
            this.desc = desc;
        }
    }

    private Type type;
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public OperationExceptions(Type type) {
        this.type=type;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return type.desc;
    }
}
