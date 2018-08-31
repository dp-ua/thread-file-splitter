package com.sysgears.filesplitter.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Communicating with the user via the console
 */
public class ConsoleInOut implements UserInOut {

    /**
     * Write message to console
     *
     * @param message sends to console
     */
    public void write(String message) {
        System.out.println(message);
    }

    /**
     * Read message from console
     *
     * @return string message from user
     * @throws IOException if there were problems entering
     */
    public String read() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}