package com.gz.mozixing.utils;

import android.util.Log;

import com.gz.mozixing.BuildConfig;


/**
 * 日志工具类，可以针对级别对日志的打印进行控制。
 *
 * @author Alex
 * @since 19/1/30
 */
public class LogUtil {

    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 6;

    private static int level = VERBOSE;

    public static void v(String tag, String msg) {
        if (level <= VERBOSE && msg != null && BuildConfig.LOG_DEBUG) {
            Log.v(tag, msg + "");
        }
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG && msg != null && BuildConfig.LOG_DEBUG) {
            Log.d(tag, msg + "");
        }
    }

    public static void i(String tag, String msg) {
        if (level <= INFO && msg != null && BuildConfig.LOG_DEBUG) {
            Log.i(tag, msg + "");
        }
    }

    public static void w(String tag, String msg) {
        if (level <= WARN && msg != null && BuildConfig.LOG_DEBUG) {
            Log.w(tag, msg + "");
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (level <= WARN && msg != null && BuildConfig.LOG_DEBUG) {
            Log.w(tag, msg + "", tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (level <= ERROR && msg != null && BuildConfig.LOG_DEBUG) {
            Log.e(tag, msg + "", tr);
        }
    }

}