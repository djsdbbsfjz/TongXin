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
 * 注册界面逻辑
 * Created by djs on 2017/5/2.
 */

public class RegisterFragment extends BaseFragment implements UserProxy.ISignUpListener{

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;
    private Button mRegisterButton;
    private Button mReturnLoginButton;
    private View mProgressView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register,container,false);

        mUsernameEditText = (EditText) v.findViewById(R.id.edit_text_username_register);
        mPasswordEditText = (EditText) v.findViewById(R.id.edit_text_password_register);
        mEmailEditText = (EditText) v.findViewById(R.id.edit_text_email_register);
        mRegisterButton = (Button) v.findViewById(R.id.button_register);
        mReturnLoginButton = (Button) v.findViewById(R.id.button_return_login_reg);
        mProgressView = v.findViewById(R.id.view_register_progress);

        setListener();

        return v;
    }

    private void setListener(){
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
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

    private void register(){
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String email = mEmailEditText.getText().toString();

        if (TextUtils.isEmpty(username)){
            mUsernameEditText.setError(getString(R.string.empty_username));
            return;
        } else if (TextUtils.isEmpty(password)){
            mPasswordEditText.setError(getString(R.string.empty_password));
            return;
        } else if(TextUtils.isEmpty(email)){
            mEmailEditText.setError(getString(R.string.empty_email));
            return;
        } else if (!StringUtils.isValidEmail(email)){
            mEmailEditText.setError(getString(R.string.error_email));
        } else {
            mProgressView.setVisibility(View.VISIBLE);
            mUserProxy.setOnSignUpListener(this);
            LogUtils.i(TAG, "register begin....");
            mUserProxy.signUp(username, password, email);
        }
    }

    @Override
    public void onSignUpSuccess() {
        dismissProgressbar();
        replaceFragment();
        ToastFactory.show(mContext, getString(R.string.succeed_register));
        LogUtils.i(TAG,"register succeed!");
    }

    @Override
    public void onSignUpFailure(String msg) {
        dismissProgressbar();
        ToastFactory.show(mContext, getString(R.string.failed_register));
        LogUtils.i(TAG,"register failed!"+ msg);
    }

    private void dismissProgressbar(){
        if(mProgressView != null && mProgressView.isShown()){
            mProgressView.setVisibility(View.GONE);
        }
    }
}
