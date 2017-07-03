package com.example.tongxin.utils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tongxin.R;
import com.example.tongxin.entity.Post;
import com.example.tongxin.entity.User;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * 加载头像，帖子图片，评论图片等工具类
 * Created by djs on 2017/5/6.
 */

public class PictureUtils {
    private static final String TAG = "PictureUtils";

    public static Bitmap getScaleBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);

        return getScaleBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaleBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }

    public static void loadUserAvatar(Context context, final ImageView avatarView) {
        final User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser == null) {
            avatarView.setImageResource(R.drawable.header);
        } else {
            BmobFile bmobFile = currentUser.getAvatar();
            if (bmobFile != null) {
                String fileName = bmobFile.getFilename();
                String files = CacheUtils.getAvatarCacheDir(context, fileName);
                Bitmap bitmap = BitmapFactory.decodeFile(files);
                if (bitmap != null) {
                    avatarView.setImageBitmap(bitmap);
                    LogUtils.i(TAG, "local avatar load succeed:" + currentUser.getNickname());
                } else {
                    bmobFile.download(new File(files), new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                LogUtils.i(TAG, "server avatar download succeed:" + currentUser.getNickname());
                                Bitmap bitmap = BitmapFactory.decodeFile(s);
                                avatarView.setImageBitmap(bitmap);
                            } else {
                                LogUtils.i(TAG, "server avatar download failed:" + e.getMessage());
                            }
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                }
            }
        }
    }

    public static void loadAuthorAvatar(Context context, final ImageView avatarView, final User user) {
        if (user.getAvatar() == null) {
            avatarView.setImageResource(R.drawable.header);
        } else {
            BmobFile bmobFile = user.getAvatar();
            if (bmobFile != null) {
                String fileName = bmobFile.getFilename();
                String files = CacheUtils.getPostAvatarCacheDir(context, fileName);
                Bitmap bitmap = BitmapFactory.decodeFile(files);
                if (bitmap != null) {
                    avatarView.setImageBitmap(bitmap);
                    LogUtils.i(TAG, "local avatar load succeed:" + user.getNickname());
                } else {
                    bmobFile.download(new File(files), new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                LogUtils.i(TAG, "server avatar download succeed:" + user.getNickname());
                                Bitmap bitmap = BitmapFactory.decodeFile(s);
                                avatarView.setImageBitmap(bitmap);
                            } else {
                                LogUtils.i(TAG, "server avatar download failed:" + e.getMessage());
                            }
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                }
            }
        }
    }

    public static void loadPostPic(Context context, final ImageView imageView, final BmobFile bmobFile) {
        if (bmobFile != null) {
            String fileName = bmobFile.getFilename();
            String files = CacheUtils.getPostPicCacheDir(context, fileName);
            Bitmap bitmap = BitmapFactory.decodeFile(files);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                LogUtils.i(TAG, "local pic of post load succeed.");
            } else {
                bmobFile.download(new File(files), new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            LogUtils.i(TAG, "server pic of post download succeed.");
                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                            imageView.setImageBitmap(bitmap);
                        } else {
                            LogUtils.i(TAG, "server pic of post download failed:" + e.getMessage());
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            }
        }
    }

    public static void loadPostDetailPic(Context context, final ImageView imageView, final BmobFile bmobFile) {
        if (bmobFile != null) {
            String fileName = bmobFile.getFilename();
            String files = CacheUtils.getPostPicCacheDir(context, fileName);
            Bitmap bitmap = BitmapFactory.decodeFile(files);
            if (bitmap.getWidth() > bitmap.getHeight()){
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                imageView.setLayoutParams(layoutParams);
            }
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                LogUtils.i(TAG, "local pic of post load succeed.");
            } else {
                bmobFile.download(new File(files), new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            LogUtils.i(TAG, "server pic of post download succeed.");
                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                            imageView.setImageBitmap(bitmap);
                        } else {
                            LogUtils.i(TAG, "server pic of post download failed:" + e.getMessage());
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            }
        }
    }

    public static void loadCommentPic(Context context, final ImageView imageView, final BmobFile bmobFile) {
        if (bmobFile != null) {
            String fileName = bmobFile.getFilename();
            String files = CacheUtils.getCommentPicCacheDir(context, fileName);
            Bitmap bitmap = BitmapFactory.decodeFile(files);
            if (bitmap.getWidth() > bitmap.getHeight()){
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                imageView.setLayoutParams(layoutParams);
            }
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                LogUtils.i(TAG, "local pic of comment load succeed.");
            } else {
                bmobFile.download(new File(files), new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            LogUtils.i(TAG, "server pic of comment download succeed.");
                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                            imageView.setImageBitmap(bitmap);
                        } else {
                            LogUtils.i(TAG, "server pic of comment download failed:" + e.getMessage());
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            }
        }
    }

    @TargetApi(19)
    public static String handleImageOnKitKat(Context context,Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(context,contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(context,uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    public static String getImagePath(Context context,Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
