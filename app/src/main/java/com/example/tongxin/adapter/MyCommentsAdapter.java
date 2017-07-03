package com.example.tongxin.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tongxin.R;
import com.example.tongxin.entity.Comment;
import com.example.tongxin.entity.Post;
import com.example.tongxin.ui.activity.PostDetailActivity;
import com.example.tongxin.utils.PictureUtils;
import com.example.tongxin.utils.StringUtils;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * 个人中心查看自己的回复的适配器
 * Created by djs on 2017/5/7.
 */

public class MyCommentsAdapter extends RecyclerView.Adapter<MyCommentsAdapter.ViewHolder> {

    private static final String TAG = "MyCommentsAdapter";

    private List<Comment> mComments;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Comment mComment;

        private TextView mUserNameTextView;

        private TextView mCommentTimeTextView;

        private TextView mCommentContentTextView;

        private TextView mPostSubjectOfCommentTextView;

        private CircleImageView mAuthorAvatarImageView;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            mUserNameTextView = (TextView) view.findViewById(R.id.text_view_user_name_my_comments);
            mCommentTimeTextView = (TextView) view.findViewById(R.id.text_view_comment_time_my_comments);
            mCommentContentTextView = (TextView) view.findViewById(R.id.text_view_comment_content_my_comments);
            mAuthorAvatarImageView = (CircleImageView) view.findViewById(R.id.image_view_user_head_picture_my_comments);
            mPostSubjectOfCommentTextView = (TextView) view.findViewById(R.id.text_view_post_of_comment);
        }

        public void bindPost(Comment comment){
            mComment = comment;
            mUserNameTextView.setText(comment.getUser().getNickname());
            mCommentTimeTextView.setText(StringUtils.timeDiff(StringUtils.string2date(comment.getCreatedAt())));
            mCommentContentTextView.setText(comment.getContent());
            mPostSubjectOfCommentTextView.setText("原帖：" + comment.getPost().getPostSubject());
            PictureUtils.loadAuthorAvatar(itemView.getContext(), mAuthorAvatarImageView,comment.getUser());
        }

        @Override
        public void onClick(View v) {
            Post post = mComment.getPost();
            Intent intent = PostDetailActivity.newIntent(v.getContext(),post);
            v.getContext().startActivity(intent);
        }
    }

    public MyCommentsAdapter(List<Comment> comments){
        mComments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_my_comments,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.bindPost(comment);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
}
