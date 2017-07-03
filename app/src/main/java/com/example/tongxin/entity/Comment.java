package com.example.tongxin.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by djs on 2017/5/2.
 */

public class Comment extends BmobObject {

    public static final String TAG = "Comment";

    private User mUser;//作者

    private Post mPost;//帖子

    private String mContent;//评论内容

    private String mParentId;//若不是子评论，该值为"null"，否则为父评论的ObjectId

    private BmobFile mImage0;//评论图片

    private BmobFile mImage1;

    private BmobFile mImage2;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Post getPost() {
        return mPost;
    }

    public void setPost(Post post) {
        mPost = post;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getParentId() {
        return mParentId;
    }

    public void setParentId(String parentId) {
        mParentId = parentId;
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
}
