package com.example.tongxin.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.tongxin.R;
import com.example.tongxin.ui.fragment.LoginFragment;
import com.example.tongxin.utils.ActivityManagerUtils;

/**
 *
 */

public class RegisterAndLoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);

        ActivityManagerUtils.getInstance().addActivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.register_login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fm = getSupportFragmentManager();
        LoginFragment publishPostFragment = new LoginFragment();
        fm.beginTransaction().add(R.id.frame_layout_login_container, publishPostFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

