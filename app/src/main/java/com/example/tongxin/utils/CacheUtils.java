package com.example.tongxin.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.example.tongxin.entity.User;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.BmobUser;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by djs on 2017/5/6.
 */

public class CacheUtils {

    /**
     * 获取/data/data/cache目录
     * @param context
     * @return
     */

    public static File getCacheDirectory(Context context, boolean preferExternal,String dirName) {
        File appCacheDir = null;
        if (preferExternal && MEDIA_MOUNTED
                .equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context,dirName);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }


    private static File getExternalCacheDir(Context context,String dirName) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir2 = new File(new File(dataDir, context.getPackageName()), "cache");
        File appCacheDir = new File(appCacheDir2, dirName);
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                Log.w(TAG,"Unable to create external cache directory");
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                Log.i(TAG,"Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    /**
     * 获取当前用户头像保存路径
     * @param context
     * @param filename
     * @return
     */
    public static String getAvatarCacheDir(Context context,String filename){
        String files = CacheUtils.getCacheDirectory(context, true, BmobUser.getCurrentUser().getUsername()) + "/avatar/" + filename;
        return files;
    }

    /**
     * 获取其他帖子发布者的头像保存路径
     * @param context
     * @param filename
     * @return
     */
    public static String getPostAvatarCacheDir(Context context, String filename){
        String files = CacheUtils.getCacheDirectory(context, true, "avatar") + "/" + filename;
        return files;
    }

    /**
     * 获取帖子的图片的路径
     * @param context
     * @param filename
     * @return
     */
    public static String getPostPicCacheDir(Context context,String filename){
        String files = CacheUtils.getCacheDirectory(context, true, "pic") + "/post/" + filename;
        return files;
    }

    /**
     * 获取帖子图片的路径
     * @param context
     * @param filename
     * @return
     */
    public static String getCommentPicCacheDir(Context context,String filename){
        String files = CacheUtils.getCacheDirectory(context, true, "pic") + "/comment/" + filename;
        return files;
    }

    private static final String TAG = "CacheUtils";
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
