package com.example.tongxin.message;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;

import com.example.tongxin.entity.Comment;
import com.example.tongxin.entity.NotifyMessage;
import com.example.tongxin.entity.User;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.ToastFactory;

import java.util.logging.Logger;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by djs on 2017/5/10.
 */

public class MessageUtils {

    private static final String TAG = "MessageUtils";

    /**
     * 连接到服务器
     * @param context
     */
    public static void connectMessageServer(final Context context) {
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null){
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //ToastFactory.show(context,"connect succeed");
                        LogUtils.i(TAG, "message connect succeed");
                    } else {
                        LogUtils.i(TAG, e.getErrorCode() + "/" + e.getMessage());
                    }
                }
            });
        }
    }

    /**
     * 断开连接
     */
    public static void messageDisConnect() {
        BmobIM.getInstance().disConnect();
    }

    /**
     * 发送新评论通知
     * @param context
     * @param user 目标用户
     * @param comment 哪个评论
     */
    public static void sendMessage(final Context context,final User user, final Comment comment,final String type){
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setUser(user);
        notifyMessage.setComment(comment);
        notifyMessage.setRead(false);
        notifyMessage.setType(type);
        notifyMessage.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){

                }else {
                    LogUtils.i(TAG,e.getMessage());
                }
            }
        });

        LogUtils.i(TAG,"notify message save succeed");
        BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(),"null","null");
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true,
                new ConversationListener() {
            @Override
            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
            }
        });
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent("send succeed");
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e == null){
                }else {
                }
            }
        });
    }

    public static void updateUser(){
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(currentUser.getObjectId(), "null", "null"));
        }
    }
}
