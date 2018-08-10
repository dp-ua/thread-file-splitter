package com.sysgears.filesplitter.user;

import java.io.IOException;


/**
 * User interface.
 *
 * Sends data to the user and receives responses from him
 */
public class UserView {

    /**
     * Interface for communications
     */
    private final UserInOut userInOut;

    /**
     * Set the basic interface
     *
     * @param userInOut user interface
     */
    public UserView(UserInOut userInOut) {
        this.userInOut = userInOut;
    }

    /**
     * Send message to user
     *
     * @param message String
     */
    public void send(String message) {
        userInOut.write(message);
    }

    /**
     * Get message from user
     *
     * @return String of user input
     * @throws IOException when take some errors while input data
     */
    public String get() throws IOException {
        return userInOut.read();
    }
}
