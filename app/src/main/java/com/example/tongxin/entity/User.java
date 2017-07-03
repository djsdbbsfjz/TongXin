package com.example.tongxin.entity;

import java.io.Serializable;

import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by djs on 2017/5/1.
 */

public class User extends BmobUser implements Serializable{

    public static final String TAG = "User";

    private String mNickname;//用户名称

    private String mSelfIntroduction;//自我介绍

    private String mCollegeInformation;//学院信息

    private String mContactWay;//联系方式

    private String mSex;//性别

    private BmobRelation mFavorite;//我的收藏

    private BmobFile mAvatar;

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        this.mNickname = nickname;
    }

    public String getSelfIntroduction() {
        return mSelfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        mSelfIntroduction = selfIntroduction;
    }

    public String getCollegeInformation() {
        return mCollegeInformation;
    }

    public void setCollegeInformation(String collegeInformation) {
        this.mCollegeInformation = collegeInformation;
    }

    public String getContactWay() {
        return mContactWay;
    }

    public void setContactWay(String contactWay) {
        this.mContactWay = contactWay;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String sex) {
        this.mSex = sex;
    }

    public BmobRelation getFavorite() {
        return mFavorite;
    }

    public void setFavorite(BmobRelation favorite) {
        this.mFavorite = favorite;
    }

    public BmobFile getAvatar() {
        return mAvatar;
    }

    public void setAvatar(BmobFile avatar) {
        mAvatar = avatar;
    }
}
