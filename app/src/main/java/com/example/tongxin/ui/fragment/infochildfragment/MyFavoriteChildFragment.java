package com.example.tongxin.ui.fragment.infochildfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tongxin.R;
import com.example.tongxin.adapter.PostAdapter;
import com.example.tongxin.entity.Post;
import com.example.tongxin.entity.User;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by djs on 2017/5/17.
 */

public class MyFavoriteChildFragment extends BaseFragment {

    private User currentUser;

    private List<Post> mPosts;

    private PostAdapter mPostAdapter;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mProgressView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = BmobUser.getCurrentUser(User.class);
        mPosts = new ArrayList<>();
        mPostAdapter = new PostAdapter(mPosts);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cf_info_my_favorite, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_child_info_my_favorite);
        toolbar.setTitle(getString(R.string.my_favorite));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_my_favorite);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout_my_favorite);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPosts();
            }
        });
        mProgressView = v.findViewById(R.id.view_my_favorite_progress);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mPostAdapter);
        showProgressbar();
        fetchPosts();
        return v;
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

    private void fetchPosts() {
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereRelatedTo("mFavorite", new BmobPointer(currentUser))
            .include("mUser");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    mPosts.clear();
                    mPosts.addAll(list);
                    mPostAdapter.notifyDataSetChanged();
                } else {
                    //ToastFactory.show(PostDetailActivity.this, "find favorites failed!");
                    LogUtils.i(TAG, "find favorites failed!");
                }
                dismissProgressbar();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showProgressbar(){
        if(mProgressView != null && !mProgressView.isShown()) {
            mProgressView.setVisibility(View.VISIBLE);
        }
    }

    private void dismissProgressbar(){
        if(mProgressView != null && mProgressView.isShown()){
            mProgressView.setVisibility(View.GONE);
        }
    }
}
