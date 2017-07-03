package com.example.tongxin.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.ddz.floatingactionbutton.FloatingActionButton;
import com.ddz.floatingactionbutton.FloatingActionMenu;
import com.example.tongxin.R;
import com.example.tongxin.adapter.PostAdapter;
import com.example.tongxin.animation.DepthPageTransformer;
import com.example.tongxin.entity.Post;
import com.example.tongxin.ui.activity.PublishPostActivity;
import com.example.tongxin.ui.activity.RegisterAndLoginActivity;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.ToastFactory;
import com.example.tongxin.utils.ViewUtils;
import com.zhy.magicviewpager.transformer.AlphaPageTransformer;
import com.zhy.magicviewpager.transformer.RotateDownPageTransformer;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;
import static com.example.tongxin.utils.Constants.sSections;

/**
 * 论坛界面
 * Created by djs on 2017/5/3.
 */

public class ForumFragment extends BaseFragment {

    private static final String TAG = "ForumFragment";

    private static final int REQUEST_PUBLISH = 1;

    private int mPageNum;

    private PostAdapter mPostAdapter;

    private List<Post> mPosts;

    private List<ImageView> mImageViewList;

    private int[] mImageId;

    private List<String> mList;

    private String mSection;

    private RecyclerView mPostsRecyclerView;
    private FloatingActionMenu mFloatingActionMenu;
    private FloatingActionButton mFindTeamFab;
    private FloatingActionButton mFindTeammateFab;
    private FloatingActionButton mShowFindTeamFab;
    private FloatingActionButton mShowFindTeammateFab;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mProgressView;
    private View mFabBgView;
    private ViewPager mMainViewPager;
    private Spinner mSpinner;

    private LinearLayoutManager mLinearLayoutManager;

    public enum RefreshType {
        REFRESH, LOAD_MORE
    }

    private RefreshType mRefreshType = RefreshType.LOAD_MORE;

    public enum ShowPostType {
        ALL_POST, ONLY_FIND_TEAM, ONLY_FIND_TEAMMATE
    }

