package com.example.tongxin.entity;

import java.io.Serializable;
import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by djs on 2017/5/2.
 */

public class Post extends BmobObject implements Serializable{

    public static final String TAG = "Post";

    private User mUser;//帖子作者

    private String mPostType;//帖子类型，寻找队伍还是寻找队友

    private String mSection;//所属版块

    private String mPostSubject;//帖子标题

    private String mPostContent;//帖子内容

    private String mIntroduction;//个人或队伍描述

    private BmobFile mImage0;//帖子图片0

    private BmobFile mImage1;//帖子图片1

    private BmobFile mImage2;//帖子图片2

    private Integer mReadNum;//阅读数

    private Integer mCommentNum;//评论数

    private Date mLastCommentTime;//最后评论时间

    private String mTeammate0;//队友要求信息

    private String mTeammate1;

    private String mTeammate2;

    private String mTeammate3;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getPostType() {
        return mPostType;
    }

    public void setPostType(String postType) {
        mPostType = postType;
    }

    public String getSection() {
        return mSection;
    }

    public void setSection(String section) {
        mSection = section;
    }

    public String getPostSubject() {
        return mPostSubject;
    }

    public void setPostSubject(String postSubject) {
        mPostSubject = postSubject;
    }

    public String getPostContent() {
        return mPostContent;
    }

    public void setPostContent(String postContent) {
        mPostContent = postContent;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String introduction) {
        mIntroduction = introduction;
    }

    public BmobFile getImage0() {
        return mImage0;
    }

    public void setImage0(BmobFile image0) {
        mImage0 = image0;
    }

    public BmobFile getImage1() {
        return mImage1;
    }

    public void setImage1(BmobFile image1) {
        mImage1 = image1;
    }

    public BmobFile getImage2() {
        return mImage2;
    }

    public void setImage2(BmobFile image2) {
        mImage2 = image2;
    }

    public Integer getReadNum() {
        return mReadNum;
    }

    public void setReadNum(Integer readNum) {
        mReadNum = readNum;
    }

    public Integer getCommentNum() {
        return mCommentNum;
    }

    public void setCommentNum(Integer commentNum) {
        mCommentNum = commentNum;
    }

    public Date getLastCommentTime() {
        return mLastCommentTime;
    }

    public void setLastCommentTime(Date lastCommentTime) {
        mLastCommentTime = lastCommentTime;
    }

    public String getTeammate0() {
        return mTeammate0;
    }

    public void setTeammate0(String teammate0) {
        mTeammate0 = teammate0;
    }

    public String getTeammate1() {
        return mTeammate1;
    }

    public void setTeammate1(String teammate1) {
        mTeammate1 = teammate1;
    }

    public String getTeammate2() {
        return mTeammate2;
    }

    public void setTeammate2(String teammate2) {
        mTeammate2 = teammate2;
    }

    public String getTeammate3() {
        return mTeammate3;
    }

    public void setTeammate3(String teammate3) {
        mTeammate3 = teammate3;
    }
}
