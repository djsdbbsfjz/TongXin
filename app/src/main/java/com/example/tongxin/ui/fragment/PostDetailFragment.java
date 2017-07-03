package com.example.tongxin.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tongxin.R;
import com.example.tongxin.adapter.CommentAdapter;
import com.example.tongxin.entity.Comment;
import com.example.tongxin.entity.Post;
import com.example.tongxin.entity.User;
import com.example.tongxin.message.MessageUtils;
import com.example.tongxin.ui.activity.RegisterAndLoginActivity;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.CacheUtils;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.ToastFactory;
import com.example.tongxin.utils.ViewUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static com.example.tongxin.utils.PictureUtils.handleImageOnKitKat;

/**
 *
 * 帖子详细界面逻辑
 * Created by djs on 2017/5/3.
 */

public class PostDetailFragment extends BaseFragment {

    private static final String TAG = "PostDetailFragment";

    private static final String ARG_POST = "post";

    private Post mPost;

    private User mCurrentUser;

    private List<Comment> mComments;

    private int mPageNum;

    private CommentAdapter mCommentAdapter;

    private List<String> mTargetUrls;

    private String dateTime;

    private RecyclerView mCommentsRecyclerView;
    private EditText mSendContentEditView;
    private Button mSendCommentButton;
    private View mProgressView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageButton mAddCommentPicButton;
    private ImageView mPreviewCommentPicImageView0;
    private ImageView mPreviewCommentPicImageView1;
    private ImageView mPreviewCommentPicImageView2;
    private ImageButton mDeletePicImageButton0;
    private ImageButton mDeletePicImageButton1;
    private ImageButton mDeletePicImageButton2;
    private LinearLayout mCommentPicPreviewLinearLayout;

    private LinearLayoutManager mLinearLayoutManager;

    public enum RefreshType{
        REFRESH,LOAD_MORE
    }

    private RefreshType mRefreshType = RefreshType.LOAD_MORE;


    public static PostDetailFragment newInstance(Post post){
        Bundle args = new Bundle();
        args.putSerializable(ARG_POST,post);

        PostDetailFragment fragment = new PostDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Post post = (Post) getArguments().getSerializable(ARG_POST);
        mPost = post;

        mPageNum = 0;
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_detail,container,false);

