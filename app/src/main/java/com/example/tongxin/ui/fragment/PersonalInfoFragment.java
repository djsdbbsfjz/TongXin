package com.example.tongxin.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tongxin.R;
import com.example.tongxin.adapter.PersonalPostTitleAdapter;
import com.example.tongxin.entity.Post;
import com.example.tongxin.entity.User;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.ActivityUtils;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.PictureUtils;
import com.example.tongxin.utils.ToastFactory;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 *
 * 个人资料卡，根据传入的user信息进行加载
 * Created by djs on 2017/5/8.
 */

public class PersonalInfoFragment extends BaseFragment {

    private User mUser;

    private List<Post> mPosts;

    private PersonalPostTitleAdapter mPersonalPostTitleAdapter;

    private static final String ARG_USER = "user";

    private ImageView mPersonalAvatarImageView;
    private ImageView mPersonalSexImageView;
    private TextView mPersonalUsernameTextView;
    private TextView mPersonalIntroductionTextView;
    private TextView mPersonalCollegeTextView;
    private TextView mPersonalContactTextView;
    private RecyclerView mPersonalPostsRecyclerView;
    private View mProgressView;

    public static PersonalInfoFragment newInstance(User user){
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER,user);

        PersonalInfoFragment personalInfoFragment = new PersonalInfoFragment();
        personalInfoFragment.setArguments(args);
        return personalInfoFragment;
    }

    public void setUser(User user){
        mUser = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = (User) getArguments().getSerializable(ARG_USER);
        mUser = user;
        mPosts = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_info,container,false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_personal_info);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.detail_info);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPersonalAvatarImageView = (ImageView) v.findViewById(R.id.image_view_personal_avatar);
        mPersonalSexImageView = (ImageView) v.findViewById(R.id.image_view_personal_sex);
        mPersonalUsernameTextView = (TextView) v.findViewById(R.id.text_view_personal_username);
        mPersonalIntroductionTextView = (TextView) v.findViewById(R.id.text_view_personal_introduction);
        mPersonalCollegeTextView = (TextView) v.findViewById(R.id.text_view_personal_college);
        mPersonalContactTextView = (TextView) v.findViewById(R.id.text_view_personal_contact_way);
        mPersonalPostsRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_personal_posts);
        mProgressView = v.findViewById(R.id.view_personal_info_progress);

        setupView();

        return v;
    }

    private void setupView(){
        PictureUtils.loadAuthorAvatar(mContext,mPersonalAvatarImageView,mUser);
        mPersonalUsernameTextView.setText(mUser.getNickname());
        mPersonalIntroductionTextView.setText(mUser.getSelfIntroduction().equals("")
                ? getString(R.string.personal_info_def) : mUser.getSelfIntroduction());
        mPersonalCollegeTextView.setText(mUser.getCollegeInformation().equals("")
                ? getString(R.string.personal_info_def) : mUser.getCollegeInformation());
        mPersonalContactTextView.setText(mUser.getContactWay().equals("")
                ? getString(R.string.personal_info_def) : mUser.getContactWay());
        if (!mUser.getSex().equals("")){
            if (mUser.getSex().equals(Constants.SEX_MALE)){
                mPersonalSexImageView.setImageResource(R.drawable.ic_sex_male);
            }else if (mUser.getSex().equals(Constants.SEX_FEMALE)){
                mPersonalSexImageView.setImageResource(R.drawable.ic_sex_female);
            }else {
                mPersonalSexImageView.setImageBitmap(null);
            }
        }
        mPersonalPostsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPersonalPostTitleAdapter = new PersonalPostTitleAdapter(mPosts);
        mPersonalPostsRecyclerView.setAdapter(mPersonalPostTitleAdapter);
        showProgressbar();
        fetchPostsData();
    }

    //获取user的帖子
    private void fetchPostsData() {
        BmobQuery<Post> query = new BmobQuery<>();

        query.addWhereEqualTo("mUser", mUser)
                .order("-mLastCommentTime")
                .include("mUser");

        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    LogUtils.i(TAG, "find my posts success." + list.size());
                    mPosts.clear();
                    mPosts.addAll(list);
                    mPersonalPostTitleAdapter.notifyDataSetChanged();
                    dismissProgressbar();
                } else {
                    LogUtils.i(TAG, "find my posts failed." + e.getMessage());
                    ToastFactory.show(mContext, getString(R.string.failed_load_post));
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
