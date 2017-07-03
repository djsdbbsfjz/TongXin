package com.example.tongxin.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tongxin.R;
import com.example.tongxin.entity.Comment;
import com.example.tongxin.entity.NotifyMessage;
import com.example.tongxin.entity.Post;
import com.example.tongxin.ui.activity.PostDetailActivity;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.PictureUtils;
import com.example.tongxin.utils.StringUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * 新消息通知界面的适配器
 * Created by djs on 2017/5/10.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private List<NotifyMessage> mNotifyMessages;

    private List<Comment> mComments;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Comment mComment;

        private NotifyMessage mNotifyMessage;

        private TextView mUserNameTextView;

        private TextView mCommentTimeTextView;

        private TextView mCommentContentTextView;

        private TextView mPostSubjectOfCommentTextView;

        private TextView mNewMessageTextView;

        private TextView mMessageTypeTextView;

        private CircleImageView mAuthorAvatarImageView;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            mUserNameTextView = (TextView) view.findViewById(R.id.text_view_user_name_my_comments);
            mCommentTimeTextView = (TextView) view.findViewById(R.id.text_view_comment_time_my_comments);
            mCommentContentTextView = (TextView) view.findViewById(R.id.text_view_comment_content_my_comments);
            mNewMessageTextView = (TextView) view.findViewById(R.id.text_view_new_message);
            mMessageTypeTextView = (TextView) view.findViewById(R.id.text_view_author_label);
            mAuthorAvatarImageView = (CircleImageView) view.findViewById(R.id.image_view_user_head_picture_my_comments);
            mPostSubjectOfCommentTextView = (TextView) view.findViewById(R.id.text_view_post_of_comment);
        }

        public void bindPost(Comment comment,NotifyMessage notifyMessage){
            mComment = comment;
            mNotifyMessage = notifyMessage;
            mUserNameTextView.setText(comment.getUser().getNickname());
            mCommentTimeTextView.setText(StringUtils.timeDiff(StringUtils.string2date(comment.getCreatedAt())));
            mCommentContentTextView.setText(comment.getContent());
            mPostSubjectOfCommentTextView.setText("原帖：" + comment.getPost().getPostSubject());
            PictureUtils.loadAuthorAvatar(itemView.getContext(), mAuthorAvatarImageView,comment.getUser());
            if (!notifyMessage.isRead()){
                mNewMessageTextView.setVisibility(View.VISIBLE);
            }else {
                mNewMessageTextView.setVisibility(View.GONE);
            }
            if (notifyMessage.getType().equals(Constants.sMessageType[0])){
                mMessageTypeTextView.setText(itemView.getContext().getString(R.string.message_notify0));
            }else {
                mMessageTypeTextView.setText(itemView.getContext().getString(R.string.message_notify1));
            }
        }

        @Override
        public void onClick(View v) {
            Post post = mComment.getPost();
            mNotifyMessage.setRead(true);
            mNotifyMessage.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {

                }
            });
            Intent intent = PostDetailActivity.newIntent(v.getContext(),post);
            v.getContext().startActivity(intent);
        }
    }

    public MessageAdapter(List<NotifyMessage> notifyMessages,List<Comment> comments){
        mNotifyMessages = notifyMessages;
        mComments = comments;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        NotifyMessage notifyMessage = mNotifyMessages.get(position);
        holder.bindPost(comment,notifyMessage);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
}
