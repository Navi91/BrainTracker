package com.orgazmpionerki.braintracker.util;

import android.util.Log;

public class Tracer {
    private final static String MULTIPLIER_TAG = "multiplier debug";
    static private boolean isDebugEnabled = true;
    static private boolean isDebugToastEnabled = false;
    static private boolean isMethodTracingEnabled = true;

    private static boolean isNormalStrings(String tag, String message) {
        // tag must be present ALWAYS
        if ((tag == null) || (tag.equals(""))) {
            return false;
        }

        if ((message == null) || (message.equals(""))) {
            return false;
        }
        return true;
    }

    public static void error(String tag, String error) {
        if (isNormalStrings(tag, error)) {
            Log.e(tag, error);
        }
    }

    public static void error(String tag, String error, Throwable e) {
        if (isNormalStrings(tag, error)) {
            Log.e(tag, error, e);
        }
    }

    public static void debug(String message) {
        if (isDebugEnabled) {
            if (isNormalStrings(MULTIPLIER_TAG, message)) {
                Log.v(MULTIPLIER_TAG, message);
            }
        }
    }

    public static void debug(String tag, String message) {
        if (isDebugEnabled) {
            if (isNormalStrings(tag, message)) {
                Log.v(tag, message);
            }
        }
    }

    public static void setEnabled(boolean needDebug, boolean needToast, boolean needMethodEnter) {
        isDebugEnabled = needDebug;
        isDebugToastEnabled = needToast;
        isMethodTracingEnabled = needMethodEnter;
    }

    public static void methodEnter() {
        if (isMethodTracingEnabled) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            StackTraceElement el = elements[3];
            Log.v("MT_enter", "m=" + el.getClassName() + ":" + el.getMethodName() + ", " + el.getFileName() + ":" + el.getLineNumber());
        }
    }

    public static void methodEnter(String tag) {
        if (isMethodTracingEnabled) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            StackTraceElement el = elements[3];
            Log.v(tag, "m=" + el.getClassName() + ":" + el.getMethodName() + ", " + el.getFileName() + ":" + el.getLineNumber());
        }
    }

    public static void methodExit() {
        if (isMethodTracingEnabled) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            StackTraceElement el = elements[3];
            Log.v("MT_exit", "m=" + el.getClassName() + ":" + el.getMethodName() + ", " + el.getFileName() + ":" + el.getLineNumber());
        }
    }
}
