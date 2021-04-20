package com.Eremej;

import java.io.PrintStream;

public class Debug {
    private static final String PREFIX = "dbg";

    private PrintStream mOut;
    private boolean mIsDebug;

    public Debug(PrintStream stream, boolean isDebug) {
        mOut = stream;
        mIsDebug = isDebug;
    }
    public Debug(PrintStream stream) {
        this(stream, false);
    }
    public Debug() {
        this(System.out, false);
    }

    public void enableDebug() {
        mIsDebug = true;
    }

    public void disableDebug() {
        mIsDebug = false;
    }

    public void log(String tag, String msg, Throwable throwable) {
        if (mIsDebug)
            mOut.println(PREFIX + "/" + tag + ": " + msg + "\n" + throwable.getMessage());
    }
    public void log(String tag, String msg) {
        if (mIsDebug)
            mOut.println(PREFIX + "/" + tag + ": " + msg);
    }
    public void log(String msg) {
        if (mIsDebug)
            mOut.println(PREFIX + ": " + msg);
    }
}
