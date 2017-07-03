package com.example.tongxin;

import android.app.Activity;
import android.app.Application;

import com.example.tongxin.entity.User;
import com.example.tongxin.message.MessageUtils;
import com.example.tongxin.message.NotifyMessageHandler;
import com.example.tongxin.utils.ActivityManagerUtils;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;

/**
 * Created by djs on 2017/5/1.
 */

public class MyApplication extends Application{

    public static String TAG;

    private static MyApplication myApplication = null;

    public static MyApplication getInstance(){
        return myApplication;
    }

    public User getCurrentUser() {
        User user = BmobUser.getCurrentUser(User.class);
        if(user!=null){
            return user;
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TAG = this.getClass().getSimpleName();
        //由于Application类本身已经单例，所以直接按以下处理即可。
        myApplication = this;


    }

    public void addActivity(Activity ac){
        ActivityManagerUtils.getInstance().addActivity(ac);
    }

    public void exit(){
        ActivityManagerUtils.getInstance().removeAllActivity();
    }

    public Activity getTopActivity(){
        return ActivityManagerUtils.getInstance().getTopActivity();
    }

}
