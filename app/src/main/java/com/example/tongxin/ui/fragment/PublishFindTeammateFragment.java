package com.example.tongxin.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tongxin.R;
import com.example.tongxin.entity.Post;
import com.example.tongxin.entity.User;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.ActivityUtils;
import com.example.tongxin.utils.CacheUtils;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.ToastFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static com.example.tongxin.utils.PictureUtils.handleImageOnKitKat;

/**
 * Created by djs on 2017/5/9.
 */

public class PublishFindTeammateFragment extends BaseFragment {

    private static final String ARG_SECTION_FIND_TEAMMATE = "section_find_teammate";

    private View mProgressView;
    private Button mAddPostPicButton;
    private Button mAddTeammateButton;
    private EditText mSubjectEditText;
    private EditText mContentEditText;
    private EditText mTeamIntroductionEditText;
    private EditText mTeammateRequireEditTex0;
    private EditText mTeammateRequireEditTex1;
    private EditText mTeammateRequireEditTex2;
    private EditText mTeammateRequireEditTex3;
    private ImageView mPreviewPostPicImageView0;
    private ImageView mPreviewPostPicImageView1;
    private ImageView mPreviewPostPicImageView2;
    private ImageButton mDeletePicImageButton0;
    private ImageButton mDeletePicImageButton1;
    private ImageButton mDeletePicImageButton2;
    private ImageButton mDeleteTeammateImageButton0;
    private ImageButton mDeleteTeammateImageButton1;
    private ImageButton mDeleteTeammateImageButton2;
    private ImageButton mDeleteTeammateImageButton3;
    private LinearLayout mTeammateLinearLayout0;
    private LinearLayout mTeammateLinearLayout1;
    private LinearLayout mTeammateLinearLayout2;
    private LinearLayout mTeammateLinearLayout3;

    private List<String> mTargetUrls;

    private String dateTime;

    private String mSection;

    public static PublishFindTeammateFragment newInstance(String section){
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_FIND_TEAMMATE,section);

        PublishFindTeammateFragment fragment = new PublishFindTeammateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSection = getArguments().getString(ARG_SECTION_FIND_TEAMMATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_publish_find_teammate, container, false);

