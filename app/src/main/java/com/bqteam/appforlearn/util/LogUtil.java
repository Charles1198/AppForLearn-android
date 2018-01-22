package com.bqteam.appforlearn.util;

import android.util.Log;

/**
 * @author charles
 * @date 2017/5/11
 */

public class LogUtil {
    public static void d(int... s) {
        String message = s[0] + "";
        for (int i = 1; i < s.length; i++) {
            message += ", " + s[i];
        }
        Log.d("intValues", message);
    }

    public static void d(double... s) {
        String message = s[0] + "";
        for (int i = 1; i < s.length; i++) {
            message += ", " + s[i];
        }
        Log.d("doubleValues", message);
    }

    public static void d(float... s) {
        String message = s[0] + "";
        for (int i = 1; i < s.length; i++) {
            message += ", " + s[i];
        }
        Log.d("floatValues", message);
    }

    public static void d(String... s) {
        String message = s[0];
        for (int i = 1; i < s.length; i++) {
            message += ", " + s[i];
        }
        Log.d("", message);
    }

    public static void d(String tag, String... s) {
        String message = s[0];
        for (int i = 1; i < s.length; i++) {
            message += ", " + s[i];
        }
        Log.d(tag, message);
    }
}
