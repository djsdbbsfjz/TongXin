package com.example.tongxin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tongxin.R;
import com.example.tongxin.entity.User;
import com.example.tongxin.ui.fragment.PersonalInfoFragment;
import com.example.tongxin.utils.ActivityManagerUtils;

public class PersonalInfoActivity extends AppCompatActivity {

    private boolean isFirstStart = true;

    public static final String EXTRA_USER = "com.example.tongzhou.user";

    private PersonalInfoFragment mPersonalInfoFragment;

    public static Intent newIntent(Context packageContext, User user){
        Intent intent = new Intent(packageContext,PersonalInfoActivity.class);
        intent.putExtra(EXTRA_USER,user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ActivityManagerUtils.getInstance().addActivity(this);

        User user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (isFirstStart){
            mPersonalInfoFragment = PersonalInfoFragment.newInstance(user);
            fragmentManager.beginTransaction().add(R.id.frame_layout_personal_info_container,mPersonalInfoFragment)
                    .commit();
            isFirstStart = false;
        }else {
            mPersonalInfoFragment.setUser(user);
            fragmentManager.beginTransaction().replace(R.id.frame_layout_personal_info_container, mPersonalInfoFragment)
                    .commit();
        }
    }
}