    private ShowPostType mShowPostType = ShowPostType.ALL_POST;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNum = 0;
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forum, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.forum_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setTitle(getString(R.string.title_forum));
        }

        mFloatingActionMenu = (FloatingActionMenu) v.findViewById(R.id.fab);
        mFindTeammateFab = (FloatingActionButton) v.findViewById(R.id.fab_find_teammate);
        mFindTeamFab = (FloatingActionButton) v.findViewById(R.id.fab_find_team);
        mShowFindTeammateFab = (FloatingActionButton) v.findViewById(R.id.fab_only_show_find_teammate);
        mShowFindTeamFab = (FloatingActionButton) v.findViewById(R.id.fab_only_show_find_team);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout_forum);
        mPostsRecyclerView = (RecyclerView) v.findViewById(R.id.posts_recycler_view);
        mProgressView = v.findViewById(R.id.view_forum_progress);
        mMainViewPager = (ViewPager) v.findViewById(R.id.view_pager_main);
        mFabBgView = v.findViewById(R.id.view_fab_bg);
        mSpinner = (Spinner) v.findViewById(R.id.spinner_section);

        mPosts = new ArrayList<>();
        mPostAdapter = new PostAdapter(mPosts);
        mPostsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPostsRecyclerView.setAdapter(mPostAdapter);
        initSectionList();
        setupViews();
        setListener();

        return v;
    }

    /**
     * 初始化viewpager图片信息以及适配器
     */
    private void initImageViewList() {
        mMainViewPager.setPageTransformer(true, new AlphaPageTransformer());
        mMainViewPager.setPageMargin(10);
        mMainViewPager.setOffscreenPageLimit(3);
        mImageId = new int[]{R.drawable.image0, R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};
        mImageViewList = new ArrayList<>();
        for (int imageId : mImageId) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imageId);
            mImageViewList.add(imageView);
        }

        mMainViewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                container.addView(mImageViewList.get(position));
                switch (position) {
                    case 2:
                        mImageViewList.get(position).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse("http://istudy.isoftstone.com/match/index");
                                intent.setData(content_url);
                                startActivity(intent);
                            }
                        });
                        break;
                }
                return mImageViewList.get(position);
            }

            @Override
            public int getCount() {
                return mImageViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViewList.get(position));
            }
        });
        mMainViewPager.setCurrentItem(2);
    }

    private void initSectionList() {
        mList = new ArrayList<>();
        for (String s : sSections) {
            mList.add(s);
        }

        mSection = mList.get(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinner.setAdapter(adapter);
    }


    private void setupViews() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        initImageViewList();
    }

    private void setListener() {
        //发布寻找队伍帖子
        mFindTeamFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser currentUser = BmobUser.getCurrentUser();
                if (currentUser != null) {
                    String name = currentUser.getUsername();
                    String email = currentUser.getEmail();
                    LogUtils.i(TAG, "username:" + name + ",email:" + email);

                    mFloatingActionMenu.collapseImmediately();
                    if (mSection.equals(sSections[0])) {
                        ToastFactory.show(getActivity(), getString(R.string.please_choose_section));
                    } else {
                        Intent intent = PublishPostActivity.newIntent(v.getContext(), Constants.FIND_TEAM, mSection);
                        startActivityForResult(intent, REQUEST_PUBLISH);
                    }
                } else {
                    mFloatingActionMenu.collapseImmediately();
                    ToastFactory.show(mContext, getString(R.string.goto_login));
                    Intent intent = new Intent(v.getContext(), RegisterAndLoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        //发布寻找队友帖子
        mFindTeammateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser currentUser = BmobUser.getCurrentUser();
                if (currentUser != null) {
                    String name = currentUser.getUsername();
                    String email = currentUser.getEmail();
                    LogUtils.i(TAG, "username:" + name + ",email:" + email);

                    mFloatingActionMenu.collapseImmediately();
                    if (mSection.equals(sSections[0])) {
                        ToastFactory.show(getActivity(), getString(R.string.please_choose_section));
                    } else {
                        Intent intent = PublishPostActivity.newIntent(v.getContext(), Constants.FIND_TEAMMATE, mSection);
                        startActivityForResult(intent, REQUEST_PUBLISH);
                    }
                } else {
                    mFloatingActionMenu.collapseImmediately();
                    ToastFactory.show(mContext, getString(R.string.goto_login));
                    Intent intent = new Intent(v.getContext(), RegisterAndLoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        //只显示寻找队伍的帖子
        mShowFindTeamFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionMenu.collapseImmediately();
                showProgressbar();
                mRefreshType = RefreshType.REFRESH;
                mShowPostType = ShowPostType.ONLY_FIND_TEAM;
                reloadPosts();
            }
        });

        //只显示寻找队友的帖子
        mShowFindTeammateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionMenu.collapseImmediately();
                showProgressbar();
                mRefreshType = RefreshType.REFRESH;
                mShowPostType = ShowPostType.ONLY_FIND_TEAMMATE;
                reloadPosts();
            }
        });

        mFloatingActionMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mFabBgView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                mFabBgView.setVisibility(View.GONE);
            }
        });

        mFabBgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionMenu.collapseImmediately();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshType = RefreshType.REFRESH;
                reloadPosts();
            }
        });

        //下滑加载更多
        mPostsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (ViewUtils.isSlideToBottom(recyclerView) &&
                        mPosts.size() >= Constants.NUMBERS_PER_PAGE) {
                    showProgressbar();
                    mRefreshType = RefreshType.LOAD_MORE;
                    reloadPosts();
                }
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSection = mList.get(position);
                showProgressbar();
                mRefreshType = RefreshType.REFRESH;
                reloadPosts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //加载所有帖子，不分类别
    private void fetchAllPost(String section) {
        BmobQuery<Post> queryPosts = new BmobQuery<>();

        queryPosts.setLimit(Constants.NUMBERS_PER_PAGE)
                .setSkip(Constants.NUMBERS_PER_PAGE * (mPageNum++))
                .order("-mLastCommentTime")
                .include("mUser");

        if (!section.equals(sSections[0])) {
            queryPosts.addWhereEqualTo("mSection", section);
        }

        LogUtils.i(TAG, "SIZE:" + Constants.NUMBERS_PER_PAGE * mPageNum);

        queryPosts.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    LogUtils.i(TAG, "find success." + list.size());
                    if (list.size() >= 0) {
                        if (mRefreshType == RefreshType.REFRESH) {
                            mPosts.clear();
                        }
                        if ((list.size() != 0) && (list.size() < Constants.NUMBERS_PER_PAGE)) {
                            LogUtils.i(TAG, "load all posts~");
                        }

                        mPosts.addAll(list);
                        mPostAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        dismissProgressbar();
                    } else {
                        mPageNum--;
                        mSwipeRefreshLayout.setRefreshing(false);
                        dismissProgressbar();
                    }
                } else {
                    mPageNum--;
                    LogUtils.i(TAG, "find failed." + e.getMessage());
                    ToastFactory.show(mContext, getString(R.string.failed_load_post) + e.getMessage());
                    mSwipeRefreshLayout.setRefreshing(false);
                    dismissProgressbar();
                }
            }
        });
    }

    //从服务器获取寻找队伍的帖子
    private void fetchFindTeamPost(String section) {
        BmobQuery<Post> queryPosts = new BmobQuery<>();

        queryPosts.setLimit(Constants.NUMBERS_PER_PAGE)
                .setSkip(Constants.NUMBERS_PER_PAGE * (mPageNum++))
                .addWhereEqualTo("mPostType", Constants.FIND_TEAM)
                .order("-mLastCommentTime")
                .include("mUser");

        if (!section.equals(sSections[0])) {
            queryPosts.addWhereEqualTo("mSection", section);
        }

        LogUtils.i(TAG, "SIZE:" + Constants.NUMBERS_PER_PAGE * mPageNum);

        queryPosts.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    LogUtils.i(TAG, "find success." + list.size());
                    if (list.size() >= 0) {
                        if (mRefreshType == RefreshType.REFRESH) {
                            mPosts.clear();
                        }
                        if ((list.size() != 0) && (list.size() < Constants.NUMBERS_PER_PAGE)) {
                            LogUtils.i(TAG, "load all posts~");
                        }

                        mPosts.addAll(list);
                        mPostAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        dismissProgressbar();
                    } else {
                        mPageNum--;
                        mSwipeRefreshLayout.setRefreshing(false);
                        dismissProgressbar();
                    }
                } else {
                    mPageNum--;
                    LogUtils.i(TAG, "find failed." + e.getMessage());
                    ToastFactory.show(mContext, getString(R.string.failed_load_post));
                    mSwipeRefreshLayout.setRefreshing(false);
                    dismissProgressbar();
                }
            }
        });
    }

    //从服务器获取寻找队友的帖子
    private void fetchFindTeammatePost(String section) {
        BmobQuery<Post> queryPosts = new BmobQuery<>();

        queryPosts.setLimit(Constants.NUMBERS_PER_PAGE)
                .setSkip(Constants.NUMBERS_PER_PAGE * (mPageNum++))
                .addWhereEqualTo("mPostType", Constants.FIND_TEAMMATE)
                .order("-mLastCommentTime")
                .include("mUser");

        if (!section.equals(sSections[0])) {
            queryPosts.addWhereEqualTo("mSection", section);
        }

        LogUtils.i(TAG, "SIZE:" + Constants.NUMBERS_PER_PAGE * mPageNum);

        queryPosts.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    LogUtils.i(TAG, "find success." + list.size());
                    if (list.size() >= 0) {
                        if (mRefreshType == RefreshType.REFRESH) {
                            mPosts.clear();
                        }
                        if ((list.size() != 0) && (list.size() < Constants.NUMBERS_PER_PAGE)) {
                            LogUtils.i(TAG, "load all posts~");
                        }

                        mPosts.addAll(list);
                        mPostAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        dismissProgressbar();
                    } else {
                        mPageNum--;
                        mSwipeRefreshLayout.setRefreshing(false);
                        dismissProgressbar();
                    }
                } else {
                    mPageNum--;
                    LogUtils.i(TAG, "find failed." + e.getMessage());
                    ToastFactory.show(mContext, getString(R.string.failed_load_post));
                    mSwipeRefreshLayout.setRefreshing(false);
                    dismissProgressbar();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PUBLISH:
                if (resultCode == RESULT_OK) {
                    showProgressbar();
                    mRefreshType = RefreshType.REFRESH;
                    reloadPosts();
                    ToastFactory.show(mContext, getString(R.string.succeed_publish));
                }
                break;
            default:
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

    public void refreshUI() {
        if (mPostsRecyclerView.getAdapter().getItemCount() > 0) {
            mPostsRecyclerView.smoothScrollToPosition(0);
        }
        mRefreshType = RefreshType.REFRESH;
        mShowPostType = ShowPostType.ALL_POST;
        reloadPosts();
    }

    public void reloadPosts() {
        switch (mShowPostType) {
            case ALL_POST:
                if (mRefreshType == RefreshType.REFRESH) {
                    mPageNum = 0;
                }
                fetchAllPost(mSection);
                break;
            case ONLY_FIND_TEAM:
                if (mRefreshType == RefreshType.REFRESH) {
                    mPageNum = 0;
                }
                fetchFindTeamPost(mSection);
                break;
            case ONLY_FIND_TEAMMATE:
                if (mRefreshType == RefreshType.REFRESH) {
                    mPageNum = 0;
                }
                fetchFindTeammatePost(mSection);
                break;
        }
    }
}
