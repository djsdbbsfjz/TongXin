package com.example.tongxin.utils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by djs on 2017/5/1.
 * activity的收集与释放
 */

public class ActivityManagerUtils {
    private ArrayList<Activity> activityList = new ArrayList<Activity>();

    private static ActivityManagerUtils activityManagerUtils;

    private ActivityManagerUtils(){

    }

    public static ActivityManagerUtils getInstance(){
        if(null == activityManagerUtils){
            activityManagerUtils = new ActivityManagerUtils();
        }
        return activityManagerUtils;
    }

    public Activity getTopActivity(){
        return activityList.get(activityList.size()-1);
    }

    public void addActivity(Activity ac){
        activityList.add(ac);
    }

    public void removeAllActivity(){
        for(Activity ac:activityList){
            if(null != ac){
                if(!ac.isFinishing()){
                    ac.finish();
                }
                ac = null;
            }
        }
        activityList.clear();
    }
}
