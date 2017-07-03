package com.example.tongxin.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by djs on 2017/5/10.
 */

public class NotifyMessage extends BmobObject {

    private User mUser;//通知消息的目标用户

    private Comment mComment;//发送通知的某个回复

    private boolean isRead;//是否点击过消息

    private String mType;//回复的是帖子还是评论

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Comment getComment() {
        return mComment;
    }

    public void setComment(Comment comment) {
        mComment = comment;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
