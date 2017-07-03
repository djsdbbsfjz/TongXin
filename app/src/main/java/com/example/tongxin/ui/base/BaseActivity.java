package com.example.tongxin.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.tongxin.MyApplication;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.Sputil;

/**
 * Created by djs on 2017/5/1.
 */

public class BaseActivity extends FragmentActivity implements OnSharedPreferenceChangeListener {
    protected static String TAG ;

    protected MyApplication mMyApplication;
    protected Sputil sputil;
    protected Resources mResources;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        TAG = this.getClass().getSimpleName();
        initConfigure();
    }


    private void initConfigure() {
        mContext = this;
        if(null == mMyApplication){
            mMyApplication = MyApplication.getInstance();
        }
        mMyApplication.addActivity(this);
        if(null == sputil){
            sputil = new Sputil(this, Constants.PRE_NAME);
        }
        sputil.getInstance().registerOnSharedPreferenceChangeListener(this);
        mResources = getResources();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        // TODO Auto-generated method stub
        //可用于监听设置参数，然后作出响应
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
