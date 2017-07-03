package com.example.tongxin.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tongxin.R;
import com.example.tongxin.proxy.UserProxy;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.StringUtils;
import com.example.tongxin.utils.ToastFactory;

/**
 *
 * 重置密码界面逻辑
 * 发送一峰邮件到注册邮箱
 * 不进行邮箱有效判定
 * Created by djs on 2017/5/2.
 */

public class ResetPasswordFragment extends BaseFragment implements UserProxy.IResetPasswordListener{

    private EditText mEmailEditText;
    private Button mResetPasswordButton;
    private Button mReturnLoginButton;
    private View mProgressView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reset_password,container,false);

        mEmailEditText = (EditText) v.findViewById(R.id.edit_view_email_reset_password);
        mResetPasswordButton = (Button) v.findViewById(R.id.button_reset_password);
        mReturnLoginButton = (Button) v.findViewById(R.id.button_return_login_reset);
        mProgressView = v.findViewById(R.id.view_reset_password_progress);

        setListener();
        return v;
    }

    private void setListener(){
        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        mReturnLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment();
            }
        });
    }

    private void replaceFragment(){
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_layout_login_container,loginFragment)
                .addToBackStack(null)
                .commit();
    }

    private void resetPassword(){
        String email = mEmailEditText.getText().toString();
        if(TextUtils.isEmpty(email)){
            mEmailEditText.setError(getString(R.string.empty_email));
            return;
        } else if (!StringUtils.isValidEmail(email)){
            mEmailEditText.setError(getString(R.string.error_email));
        } else {
            mProgressView.setVisibility(View.VISIBLE);
            mUserProxy.setOnResetPasswordListener(this);
            LogUtils.i(TAG, "reset password begin....");
            mUserProxy.resetPassword(email);
        }
    }

    @Override
    public void onResetSuccess() {
        replaceFragment();
        dismissProgressbar();
        ToastFactory.show(mContext, getString(R.string.succeed_reset_password));
        LogUtils.i(TAG,"reset password succeed!");
    }

    @Override
    public void onResetFailure(String msg) {
        dismissProgressbar();
        ToastFactory.show(mContext, getString(R.string.failed_reset_password));
        LogUtils.i(TAG,"reset password failed!"+ msg);
    }

    private void dismissProgressbar(){
        if(mProgressView != null && mProgressView.isShown()){
            mProgressView.setVisibility(View.GONE);
        }
    }
}
