package com.example.tongxin.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tongxin.R;
import com.example.tongxin.entity.Post;
import com.example.tongxin.ui.activity.PostDetailActivity;

import java.util.List;

/**
 *
 * 个人中心查看自己发布的帖子的适配器
 * Created by djs on 2017/5/9.
 */

public class PersonalPostTitleAdapter extends RecyclerView.Adapter<PersonalPostTitleAdapter.ViewHolder>{

    private List<Post> mPosts;

    static class ViewHolder extends RecyclerView.ViewHolder{

        private Post mPost;

        private TextView mPostTitleTextView;

        public ViewHolder(View view){
            super(view);
            mPostTitleTextView = (TextView) view.findViewById(R.id.text_view_personal_post_subject);

            mPostTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PostDetailActivity.newIntent(v.getContext(),mPost);
                    v.getContext().startActivity(intent);
                }
            });
        }

        public void bindTitle(Post post){
            mPost = post;
            mPostTitleTextView.setText(post.getPostSubject());
        }
    }

    public PersonalPostTitleAdapter(List<Post> posts){
        mPosts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_personal_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bindTitle(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
