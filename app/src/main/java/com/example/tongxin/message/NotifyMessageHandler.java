package com.example.tongxin.message;

import android.content.Context;
import android.widget.Toast;

import com.example.tongxin.R;
import com.example.tongxin.adapter.MessageAdapter;
import com.example.tongxin.entity.Comment;
import com.example.tongxin.entity.NotifyMessage;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.ToastFactory;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 *
 * 接受消息并处理
 * Created by djs on 2017/5/10.
 */

public class NotifyMessageHandler extends BmobIMMessageHandler{

    private static final String TAG = "NotifyMessageHandler";

    public static List<Comment> sComments = new ArrayList<>();
    public static List<NotifyMessage> sNotifyMessages = new ArrayList<>();
    public static MessageAdapter sMessageAdapter = new MessageAdapter(sNotifyMessages,sComments);

    final private Context context;

    public NotifyMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);
        Toast.makeText(context, context.getString(R.string.new_comments_notify), Toast.LENGTH_LONG).show();
        updateMessage();
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);
    }

    public static void updateMessage(){
        sComments.clear();
        sNotifyMessages.clear();
        BmobQuery<NotifyMessage> query = new BmobQuery<>();
        if (BmobUser.getCurrentUser() == null){
            sMessageAdapter.notifyDataSetChanged();
        }else {
            query.addWhereEqualTo("mUser", BmobUser.getCurrentUser());
            query.order("-createdAt");
            query.include("mUser,mComment,mComment.mUser,mComment.mPost,mComment.mPost.mUser");
            query.findObjects(new FindListener<NotifyMessage>() {
                @Override
                public void done(List<NotifyMessage> list, BmobException e) {
                    if (e == null) {
                        sNotifyMessages.addAll(list);
                        for (NotifyMessage notifyMessage : sNotifyMessages) {
                            sComments.add(notifyMessage.getComment());
                        }
                        sMessageAdapter.notifyDataSetChanged();
                    } else {
                        LogUtils.i(TAG, "find notify message failed:" + e.getMessage());
                    }
                }
            });
        }
    }
}
