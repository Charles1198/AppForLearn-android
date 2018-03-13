package com.bqteam.appforlearn.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.bqteam.appforlearn.app.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by charles on 16/10/17.
 */

public class FileUtil {
    private static final String DIR_IMAGE_CREATE_AT = "dir_image_create_at";
    private static final String DIR_AVATAR_CREATE_AT = "dir_avatar_create_at";
    private static final String DIR_FILE_CREATE_AT = "dir_file_create_at";

    //是否含有文件file
    public static boolean hasFile(String fileName) {
        Context context = MyApplication.getContext();
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasFile(String filePath, String Filename) {
        File file = new File(filePath, Filename);
        return file.exists();
    }

    public static String getImageDirectory() {

        File file = new File(MyApplication.getContext().getExternalCacheDir() + "/image");

        return file.getAbsolutePath();

    }

    public static String getAvatarDirectory() {
        File file = new File(MyApplication.getContext().getExternalCacheDir() + "/avatar");
        if (!file.exists() && file.mkdirs()) {
//            SPUtils.put(DIR_AVATAR_CREATE_AT, DateUtil.getCurTime());
        }
        return file.getAbsolutePath();
    }

    public static String getFileDirectory() {
        File file = new File(MyApplication.getContext().getExternalFilesDir("apk") + "/apk");
        if (!file.exists() && file.mkdirs()) {
//            SPUtils.put(DIR_FILE_CREATE_AT, DateUtil.getCurTime());
        }
        return file.getAbsolutePath();
    }

    //获取内部存储目录
    public static String getInternalDirectory() {
        return MyApplication.getContext().getFilesDir().getAbsolutePath();
    }

    //获取外部存储目录
    public static String getExternalDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    //将文件存储在本地
    public static boolean save(byte[] bytes, String filePath, String fileName) {
        File file = new File(filePath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //将 bitmap 存储在本地
    public static boolean saveBitmap(Bitmap bitmap, String fileName) {
        File file = new File(getImageDirectory(), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除指定文件
    public static void deleteFile(String filePath, String Filename) {
        File file = new File(filePath, Filename);
        if (file.exists()) {
            file.delete();
        }
    }

    //删除指定文件夹
    public static void deleteDirectory(String dir) {
        File file = new File(dir);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    deleteDirectory(file1.getAbsolutePath());
                } else {
                    file1.delete();
                }
            }
        }
    }

    //从本地获取文件
    public static Bitmap getBitmap(String file, String Filename) {
        File cropFile = new File(file, Filename);
        return BitmapFactory.decodeFile(cropFile.getPath());
    }



    public static void writeStringToFile(String s, String fileName) {
        Context context = MyApplication.getContext();
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readStringFromFile(String fileName) {
        Context context = MyApplication.getContext();
        String string = "";
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                string = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("activity_login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("activity_login activity", "Can not read file: " + e.toString());
        }

        return string;
    }

    /**
     * 从assets目录中读取字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getStringFromAssets(Context context, String fileName) {
        String Result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }
}
