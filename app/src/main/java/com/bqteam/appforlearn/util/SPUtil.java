package com.bqteam.appforlearn.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author charles
 * @date 2018/1/22
 */

public class SPUtil {
//    /**
//     * 保存在手机里面的文件名
//     */
//    private static final String FILE_NAME = "app_baseData";
//
//    /**
//     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
//     *
//     * @param key
//     * @param object
//     */
//    public static void put(String key, Object object) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//
//        //在存储之前先进行object类型的判断，如果是String类型的，那么就以string类型进行存储
//        if (object instanceof String) {
//            editor.putString(key, (String) object);
//        } else if (object instanceof Integer) {
//            editor.putInt(key, (Integer) object);
//        } else if (object instanceof Boolean) {
//            editor.putBoolean(key, (Boolean) object);
//        } else if (object instanceof Float) {
//            editor.putFloat(key, (Float) object);
//        } else if (object instanceof Long) {
//            editor.putLong(key, (Long) object);
//        }
//
//        editor.apply();
//    }
//
//    //判断key是否存在
//    public static boolean getBoolean(String key, boolean defaultObject) {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getBoolean(key, defaultObject);
//    }
//
//    //将String key的值拿到
//    public static String getString(String key) {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getString(key, "");
//
//    }
//
//    //获取String类型的变量
//    // 如果没有找到key的值，那么使用默认值来代替value
//    public static String getString(String key, String def) {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getString(key, def);
//    }
//
//    //获取int类型的值变量
//    public static int getInt(String key) {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getInt(key, -1);
//
//    }
//
//    public static float getFloat(String key) {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getFloat(key, 0);
//    }
//
//    public static float getFloat(String key, float def) {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getFloat(key, def);
//    }
//
//    /**
//     * 移除某个key对应的值
//     *
//     * @param key
//     */
//    public static void remove(String key) {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.remove(key);
//        editor.apply();
//    }
//
//    /**
//     * 清除所有数据
//     */
//    public static void clear() {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
//        editor.apply();
//    }
//
//    /**
//     * 查询某个key是否已经存在
//     *
//     * @param key
//     * @return
//     */
//    public static boolean contains(String key) {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.contains(key);
//    }
//
//    /**
//     * 返回所有的键值对
//     *
//     * @return
//     */
//    public static Map<String, ?> getAll() {
//        Context context = MyApplication.getContext();
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getAll();
//    }
}
