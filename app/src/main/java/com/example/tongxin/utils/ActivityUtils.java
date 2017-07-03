package com.example.tongxin.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.tongxin.MyApplication;

/**
 * Created by djs on 2017/5/2.
 */

public class ActivityUtils {

    /**
     * 关闭已经显示的输入法窗口。
     * @param context 上下文对象，一般为Activity
     * @param focusingView 输入法所在焦点的View
     */
    public static void closeSoftInput(Context context, View focusingView) {
        InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focusingView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 获取屏幕宽高
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return px2dip(activity,dm.widthPixels);
    }

    /**
     * 获取屏幕宽高
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return px2dip(activity,dm.heightPixels);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context 上下文，一般为Activity
     * @param pxValue px像素值
     * @return dp数据值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}
