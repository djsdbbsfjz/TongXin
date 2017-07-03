package com.example.tongxin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.tongxin.R;
import com.example.tongxin.entity.Comment;
import com.example.tongxin.entity.Post;

import java.util.List;

/**
 *
 * 个人中心查看自己的帖子和回复的viewpager的适配器
 * Created by djs on 2017/5/7.
 */

public class VPMyPostsAdapter extends PagerAdapter{

    private Context mContext;

    private List<View> mViewList;

    private List<String> mTabStripTitles;

    private List<Post> mPosts;

    private List<Comment> mComments;

    public VPMyPostsAdapter(Context context,List<View> viewList,List<String> tabStripTitles,List<Post> posts,List<Comment> comments) {
        mContext = context;
        mViewList = viewList;
        mTabStripTitles = tabStripTitles;
        mPosts = posts;
        mComments = comments;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {//实例化
        container.addView(mViewList.get(position));
        View view = mViewList.get(position);
        RecyclerView recyclerView;
        if (position == 0){
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_child_my_posts);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(new PostAdapter(mPosts));
        }else if (position == 1){
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_child_my_comments);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(new MyCommentsAdapter(mComments));
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//销毁
        container.removeView(mViewList.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabStripTitles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
