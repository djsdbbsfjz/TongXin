package com.example.tongxin.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tongxin.R;
import com.example.tongxin.entity.User;
import com.example.tongxin.message.MessageUtils;
import com.example.tongxin.proxy.UserProxy;
import com.example.tongxin.ui.activity.MainActivity;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.ActivityUtils;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.ToastFactory;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;

/**
 *
 * 登陆界面
 * Created by djs on 2017/5/2.
 */

public class LoginFragment extends BaseFragment implements UserProxy.ILoginListener{

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private Button mResetPasswordButton;
    private View mProgressView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,container,false);

        mUsernameEditText = (EditText) v.findViewById(R.id.edit_text_username_login);
        mPasswordEditText = (EditText) v.findViewById(R.id.edit_text_password_login);
        mLoginButton = (Button) v.findViewById(R.id.button_login);
        mRegisterButton = (Button) v.findViewById(R.id.button_to_register);
        mResetPasswordButton = (Button) v.findViewById(R.id.button_to_reset_password);
        mProgressView = v.findViewById(R.id.view_login_progress);

        setListener();

        return v;
    }

    private void setListener() {

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                replaceFragment(registerFragment);
            }
        });

        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                replaceFragment(resetPasswordFragment);
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_layout_login_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    private void login(){
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(username)){
            mUsernameEditText.setError(getString(R.string.empty_username));
            return;
        } else if (TextUtils.isEmpty(password)){
            mPasswordEditText.setError(getString(R.string.empty_password));
            return;
        } else {
            showProgressbar();
            mUserProxy.setOnLoginListener(this);
            LogUtils.i(TAG, "login begin....");
            mProgressView.setVisibility(View.VISIBLE);
            mUserProxy.login(username.trim(), password.trim());
        }
    }

    @Override
    public void onLoginSuccess() {
        dismissProgressbar();
        ToastFactory.show(mContext, getString(R.string.succeed_login));
        LogUtils.i(TAG,"login succeed!");
        MessageUtils.connectMessageServer(mContext);
        MessageUtils.updateUser();
        ActivityUtils.closeSoftInput(mContext,getView());
        getActivity().finish();
    }

    @Override
    public void onLoginFailure(String msg) {
        dismissProgressbar();
        ToastFactory.show(mContext, getString(R.string.failed_login));
        LogUtils.i(TAG,"login failed!"+ msg);
    }

    private void showProgressbar(){
        if(mProgressView != null && !mProgressView.isShown()) {
            mProgressView.setVisibility(View.VISIBLE);
        }
    }

    private void dismissProgressbar(){
        if(mProgressView != null && mProgressView.isShown()){
            mProgressView.setVisibility(View.GONE);
        }
    }
}
