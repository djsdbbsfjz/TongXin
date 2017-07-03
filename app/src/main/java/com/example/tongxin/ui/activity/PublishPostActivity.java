package com.example.tongxin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tongxin.R;
import com.example.tongxin.ui.fragment.PublishFindTeamFragment;
import com.example.tongxin.ui.fragment.PublishFindTeammateFragment;
import com.example.tongxin.utils.ActivityManagerUtils;
import com.example.tongxin.utils.Constants;

public class PublishPostActivity extends AppCompatActivity {

    public static final String EXTRA_POST_TYPE = "com.example.tongzhou.post_type";

    public static final String EXTRA_POST_SECTION = "com.example.tongzhou.post_section";

    public static Intent newIntent(Context packageContext, String type, String section){
        Intent intent = new Intent(packageContext,PublishPostActivity.class);
        intent.putExtra(EXTRA_POST_TYPE,type);
        intent.putExtra(EXTRA_POST_SECTION,section);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_post);

        ActivityManagerUtils.getInstance().addActivity(this);
        String type = getIntent().getStringExtra(EXTRA_POST_TYPE);
        String section = getIntent().getStringExtra(EXTRA_POST_SECTION);
        FragmentManager fm = getSupportFragmentManager();
        if (type.equals(Constants.FIND_TEAM)) {
            PublishFindTeamFragment publishPostFragment = PublishFindTeamFragment.newInstance(section);
            fm.beginTransaction().replace(R.id.frame_layout_publish_post_container, publishPostFragment)
                    .commit();
        }else if (type.equals(Constants.FIND_TEAMMATE)){
            PublishFindTeammateFragment publishFindTeammateFragment = PublishFindTeammateFragment.newInstance(section);
            fm.beginTransaction().replace(R.id.frame_layout_publish_post_container, publishFindTeammateFragment)
                    .commit();
        }
    }
}
