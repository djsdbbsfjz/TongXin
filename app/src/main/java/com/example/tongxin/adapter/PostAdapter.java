package com.example.tongxin.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tongxin.R;
import com.example.tongxin.entity.Post;
import com.example.tongxin.ui.activity.PostDetailActivity;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.PictureUtils;
import com.example.tongxin.utils.StringUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * 论坛主界面显示所有帖子的适配器
 * Created by djs on 2017/5/3.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private static String TAG = "PostAdapter";

    private List<Post> mPosts;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Post mPost;

        private TextView mUserNameTextView;
        private TextView mReadNumTextView;
        private TextView mCommentNumTextView;
        private TextView mPostTimeTextView;
        private TextView mPostSubjectTextView;
        private TextView mTypeTextView;
        private TextView mSectionTextView;
        private CircleImageView mAuthorAvatarImageView;
        private LinearLayout mPostImageLinearLayout;
        private ImageView mPostPicImageView0;
        private ImageView mPostPicImageView1;
        private ImageView mPostPicImageView2;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            mUserNameTextView = (TextView) view.findViewById(R.id.text_view_user_name);
            mReadNumTextView = (TextView) view.findViewById(R.id.text_view_read_num);
            mCommentNumTextView = (TextView) view.findViewById(R.id.text_view_comment_num);
            mPostTimeTextView = (TextView) view.findViewById(R.id.text_view_post_time);
            mPostSubjectTextView = (TextView) view.findViewById(R.id.text_view_post_title);
            mTypeTextView = (TextView) view.findViewById(R.id.text_view_type);
            mSectionTextView = (TextView) view.findViewById(R.id.text_view_section);
            mAuthorAvatarImageView = (CircleImageView) view.findViewById(R.id.image_view_user_head_picture);
            mPostImageLinearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_post_image);
            mPostPicImageView0 = (ImageView) view.findViewById(R.id.image_view_item_image0);
            mPostPicImageView1 = (ImageView) view.findViewById(R.id.image_view_item_image1);
            mPostPicImageView2 = (ImageView) view.findViewById(R.id.image_view_item_image2);
        }

        public void bindPost(Post post){
            mPost = post;
            mUserNameTextView.setText(post.getUser().getNickname());
            mReadNumTextView.setText("阅读" + String.valueOf(post.getReadNum()) + "  ");
            mCommentNumTextView.setText("回复" + String.valueOf(mPost.getCommentNum()));
            mPostTimeTextView.setText(StringUtils.timeDiff(post.getLastCommentTime()));
            mSectionTextView.setText(post.getSection());
            if (post.getPostType().equals(Constants.FIND_TEAM)) {
                mTypeTextView.setText("【寻找队伍】");
                mTypeTextView.setTextColor(Color.parseColor("#1976D2"));
                mPostSubjectTextView.setText(mPost.getPostSubject());
            }else if (post.getPostType().equals(Constants.FIND_TEAMMATE)){
                mTypeTextView.setText("【寻找队友】");
                mPostSubjectTextView.setText(mPost.getPostSubject());
                mTypeTextView.setTextColor(Color.parseColor("#FF5252"));
            }
            PictureUtils.loadAuthorAvatar(itemView.getContext(), mAuthorAvatarImageView,post.getUser());
            if (post.getImage0() != null){
                mPostImageLinearLayout.setVisibility(View.VISIBLE);
                PictureUtils.loadPostPic(itemView.getContext(),mPostPicImageView0,post.getImage0());
                if (post.getImage1() != null){
                    PictureUtils.loadPostPic(itemView.getContext(),mPostPicImageView1,post.getImage1());
                    if (post.getImage1() != null){
                        PictureUtils.loadPostPic(itemView.getContext(),mPostPicImageView2,post.getImage2());
                    }else {
                        mPostPicImageView2.setImageBitmap(null);
                    }
                }else {
                    mPostPicImageView1.setImageBitmap(null);
                    mPostPicImageView2.setImageBitmap(null);
                }
            }else {
                mPostImageLinearLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            mPost.increment("mReadNum");
            mPost.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        LogUtils.i(TAG,"read num + 1");
                    }else{
                        LogUtils.i(TAG,e.getMessage());
                    }
                }
            });

            Intent intent = PostDetailActivity.newIntent(v.getContext(),mPost);
            v.getContext().startActivity(intent);
        }
    }

    public PostAdapter(List<Post> posts){
        mPosts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bindPost(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
