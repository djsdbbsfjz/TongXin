package com.example.tongxin.ui.fragment.infochildfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tongxin.R;
import com.example.tongxin.entity.User;
import com.example.tongxin.message.MessageUtils;
import com.example.tongxin.message.NotifyMessageHandler;
import com.example.tongxin.ui.activity.RegisterAndLoginActivity;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.ui.fragment.PersonalInfoFragment;
import com.example.tongxin.utils.PictureUtils;
import com.example.tongxin.utils.ToastFactory;

import cn.bmob.newim.listener.MessageHandler;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人中心的主界面
 * Created by djs on 2017/5/5.
 */

public class MainChildFragment extends BaseFragment {

    private CircleImageView mAvatarImageView;
    private Button mLoginNowButton;
    private Button mLogoutButton;
    private Button mMyPostsButton;
    private Button mShowSelfIntroButton;
    private Button mMyFavButton;
    private Button mSettingButton;

    private EditChildFragment mEditInfoFragment;
    private MyPostsChildFragment mMyPostsChildFragment;
    private MyFavoriteChildFragment mMyFavoriteChildFragment;
    private SettingChildFragment mSettingChildFragment;

    private FragmentManager fm;

    private User mCurrentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cf_info_main, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_child_info_main);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mAvatarImageView = (CircleImageView) v.findViewById(R.id.circle_image_view_avatar);
        mLoginNowButton = (Button) v.findViewById(R.id.button_login_now);
        mLogoutButton = (Button) v.findViewById(R.id.button_logout);
        mMyPostsButton = (Button) v.findViewById(R.id.button_my_posts);
        mShowSelfIntroButton = (Button) v.findViewById(R.id.button_show_self_introduction);
        mMyFavButton = (Button) v.findViewById(R.id.button_my_favorite);
        mSettingButton = (Button) v.findViewById(R.id.button_setting);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        mCurrentUser = mUserProxy.getCurrentUser();
        setView();
        setListener();
    }

    private void setView() {
        PictureUtils.loadUserAvatar(getActivity(), mAvatarImageView);
        if (mCurrentUser == null) {
            mLoginNowButton.setText(getString(R.string.login_now));
            mLogoutButton.setVisibility(View.GONE);
        } else {
            mLoginNowButton.setText(mCurrentUser.getNickname());
            mLogoutButton.setVisibility(View.VISIBLE);
        }
    }

    private void setListener() {
        if (mCurrentUser == null) {
            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoLogin();
                }
            });
            mLoginNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoLogin();
                }
            });
            mMyPostsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoLogin();
                }
            });
            mMyFavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoLogin();
                }
            });
            mShowSelfIntroButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoLogin();
                }
            });
        } else {
            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editInfo();
                }
            });
            mLoginNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editInfo();
                }
            });
            mLogoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(getString(R.string.ensure_logout))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    BmobUser.logOut();
                                    ToastFactory.show(mContext, getString(R.string.logout_succeed));
                                    NotifyMessageHandler.updateMessage();
                                    init();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .create()
                            .show();
                }
            });
            mMyPostsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMyPosts();
                }
            });
            mShowSelfIntroButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelfInfo();
                }
            });
            mMyFavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMyFavorite();
                }
            });
        }

        mSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetting();
            }
        });
    }

    /**
     * 进入编辑个人信息界面
     */
    private void editInfo() {
        if (mEditInfoFragment == null) {
            mEditInfoFragment = new EditChildFragment();
        }
        fm.beginTransaction().replace(R.id.frame_layout_info_container, mEditInfoFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 进入我的信息
     */
    private void showSelfInfo() {
        PersonalInfoFragment personalInfoFragment = PersonalInfoFragment.newInstance(mCurrentUser);
        fm.beginTransaction().replace(R.id.frame_layout_info_container, personalInfoFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 进入我的帖子界面
     */
    private void showMyPosts() {
        if (mMyPostsChildFragment == null) {
            mMyPostsChildFragment = new MyPostsChildFragment();
        }
        fm.beginTransaction().replace(R.id.frame_layout_info_container, mMyPostsChildFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 进入我的收藏界面
     */
    private void showMyFavorite() {
        if (mMyFavoriteChildFragment == null) {
            mMyFavoriteChildFragment = new MyFavoriteChildFragment();
        }
        fm.beginTransaction().replace(R.id.frame_layout_info_container, mMyFavoriteChildFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 进入我的设置界面
     */
    private void showSetting() {
        if (mSettingChildFragment == null) {
            mSettingChildFragment = new SettingChildFragment();
        }
        fm.beginTransaction().replace(R.id.frame_layout_info_container, mSettingChildFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 检测到未登录，跳转到登陆界面
     */
    private void gotoLogin() {
        ToastFactory.show(getActivity(), getString(R.string.login_please));
        MessageUtils.messageDisConnect();
        Intent intent = new Intent(getActivity(), RegisterAndLoginActivity.class);
        startActivity(intent);
    }
}
