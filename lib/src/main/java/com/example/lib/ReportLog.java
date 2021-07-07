package com.example.lib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 8:11 PM
 * @Version 1.0
 */
public class ReportLog {
    private static final String TAG = "ReportLog";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void logD(String msg) {
        Log.d(TAG, msg);
    }

    public static void logJson(String headInfo, String json) {
        String message;

        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(4);
            } else {
                message = json;
            }
        } catch (JSONException e) {
            message = json;
        }

        printLine(TAG, true);
        message = headInfo + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(TAG, "║ " + line);
        }
        printLine(TAG, false);

    }

    public static void printStackTrace(Exception e) {

    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
}
