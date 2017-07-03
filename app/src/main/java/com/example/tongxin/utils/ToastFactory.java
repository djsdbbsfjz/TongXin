package com.example.tongxin.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 保持全局只有一个Toast实例
 * Created by djs on 2017/5/2.
 */

public class ToastFactory {
    private static Context context = null;
    private static Toast toast = null;

    public static void show(Context context, String text) {
        if (ToastFactory.context == context) {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);

        } else {
            ToastFactory.context = context;
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