        mSendContentEditView = (EditText) v.findViewById(R.id.edit_text_send_content);
        mSendCommentButton = (Button) v.findViewById(R.id.button_send_comment);
        mCommentsRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_comments_container);
        mProgressView = v.findViewById(R.id.view_post_detail_progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout_post_detail);
        mAddCommentPicButton = (ImageButton) v.findViewById(R.id.image_button_comment_insert_pic);
        mPreviewCommentPicImageView0 = (ImageView) v.findViewById(R.id.image_view_post_detail_pic_preview0);
        mPreviewCommentPicImageView1 = (ImageView) v.findViewById(R.id.image_view_post_detail_pic_preview1);
        mPreviewCommentPicImageView2 = (ImageView) v.findViewById(R.id.image_view_post_detail_pic_preview2);
        mDeletePicImageButton0 = (ImageButton) v.findViewById(R.id.image_button_post_detail_delete_pic0);
        mDeletePicImageButton1 = (ImageButton) v.findViewById(R.id.image_button_post_detail_delete_pic1);
        mDeletePicImageButton2 = (ImageButton) v.findViewById(R.id.image_button_post_detail_delete_pic2);
        mCommentPicPreviewLinearLayout = (LinearLayout) v.findViewById(R.id.linear_layout_comment_pic_preview);

        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mComments = new ArrayList<>();
        mCommentAdapter = new CommentAdapter(mComments,mPost);
        mCommentsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCommentsRecyclerView.setAdapter(mCommentAdapter);
        if (mComments.size() == 0){
            showProgressbar();
            fetchData();
        }

        mTargetUrls = new ArrayList<>();
        mTargetUrls.add("");
        mTargetUrls.add("");
        mTargetUrls.add("");

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        mCurrentUser = mUserProxy.getCurrentUser();
        setListener();
        setupViews();
    }

    private void setupViews() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    public void setPost(Post post) {
        mPost = post;
    }

    private void setListener(){
        if (mCurrentUser == null){
            mSendCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoLogin();
                }
            });
            mAddCommentPicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoLogin();
                }
            });
        }else {
            mSendCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = mSendContentEditView.getText().toString();
                    if (!content.equals("")) {
                        showProgressbar();
                        if (mTargetUrls.get(0).equals("")) {
                            publishCommentWithoutPic(null);
                        } else {
                            publishComment();
                        }
                    }else {
                        mSendContentEditView.setError(getString(R.string.empty_content));
                    }
                }
            });

            mAddCommentPicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date = new Date(System.currentTimeMillis());
                    dateTime = date.getTime() + "";
                    if (!mTargetUrls.get(0).equals("") && !mTargetUrls.get(1).equals("")
                            && !mTargetUrls.get(2).equals("")) {
                        ToastFactory.show(mContext, getString(R.string.max_pic_num));
                    } else {
                        addPic();
                    }
                }
            });
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshType = RefreshType.REFRESH;
                mPageNum = 0;
                fetchData();
            }
        });

        mCommentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (ViewUtils.isSlideToBottom(recyclerView)) {
                    mRefreshType = RefreshType.LOAD_MORE;
                    fetchData();
                }
            }
        });

        mDeletePicImageButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = mTargetUrls.get(0);
                if (!mTargetUrls.get(1).equals("") && mTargetUrls.get(2).equals("")) {
                    mTargetUrls.set(0, mTargetUrls.get(1));
                    mTargetUrls.set(1, "");
                    mDeletePicImageButton1.setVisibility(View.GONE);
                } else if (!mTargetUrls.get(1).equals("") && !mTargetUrls.get(2).equals("")) {
                    mTargetUrls.set(0, mTargetUrls.get(1));
                    mTargetUrls.set(1, mTargetUrls.get(2));
                    mTargetUrls.set(2, "");
                    mDeletePicImageButton2.setVisibility(View.GONE);
                } else {
                    mTargetUrls.set(0, "");
                    mDeletePicImageButton0.setVisibility(View.GONE);
                }
                updatePreview();
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        });

        mDeletePicImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = mTargetUrls.get(1);
                if (!mTargetUrls.get(2).equals("")) {
                    mTargetUrls.set(1, mTargetUrls.get(2));
                    mTargetUrls.set(2, "");
                    mDeletePicImageButton2.setVisibility(View.GONE);
                } else {
                    mTargetUrls.set(1, "");
                    mDeletePicImageButton1.setVisibility(View.GONE);
                }
                updatePreview();
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        });

        mDeletePicImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeletePicImageButton2.setVisibility(View.GONE);
                String path = mTargetUrls.get(2);
                mTargetUrls.set(0, "");
                updatePreview();
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        });
    }

    private void fetchData() {
        BmobQuery<Comment> eq1 = new BmobQuery<>();
        eq1.addWhereEqualTo("mPost",mPost);
        BmobQuery<Comment> eq2 = new BmobQuery<>();
        eq2.addWhereEqualTo("mParentId", Constants.DEF_PARENT_COMMENT_ID);

        List<BmobQuery<Comment>> andQueries = new ArrayList<>();
        andQueries.add(eq1);
        andQueries.add(eq2);

        BmobQuery<Comment> query = new BmobQuery<>();
        query.and(andQueries)
                .setLimit(Constants.NUMBERS_PER_PAGE)
                .setSkip(Constants.NUMBERS_PER_PAGE * (mPageNum++))
                .order("createdAt")
                .include("mUser");

        LogUtils.i(TAG , "SIZE:" + Constants.NUMBERS_PER_PAGE * mPageNum);

        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    LogUtils.i(TAG,"find success." + list.size());
                    if(list.size() != 0 && list.get(list.size() - 1) != null){
                        if(mRefreshType == RefreshType.REFRESH){
                            mComments.clear();
                        }
                        if(list.size() < Constants.NUMBERS_PER_PAGE){
                            LogUtils.i(TAG,"load all comments");
                        }
                        mComments.addAll(list);
                        mCommentAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        dismissProgressbar();
                    } else{
                        mPageNum--;
                        mSwipeRefreshLayout.setRefreshing(false);
                        dismissProgressbar();
                    }
                } else {
                    mPageNum--;
                    LogUtils.i(TAG,"find failed." + e.getMessage());
                    mSwipeRefreshLayout.setRefreshing(false);
                    dismissProgressbar();
                }
            }
        });
    }

    /**
     * 调用相册添加图片，最多三张
     */
    private void addPic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.CHOOSE_PHOTO:
                if (data == null) {
                    return;
                } else {
                    String path = handleImageOnKitKat(mContext,data);
                    showPreviewPic(path);
                }
                break;
        }
    }

    /**
     * 在imageview中加载预览图片
     * @param path
     */
    private void showPreviewPic(String path) {
        Bitmap bitmap = compressImageFromFile(path);
        try {
            String savePath = saveToSdCard(bitmap);
            if (savePath != null) {
                if (mTargetUrls.get(0).equals("")) {
                    mTargetUrls.set(0, savePath);
                } else if (mTargetUrls.get(1).equals("")) {
                    mTargetUrls.set(1, savePath);
                    mDeletePicImageButton1.setVisibility(View.VISIBLE);
                } else if (mTargetUrls.get(2).equals("")) {
                    mTargetUrls.set(2, savePath);
                    mDeletePicImageButton2.setVisibility(View.VISIBLE);
                }
            }
        } catch (IOException e) {
        }
        updatePreview();
    }

    private void updatePreview() {
        if (!mTargetUrls.get(0).equals("")) {
            mCommentPicPreviewLinearLayout.setVisibility(View.VISIBLE);
            mDeletePicImageButton0.setVisibility(View.VISIBLE);
        }else {
            mCommentPicPreviewLinearLayout.setVisibility(View.GONE);
        }
        if (!mTargetUrls.get(1).equals("")) {
            mDeletePicImageButton1.setVisibility(View.VISIBLE);
        }
        if (!mTargetUrls.get(2).equals("")) {
            mDeletePicImageButton2.setVisibility(View.VISIBLE);
        }

        mPreviewCommentPicImageView0.setImageBitmap(compressImageFromFile(mTargetUrls.get(0)));
        mPreviewCommentPicImageView1.setImageBitmap(compressImageFromFile(mTargetUrls.get(1)));
        mPreviewCommentPicImageView2.setImageBitmap(compressImageFromFile(mTargetUrls.get(2)));
    }

    /**
     * 压缩图片
     * @param srcPath
     * @return
     */
    private Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * 保存到sd卡
     * @param bitmap
     * @return
     * @throws IOException
     */
    private String saveToSdCard(Bitmap bitmap) throws IOException {
        String files = CacheUtils.getCommentPicCacheDir(getActivity(), dateTime + ".jpg");
        File file = new File(files);
        BufferedOutputStream os = null;
        try {
            int end = files.lastIndexOf(File.separator);
            String _filePath = files.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 上传带图片的评论
     */
    private int picNum = 0;
    private void publishComment() {
        for (String path : mTargetUrls){
            if (!path.equals("")){
                picNum++;
            }
        }
        String[] paths = new String[picNum];
        for (int i = 0;i < picNum;i++){
            paths[i] = mTargetUrls.get(i);
        }

        BmobFile.uploadBatch(paths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list.size() == picNum){
                    publishCommentWithoutPic(list);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 上传不带图片评论
     * @param list
     */
    private void publishCommentWithoutPic(List<BmobFile> list){
        final Comment comment = new Comment();

        if (list != null) {
            if (list.size() == 1) {
                comment.setImage0(list.get(0));
            }
            if (list.size() == 2) {
                comment.setImage0(list.get(0));
                comment.setImage1(list.get(1));
            }
            if (list.size() == 3) {
                comment.setImage0(list.get(0));
                comment.setImage1(list.get(1));
                comment.setImage2(list.get(2));
            }
        }

        comment.setUser(BmobUser.getCurrentUser(User.class));
        comment.setPost(mPost);
        comment.setContent(mSendContentEditView.getText().toString());
        comment.setParentId(Constants.DEF_PARENT_COMMENT_ID);
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    LogUtils.i(TAG,"comment succeed!");
                    Post post = new Post();
                    post.setLastCommentTime(new Date());
                    post.increment("mCommentNum");
                    post.update(mPost.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                LogUtils.i(TAG,"last comment time alter succeed!");
                            }else {
                                LogUtils.i(TAG,"last comment time alter failed" + e.getMessage());
                            }
                        }
                    });
                    mRefreshType = RefreshType.REFRESH;
                    mPageNum = 0;
                    if(!BmobUser.getCurrentUser().getObjectId().equals(mPost.getUser().getObjectId())) {
                        MessageUtils.sendMessage(mContext, mPost.getUser(), comment,Constants.sMessageType[0]);
                    }
                    fetchData();
                }else {
                    ToastFactory.show(mContext,getString(R.string.failed_comment));
                    LogUtils.i(TAG,"comment failed" + e.getMessage());
                }
            }
        });
        mSendContentEditView.setText("");
        mTargetUrls.set(0,"");
        mTargetUrls.set(1,"");
        mTargetUrls.set(2,"");
        updatePreview();
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

    private void gotoLogin(){
        ToastFactory.show(getActivity(),getString(R.string.login_please));
        Intent intent = new Intent(getActivity(), RegisterAndLoginActivity.class);
        startActivity(intent);
    }
}
