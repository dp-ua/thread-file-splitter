package com.sysgears.filesplitter.user;

import java.io.IOException;

/**
 * Basic interface to talk with user
 */
public interface UserInOut {
    /**
     * write message to User
     * @param message string to user
     */
    void write(String message);

    /**
     * get message from User
     * @return string message from user
     * @throws IOException errors received during data entry
     */
    String read() throws IOException;
}
