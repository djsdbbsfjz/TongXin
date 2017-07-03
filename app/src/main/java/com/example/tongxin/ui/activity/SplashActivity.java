package com.example.tongxin.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.tongxin.MyApplication;
import com.example.tongxin.R;
import com.example.tongxin.message.NotifyMessageHandler;
import com.example.tongxin.message.MessageUtils;
import com.example.tongxin.utils.ActivityManagerUtils;
import com.example.tongxin.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;

/**
 * 启动闪屏界面
 */
public class SplashActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    private static final long DELAY_TIME = 2000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new NotifyMessageHandler(this));
            MessageUtils.connectMessageServer(this);
            MessageUtils.updateUser();
        }
        //BmobIM.init(this);
        //BmobIM.registerDefaultMessageHandler(new NotifyMessageHandler(this));

        ActivityManagerUtils.getInstance().addActivity(this);

        LogUtils.i(TAG, TAG + " Launched ！");

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SplashActivity.this, permissions, 1);
        }else {
            redirectByTime();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    redirectByTime();
                } else {
                    Toast.makeText(this, "发生未知错误!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    /**
     * 根据时间进行页面跳转
     */
    private void redirectByTime() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);
    }

    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
