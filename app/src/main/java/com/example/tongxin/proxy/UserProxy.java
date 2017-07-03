package com.example.tongxin.proxy;

import android.content.Context;

import com.example.tongxin.R;
import com.example.tongxin.entity.User;
import com.example.tongxin.utils.LogUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by djs on 2017/5/2.
 */

public class UserProxy {

    public static final String TAG = "UserProxy";

    private Context mContext;

    public UserProxy(Context context){
        this.mContext = context;
    }

    public User getCurrentUser(){
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null){
            LogUtils.i(TAG,"本地用户信息" + user.getObjectId() + "-"
                    + user.getUsername() + "-"
                    + user.getNickname() + "-"
                    + user.getEmail() + "-"
                    + user.getCollegeInformation() + "-"
                    + user.getContactWay() + "-"
                    + user.getSex());
            return user;
        }else {
            LogUtils.i(TAG,"本地用户为空，请登陆!");
        }
        return null;
    }

    public interface ISignUpListener{
        void onSignUpSuccess();
        void onSignUpFailure(String msg);
    }
    private ISignUpListener signUpLister;
    public void setOnSignUpListener(ISignUpListener signUpLister){
        this.signUpLister = signUpLister;
    }

    public void signUp(String username,String password,String email){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setNickname(mContext.getString(R.string.tourist) + username);
        user.setSelfIntroduction("");
        user.setCollegeInformation("");
        user.setSex("");
        user.setContactWay("");
        user.setAvatar(null);
        user.setFavorite(null);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    if(signUpLister != null){
                        signUpLister.onSignUpSuccess();
                    }else{
                        LogUtils.i(TAG,"register listener is null,you must set one!");
                    }
                }else {
                    if(signUpLister != null){
                        signUpLister.onSignUpFailure(e.toString());
                    }else{
                        LogUtils.i(TAG,"signup listener is null,you must set one!");
                    }
                }
            }
        });
    }

    public interface ILoginListener{
        void onLoginSuccess();
        void onLoginFailure(String msg);
    }
    private ILoginListener loginListener;
    public void setOnLoginListener(ILoginListener loginListener){
        this.loginListener  = loginListener;
    }

    public void login(String userName,String password){
        final BmobUser user = new BmobUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(new SaveListener<User>(){
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    if(loginListener != null){
                        loginListener.onLoginSuccess();
                    }else{
                        LogUtils.i(TAG, "login listener is null,you must set one!");
                    }
                }else {
                    if(loginListener != null){
                        loginListener.onLoginFailure(e.toString());
                    }else{
                        LogUtils.i(TAG, "login listener is null,you must set one!");
                    }
                }
            }
        });
    }

    public void logout(){
        BmobUser.logOut();
        LogUtils.i(TAG, "logout result:"+(null == getCurrentUser()));
    }

    public interface IUpdateListener{
        void onUpdateSuccess();
        void onUpdateFailure(String msg);
    }
    private IUpdateListener updateListener;
    public void setOnUpdateListener(IUpdateListener updateListener){
        this.updateListener = updateListener;
    }

    public void update(String...args){
        User user = getCurrentUser();
        user.setUsername(args[0]);
        user.setPassword(args[1]);
        user.setEmail(args[2]);
        user.setNickname(args[3]);
        user.setCollegeInformation(args[4]);
        user.setContactWay(args[5]);
        user.setSex(args[6]);

        user.update(user.getObjectId(),new UpdateListener(){
            @Override
            public void done(BmobException e) {
                if (e == null){
                    if(updateListener != null){
                        updateListener.onUpdateSuccess();
                    }else{
                        LogUtils.i(TAG,"update listener is null,you must set one!");
                    }
                }else {
                    if(updateListener != null){
                        updateListener.onUpdateFailure(e.toString());
                    }else{
                        LogUtils.i(TAG,"update listener is null,you must set one!");
                    }
                }
            }
        });
    }

    public interface IResetPasswordListener{
        void onResetSuccess();
        void onResetFailure(String msg);
    }
    private IResetPasswordListener resetPasswordListener;
    public void setOnResetPasswordListener(IResetPasswordListener resetPasswordListener){
        this.resetPasswordListener = resetPasswordListener;
    }

    public void resetPassword(String email){
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    if(resetPasswordListener != null){
                        resetPasswordListener.onResetSuccess();
                    }else{
                        LogUtils.i(TAG,"reset listener is null,you must set one!");
                    }
                }else {
                    if(resetPasswordListener != null){
                        resetPasswordListener.onResetFailure(e.toString());
                    }else{
                        LogUtils.i(TAG,"reset listener is null,you must set one!");
                    }
                }
            }
        });
    }
}
