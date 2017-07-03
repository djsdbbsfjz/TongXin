package com.example.tongxin.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tongxin.R;
import com.example.tongxin.message.MessageUtils;
import com.example.tongxin.ui.fragment.ForumFragment;
import com.example.tongxin.ui.fragment.InfoFragment;
import com.example.tongxin.ui.fragment.MessageFragment;
import com.example.tongxin.utils.ActivityManagerUtils;
import com.example.tongxin.utils.ToastFactory;

/**
 * 主界面逻辑，包含论坛，消息，个人中心三个fragment
 *
 */
public class MainActivity extends AppCompatActivity{

    private ForumFragment forumFragment;
    private MessageFragment messageFragment;
    private InfoFragment infoFragment;
    private FragmentManager fm;

    private boolean isForum = false;
    private boolean isMessage = false;
    private boolean isInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManagerUtils.getInstance().addActivity(this);

        fm = getSupportFragmentManager();
        forumFragment = new ForumFragment();
        messageFragment = new MessageFragment();
        infoFragment = new InfoFragment();

        fm.beginTransaction().add(R.id.frame_layout_forum_container, forumFragment)
                .add(R.id.frame_layout_forum_container, messageFragment)
                .add(R.id.frame_layout_forum_container, infoFragment)
                .commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_forum:
                        if (!isForum) {
                            fm.beginTransaction().hide(messageFragment)
                                    .hide(infoFragment)
                                    .show(forumFragment)
                                    .commit();
                            isForum = true;
                            isMessage = false;
                            isInfo = false;
                        } else {
                            forumFragment.refreshUI();
                        }
                        return true;

                    case R.id.navigation_message:
                        if (!isMessage) {
                            fm.beginTransaction().hide(forumFragment)
                                    .hide(infoFragment)
                                    .show(messageFragment)
                                    .commit();
                            isMessage = true;
                            isForum = false;
                            isInfo = false;
                        } else {

                        }
                        return true;

                    case R.id.navigation_info:
                        if (!isInfo) {
                            fm.beginTransaction().hide(forumFragment)
                                    .hide(messageFragment)
                                    .show(infoFragment)
                                    .commit();
                            isInfo = true;
                            isForum = false;
                            isMessage = false;
                        } else {

                        }
                        return true;
                }
                return false;
            }
        });
        navigation.setSelectedItemId(R.id.navigation_forum);
    }

    private static long firstTime;

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        int num = infoFragment.getChildFragmentManager().getBackStackEntryCount();

        if (num == 0) {
            if (firstTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                ToastFactory.show(MainActivity.this, getString(R.string.exit_app));
            }
            firstTime = System.currentTimeMillis();
        }else {
            infoFragment.getChildFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageUtils.messageDisConnect();
        ActivityManagerUtils.getInstance().removeAllActivity();
    }
}
