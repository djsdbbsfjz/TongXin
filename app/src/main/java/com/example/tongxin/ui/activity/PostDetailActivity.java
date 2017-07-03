package com.example.tongxin.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.tongxin.R;
import com.example.tongxin.entity.Post;
import com.example.tongxin.entity.User;
import com.example.tongxin.message.MessageUtils;
import com.example.tongxin.ui.fragment.PostDetailFragment;
import com.example.tongxin.utils.ActivityManagerUtils;
import com.example.tongxin.utils.ActivityUtils;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.ToastFactory;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 根据传入的post加载帖子信息
 */
public class PostDetailActivity extends AppCompatActivity {

    private boolean isFirstStart = true;

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST = "com.example.tongzhou.post_id";

    private PostDetailFragment postDetailFragment;

    private ImageButton mLikeImageButton;
    private View mProgressView;

    private Post mPost;

    private boolean isFavorite = false;

    public static Intent newIntent(Context packageContext, Post post){
        Intent intent = new Intent(packageContext,PostDetailActivity.class);
        intent.putExtra(EXTRA_POST,post);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ActivityManagerUtils.getInstance().addActivity(this);
        mPost = (Post) getIntent().getSerializableExtra(EXTRA_POST);
        Toolbar toolbar = (Toolbar) findViewById(R.id.post_detail_toolbar);
        mProgressView = findViewById(R.id.view_progress);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.read_post));
        }

        Post post = (Post) getIntent().getSerializableExtra(EXTRA_POST);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (isFirstStart){
            postDetailFragment = PostDetailFragment.newInstance(post);
            fragmentManager.beginTransaction().add(R.id.frame_layout_post_detail_container,postDetailFragment)
                    .commit();
            isFirstStart = false;
        }else {
            postDetailFragment.setPost(post);
            fragmentManager.beginTransaction().replace(R.id.frame_layout_post_detail_container, postDetailFragment)
                    .commit();
        }

        mLikeImageButton = (ImageButton) findViewById(R.id.image_button_like);
        mLikeImageButton.setImageResource(R.drawable.ic_action_not_like);
        isLike();
        mLikeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User currentUser = BmobUser.getCurrentUser(User.class);
                if (currentUser != null) {
                    showProgressbar();
                    if (!isFavorite) {
                        addFavorite();
                    } else {
                        removeFavorite();
                    }
                }else {
                    gotoLogin();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                ActivityUtils.closeSoftInput(PostDetailActivity.this,getCurrentFocus());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void isLike(){
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            BmobQuery<Post> query = new BmobQuery<>();
            query.addWhereRelatedTo("mFavorite", new BmobPointer(user));
            query.findObjects(new FindListener<Post>() {
                @Override
                public void done(List<Post> list, BmobException e) {
                    if (e == null) {
                        if (list.size() > 0) {
                            for (Post post : list) {
                                if (post.getObjectId().equals(mPost.getObjectId())) {
                                    mLikeImageButton.setImageResource(R.drawable.ic_action_is_like);
                                    isFavorite = true;
                                }
                            }
                        }
                    } else {
                        //ToastFactory.show(PostDetailActivity.this, "find favorites failed!");
                        LogUtils.i(TAG,"find favorites failed!");
                    }
                }
            });
        }
    }

    private void addFavorite(){
        User currentUser = BmobUser.getCurrentUser(User.class);
        User user = new User();
        BmobRelation bmobRelation = new BmobRelation();
        bmobRelation.add(mPost);
        user.setFavorite(bmobRelation);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastFactory.show(PostDetailActivity.this, getString(R.string.favorite_succeed));
                    mLikeImageButton.setImageResource(R.drawable.ic_action_is_like);
                    isFavorite = true;
                } else {
                    ToastFactory.show(PostDetailActivity.this, getString(R.string.favorite_failed));
                }
                dismissProgressbar();
            }
        });
    }

    private void removeFavorite(){
        new AlertDialog.Builder(PostDetailActivity.this)
                .setMessage(getString(R.string.is_delete_favorite))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User currentUser = BmobUser.getCurrentUser(User.class);
                        User user = new User();
                        BmobRelation bmobRelation = new BmobRelation();
                        bmobRelation.remove(mPost);
                        user.setFavorite(bmobRelation);
                        user.update(currentUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastFactory.show(PostDetailActivity.this, getString(R.string.delete_favorite_succeed));
                                    mLikeImageButton.setImageResource(R.drawable.ic_action_not_like);
                                    isFavorite = false;
                                } else {
                                    ToastFactory.show(PostDetailActivity.this, getString(R.string.delete_favorite_failed));
                                }
                                dismissProgressbar();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create()
                .show();
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

    /**
     * 检测到未登录，跳转到登陆界面
     */
    private void gotoLogin(){
        ToastFactory.show(PostDetailActivity.this,getString(R.string.login_please));
        MessageUtils.messageDisConnect();
        Intent intent = new Intent(PostDetailActivity.this, RegisterAndLoginActivity.class);
        startActivity(intent);
    }
}