        mTargetUrls = new ArrayList<>();
        mTargetUrls.add("");
        mTargetUrls.add("");
        mTargetUrls.add("");

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_find_teammate);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.publish_post));
        }

        mSubjectEditText = (EditText) v.findViewById(R.id.edit_text_find_teammate_subject);
        mContentEditText = (EditText) v.findViewById(R.id.edit_text_find_teammate_content);
        mTeammateRequireEditTex0 = (EditText) v.findViewById(R.id.edit_text_teammate0_require);
        mTeammateRequireEditTex1 = (EditText) v.findViewById(R.id.edit_text_teammate1_require);
        mTeammateRequireEditTex2 = (EditText) v.findViewById(R.id.edit_text_teammate2_require);
        mTeammateRequireEditTex3 = (EditText) v.findViewById(R.id.edit_text_teammate3_require);
        mTeamIntroductionEditText = (EditText) v.findViewById(R.id.edit_text_team_introduction);
        mAddPostPicButton = (Button) v.findViewById(R.id.button_add_pic_find_teammate);
        mAddTeammateButton = (Button) v.findViewById(R.id.button_add_teammate);
        mPreviewPostPicImageView0 = (ImageView) v.findViewById(R.id.image_view_pic_preview0);
        mPreviewPostPicImageView1 = (ImageView) v.findViewById(R.id.image_view_pic_preview1);
        mPreviewPostPicImageView2 = (ImageView) v.findViewById(R.id.image_view_pic_preview2);
        mDeletePicImageButton0 = (ImageButton) v.findViewById(R.id.image_button_delete_pic0);
        mDeletePicImageButton1 = (ImageButton) v.findViewById(R.id.image_button_delete_pic1);
        mDeletePicImageButton2 = (ImageButton) v.findViewById(R.id.image_button_delete_pic2);
        mDeleteTeammateImageButton0 = (ImageButton) v.findViewById(R.id.image_button_delete_teammate0);
        mDeleteTeammateImageButton1 = (ImageButton) v.findViewById(R.id.image_button_delete_teammate1);
        mDeleteTeammateImageButton2 = (ImageButton) v.findViewById(R.id.image_button_delete_teammate2);
        mDeleteTeammateImageButton3 = (ImageButton) v.findViewById(R.id.image_button_delete_teammate3);
        mTeammateLinearLayout0 = (LinearLayout) v.findViewById(R.id.linear_layout_teammate0);
        mTeammateLinearLayout1 = (LinearLayout) v.findViewById(R.id.linear_layout_teammate1);
        mTeammateLinearLayout2 = (LinearLayout) v.findViewById(R.id.linear_layout_teammate2);
        mTeammateLinearLayout3 = (LinearLayout) v.findViewById(R.id.linear_layout_teammate3);
        mProgressView = v.findViewById(R.id.view_find_teammate_progress);

        setView();
        setListener();
        return v;
    }

    private void setView(){
        mTeammateLinearLayout0.setVisibility(View.VISIBLE);
        mTeammateLinearLayout1.setVisibility(View.GONE);
        mTeammateLinearLayout2.setVisibility(View.GONE);
        mTeammateLinearLayout3.setVisibility(View.GONE);
        mDeleteTeammateImageButton0.setVisibility(View.INVISIBLE);
    }

    private void setListener() {
        mAddPostPicButton.setOnClickListener(new View.OnClickListener() {
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

        mDeleteTeammateImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeammateRequireEditTex1.setText("");
                mTeammateLinearLayout1.setVisibility(View.GONE);
            }
        });

        mDeleteTeammateImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeammateRequireEditTex2.setText("");
                mDeleteTeammateImageButton1.setVisibility(View.VISIBLE);
                mTeammateLinearLayout2.setVisibility(View.GONE);
            }
        });

        mDeleteTeammateImageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeammateRequireEditTex3.setText("");
                mDeleteTeammateImageButton2.setVisibility(View.VISIBLE);
                mTeammateLinearLayout3.setVisibility(View.GONE);
                mAddTeammateButton.setVisibility(View.VISIBLE);
            }
        });

        mAddTeammateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeammate();
            }
        });
    }

    private void addPic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.CHOOSE_PHOTO);
    }

    private void addTeammate(){
        if (mTeammateLinearLayout0.getVisibility() == View.VISIBLE
                && mTeammateLinearLayout1.getVisibility() == View.GONE){
            mDeleteTeammateImageButton0.setVisibility(View.INVISIBLE);
            mTeammateLinearLayout1.setVisibility(View.VISIBLE);
        }else if (mTeammateLinearLayout1.getVisibility() == View.VISIBLE
                && mTeammateLinearLayout2.getVisibility() == View.GONE){
            mDeleteTeammateImageButton1.setVisibility(View.INVISIBLE);
            mTeammateLinearLayout2.setVisibility(View.VISIBLE);
        }else if (mTeammateLinearLayout2.getVisibility() == View.VISIBLE
                && mTeammateLinearLayout3.getVisibility() == View.GONE){
            mDeleteTeammateImageButton2.setVisibility(View.INVISIBLE);
            mTeammateLinearLayout3.setVisibility(View.VISIBLE);
            mAddTeammateButton.setVisibility(View.GONE);
        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.publish_post_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityUtils.closeSoftInput(mContext, getView());
                getActivity().finish();
                return true;
            case R.id.menu_item_publish_post:
                String subject = mSubjectEditText.getText().toString();
                String content = mContentEditText.getText().toString();
                String teammate0 = mTeammateRequireEditTex0.getText().toString();
                String selfIntroduction = mTeamIntroductionEditText.getText().toString();
                if (TextUtils.isEmpty(subject)) {
                    mSubjectEditText.setError(getString(R.string.empty_subject));
                } else if (TextUtils.isEmpty(content)) {
                    mContentEditText.setError(getString(R.string.empty_content));
                }else if (TextUtils.isEmpty(teammate0)){
                    mTeammateRequireEditTex0.setError(getString(R.string.error_teammate));
                } else {
                    showProgressbar();
                    if (mTargetUrls.get(0).equals("")) {
                        publishPostWithoutPic(subject, content,selfIntroduction, null);
                    } else {
                        publishPost(subject, content,selfIntroduction);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
            mDeletePicImageButton0.setVisibility(View.VISIBLE);
        }
        if (!mTargetUrls.get(1).equals("")) {
            mDeletePicImageButton1.setVisibility(View.VISIBLE);
        }
        if (!mTargetUrls.get(2).equals("")) {
            mDeletePicImageButton2.setVisibility(View.VISIBLE);
        }
        mPreviewPostPicImageView0.setImageBitmap(compressImageFromFile(mTargetUrls.get(0)));
        mPreviewPostPicImageView1.setImageBitmap(compressImageFromFile(mTargetUrls.get(1)));
        mPreviewPostPicImageView2.setImageBitmap(compressImageFromFile(mTargetUrls.get(2)));
    }

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

    private String saveToSdCard(Bitmap bitmap) throws IOException {
        String files = CacheUtils.getPostPicCacheDir(getActivity(), dateTime + ".jpg");
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

    private int picNum = 0;
    private void publishPost(final String subject, final String content,final String selfIntroduction) {
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
                    publishPostWithoutPic(subject,content,selfIntroduction,list);
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

    private void publishPostWithoutPic(final String subject, final String content,String selfIntroduction,
                                       final List<BmobFile> list) {
        Post post = new Post();

        if (list != null) {
            if (list.size() == 1) {
                post.setImage0(list.get(0));
            }
            if (list.size() == 2) {
                post.setImage0(list.get(0));
                post.setImage1(list.get(1));
            }
            if (list.size() == 3) {
                post.setImage0(list.get(0));
                post.setImage1(list.get(1));
                post.setImage2(list.get(2));
            }
        }
        post.setUser(BmobUser.getCurrentUser(User.class));
        post.setPostSubject(subject);
        post.setPostContent(content);
        post.setIntroduction(selfIntroduction);
        post.setReadNum(0);
        post.setCommentNum(0);
        post.setPostType(Constants.FIND_TEAMMATE);
        post.setSection(mSection);
        post.setLastCommentTime(new Date());
        post.setTeammate0(mTeammateRequireEditTex0.getText().toString());
        post.setTeammate1(mTeammateRequireEditTex1.getText().toString());
        post.setTeammate2(mTeammateRequireEditTex2.getText().toString());
        post.setTeammate3(mTeammateRequireEditTex3.getText().toString());
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    LogUtils.i(TAG, "publish succeed!");
                    dismissProgressbar();
                    ActivityUtils.closeSoftInput(mContext, getView());
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else {
                    LogUtils.i(TAG, "publish failed:" + e.getMessage());
                }
            }
        });
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
