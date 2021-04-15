package com.example.lib;

import android.util.Log;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 8:11 PM
 * @Version 1.0
 */
public class ReportLog {
    private static final String TAG = "ReportLog";

    public static void logD(String msg) {
        Log.d(TAG, msg);
    }

    public static void printStackTrace(Exception e) {

    }
}
