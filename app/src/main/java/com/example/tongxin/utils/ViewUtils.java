package com.example.tongxin.utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by djs on 2017/5/3.
 */

public class ViewUtils {

    /**
     *
     * @param recyclerView
     * @return
     *
     * recyclerview是否滑动到底部
     */
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

}
