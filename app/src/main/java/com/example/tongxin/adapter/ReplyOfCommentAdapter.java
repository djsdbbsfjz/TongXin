package com.example.tongxin.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tongxin.R;
import com.example.tongxin.entity.Comment;
import com.example.tongxin.ui.activity.PersonalInfoActivity;
import com.example.tongxin.utils.StringUtils;

import java.util.List;

/**
 * 加载某一评论的子评论的适配器
 * Created by djs on 2017/5/3.
 */

public class ReplyOfCommentAdapter extends RecyclerView.Adapter<ReplyOfCommentAdapter.ReplyViewHolder> {

    private List<Comment> mComments;

    static class ReplyViewHolder extends RecyclerView.ViewHolder{

        private Comment mComment;

        private TextView mContentOfReplyTextView;

        private TextView mUsernameOfReplyTextView;

        private TextView mTimeOfReplyTextView;

        public ReplyViewHolder(View view){
            super(view);
            mContentOfReplyTextView = (TextView) view.findViewById(R.id.text_view_content_of_reply);
            mUsernameOfReplyTextView = (TextView) view.findViewById(R.id.text_view_username_of_reply);
            mTimeOfReplyTextView = (TextView) view.findViewById(R.id.text_view_time_of_reply);

            mUsernameOfReplyTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mComment.getUser() != null) {
                        Intent intent = PersonalInfoActivity.newIntent(v.getContext(), mComment.getUser());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bindReplyOfComment(Comment comment){
            mComment = comment;
            mUsernameOfReplyTextView.setText(comment.getUser().getNickname() +"：");
            mContentOfReplyTextView.setText(comment.getContent());
            mTimeOfReplyTextView.setText("  " + StringUtils.timeDiff(StringUtils.string2date(comment.getCreatedAt())));
        }
    }

    public ReplyOfCommentAdapter(List<Comment> comments){
        mComments = comments;
    }

    @Override
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_reply_of_comment,parent,false);
        return new ReplyOfCommentAdapter.ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.bindReplyOfComment(comment);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
}
