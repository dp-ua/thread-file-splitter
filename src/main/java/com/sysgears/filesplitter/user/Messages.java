package com.sysgears.filesplitter.user;


import com.sysgears.filesplitter.user.UserInOut;

/**
 * Show messages for user
 */
public class Messages {
    /**
     * user in out interface
     */
    private final UserInOut userInOut;

    /**
     * Set user in out interface
     *
     * @param userInOut abstract user in out
     */
    public Messages(UserInOut userInOut) {
        this.userInOut = userInOut;
    }

    /**
     * Show message "Input command"
     */
    public void showInput() {
        userInOut.write("Input command:");
    }

    /**
     * Show list of active threads
     */
    public void showThreads() {
        userInOut.write("Threads: " + Thread.getAllStackTraces().size());
        for (Thread t : Thread.getAllStackTraces().keySet())
            userInOut.write(t.toString());
    }

    /**
     * Show error message
     *
     * @param msg error message
     */
    public void showError(String msg) {
        userInOut.write("Error: " + msg);
    }

    /**
     * Show remaning time
     *
     * @param time in sec
     */
    public void showTimeRemanig(long time) {
        userInOut.write("Time remaning:" + time + "s");
    }
}
