package com.example.tongxin.ui.fragment.infochildfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tongxin.R;
import com.example.tongxin.adapter.VPMyPostsAdapter;
import com.example.tongxin.entity.Comment;
import com.example.tongxin.entity.Post;
import com.example.tongxin.entity.User;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.ToastFactory;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by djs on 2017/5/7.
 */

public class MyPostsChildFragment extends BaseFragment {

    private User currentUser;

    private List<View> mViewList;

    private List<String> mTabStripTitles;

    private List<Post> mPosts;

    private List<Comment> mComments;

    private VPMyPostsAdapter mVPMyPostsAdapter;

    private ViewPager mMyPostsViewPager;
    private PagerTabStrip mPagerTabStrip;
    private View mMyPostsView;
    private View mMyCommentsView;
    private View mProgressView;
    private SwipeRefreshLayout mPostsSwipeRefreshLayout;
    private SwipeRefreshLayout mCommentsSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = BmobUser.getCurrentUser(User.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cf_info_my_posts, container, false);
        mMyPostsView = inflater.inflate(R.layout.vp_my_posts, null);
        mMyCommentsView = inflater.inflate(R.layout.vp_my_comments, null);
        mViewList = new ArrayList<>();
        mViewList.add(mMyPostsView);
        mViewList.add(mMyCommentsView);
        mTabStripTitles = new ArrayList<>();
        mTabStripTitles.add(getString(R.string.my_posts));
        mTabStripTitles.add(getString(R.string.my_comments));

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_child_info_my_posts);
        toolbar.setTitle(getString(R.string.my_posts));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mMyPostsViewPager = (ViewPager) v.findViewById(R.id.view_pager_my_posts);
        mPagerTabStrip = (PagerTabStrip) v.findViewById(R.id.tab_strip_my_posts);
        mProgressView = v.findViewById(R.id.view_my_posts_progress);
        mPostsSwipeRefreshLayout = (SwipeRefreshLayout) mMyPostsView.findViewById(R.id.swipe_refresh_layout_my_posts);
        mCommentsSwipeRefreshLayout = (SwipeRefreshLayout) mMyCommentsView.findViewById(R.id.swipe_refresh_layout_my_comments);

        mPosts = new ArrayList<>();
        mComments = new ArrayList<>();

        mVPMyPostsAdapter = new VPMyPostsAdapter(mContext, mViewList, mTabStripTitles, mPosts, mComments);
        mMyPostsViewPager.setAdapter(mVPMyPostsAdapter);

        if (mPosts.size() == 0 || mComments.size() == 0) {
            showProgressbar();
            fetchPostsData();
        }
        setView();
        setListener();
        return v;
    }

    private void setView(){
        mPagerTabStrip.setTabIndicatorColorResource(R.color.colorPrimaryDark);
    }

    private void setListener() {
        mPostsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPostsData();
            }
        });

        mCommentsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCommentsData();
            }
        });
    }


    private void fetchPostsData() {
        BmobQuery<Post> query = new BmobQuery<>();

        query.addWhereEqualTo("mUser", currentUser)
                .order("-mLastCommentTime")
                .include("mUser");

        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    LogUtils.i(TAG, "find my posts success." + list.size());
                    mPosts.clear();
                    mPosts.addAll(list);
                    mVPMyPostsAdapter.notifyDataSetChanged();
                    fetchCommentsData();
                    mPostsSwipeRefreshLayout.setRefreshing(false);
                } else {
                    LogUtils.i(TAG, "find my posts failed." + e.getMessage());
                    ToastFactory.show(mContext, getString(R.string.failed_load_post));
                    mPostsSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void fetchCommentsData() {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("mUser", currentUser)
                .order("-createdAt")
                .include("mUser,mPost");

        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    LogUtils.i(TAG, "find my comments success." + list.size());
                    mComments.clear();
                    mComments.addAll(list);
                    mVPMyPostsAdapter.notifyDataSetChanged();
                    mCommentsSwipeRefreshLayout.setRefreshing(false);
                    dismissProgressbar();
                } else {
                    LogUtils.i(TAG, "find my comments failed." + e.getMessage());
                    ToastFactory.show(mContext, getString(R.string.failed_load_comment));
                    mCommentsSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProgressbar() {
        if (mProgressView != null && !mProgressView.isShown()) {
            mProgressView.setVisibility(View.VISIBLE);
        }
    }

    private void dismissProgressbar() {
        if (mProgressView != null && mProgressView.isShown()) {
            mProgressView.setVisibility(View.GONE);
        }
    }

}
