package com.example.tongxin.ui.fragment.infochildfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.tongxin.R;
import com.example.tongxin.entity.User;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.ActivityUtils;
import com.example.tongxin.utils.CacheUtils;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.PictureUtils;
import com.example.tongxin.utils.ToastFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by djs on 2017/5/5.
 */

public class EditChildFragment extends BaseFragment{

    private User currentUser;

    private CircleImageView mAvatarImageView;
    EditText mNewNickNameEditText;
    RadioGroup mNewSexRadioGroup;
    EditText mNewCollegeInfoEditText;
    EditText mNewContactWayEditText;
    EditText mNewSelfIntroductionEditText;
    private RelativeLayout mRelativeLayout;

    private String dateTime;
    private String mNewNickName;
    private String mNewSex;
    private String mNewCollegeInfo;
    private String mNewContactWay;
    private String mNewSelfIntroduction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = BmobUser.getCurrentUser(User.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cf_info_edit, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_child_info_edit);
        toolbar.setTitle(getString(R.string.edit_info));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAvatarImageView = (CircleImageView) v.findViewById(R.id.circle_image_view_edit_avatar);
        mNewSexRadioGroup = (RadioGroup) v.findViewById(R.id.radio_group_edit_sex);
        mNewNickNameEditText = (EditText) v.findViewById(R.id.edit_text_edit_nick_name);
        mNewCollegeInfoEditText = (EditText) v.findViewById(R.id.edit_text_edit_college_info);
        mNewContactWayEditText = (EditText) v.findViewById(R.id.edit_text_edit_contact_way);
        mNewSelfIntroductionEditText = (EditText) v.findViewById(R.id.edit_text_edit_self_introduction);
        mRelativeLayout = (RelativeLayout) v.findViewById(R.id.relative_layout_bg);

        setView();
        setListener();
        return v;
    }

    private void setView() {
        String oldSex = currentUser.getSex();
        if (oldSex == Constants.SEX_FEMALE){
            mNewSexRadioGroup.check(R.id.radio_button_edit_sex_female);
        }else if (oldSex == Constants.SEX_MALE){
            mNewSexRadioGroup.check(R.id.radio_button_edit_sex_male);
        }
        PictureUtils.loadUserAvatar(getActivity(),mAvatarImageView);
        mNewNickNameEditText.setText(currentUser.getNickname());
        mNewCollegeInfoEditText.setText(currentUser.getCollegeInformation());
        mNewContactWayEditText.setText(currentUser.getContactWay());
        mNewSelfIntroductionEditText.setText(currentUser.getSelfIntroduction());
    }

    private void setListener() {
        mAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.change_avatar))
                        .setPositiveButton(R.string.album, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Date date = new Date(System.currentTimeMillis());
                                dateTime = date.getTime() + "";
                                getAvatarFromAlbum();
                            }
                        })
                        .setNegativeButton(R.string.take_photo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Date date = new Date(System.currentTimeMillis());
                                dateTime = date.getTime() + "";
                                getAvatarFromCamera();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    private void saveInfo(){
        mNewNickName = mNewNickNameEditText.getText().toString();
        mNewCollegeInfo = mNewCollegeInfoEditText.getText().toString();
        mNewContactWay = mNewContactWayEditText.getText().toString();
        mNewSelfIntroduction = mNewSelfIntroductionEditText.getText().toString();
        int checkId = mNewSexRadioGroup.getCheckedRadioButtonId();
        if (checkId == R.id.radio_button_edit_sex_female){
            mNewSex = Constants.SEX_FEMALE;
        }else if (checkId == R.id.radio_button_edit_sex_male){
            mNewSex = Constants.SEX_MALE;
        }else {
            mNewSex = "";
        }

        User newUser = new User();
        if (!mNewNickName.equals("")){
            newUser.setNickname(mNewNickName);
        }
        if (!mNewSex.equals("")){
            newUser.setSex(mNewSex);
        }
        if (!mNewCollegeInfo.equals("")){
            newUser.setCollegeInformation(mNewCollegeInfo);
        }
        if (!mNewContactWay.equals("")){
            newUser.setContactWay(mNewContactWay);
        }
        if (!mNewSelfIntroduction.equals("")){
            newUser.setSelfIntroduction(mNewSelfIntroduction);
        }

        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    ToastFactory.show(mContext,getString(R.string.update_user_info_succeed));
                    LogUtils.i(TAG, "update user info succeed:" + currentUser.getNickname());
                    ActivityUtils.closeSoftInput(mContext,getView());
                    getFragmentManager().popBackStack();
                }else {
                    ToastFactory.show(mContext,getString(R.string.update_user_info_failed));
                    LogUtils.i(TAG, "update user info failed:" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_info_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityUtils.closeSoftInput(mContext,getView());
                getFragmentManager().popBackStack();
                return true;

            case R.id.menu_item_save_info:
                saveInfo();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    String iconUrl;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String files = CacheUtils.getAvatarCacheDir(getActivity(),dateTime);
                    File file = new File(files);
                    BitmapFactory.decodeFile(files);
                    if (file.exists() && file.length() > 0) {
                        Uri uri = Uri.fromFile(file);
                        startPhotoZoom(uri);
                    } else {
                    }
                }
                break;

            case Constants.CHOOSE_PHOTO:
                if (data == null) {
                    return;
                } else {
                    startPhotoZoom(data.getData());
                }
                break;

            case Constants.PHOTO_ZOOM:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        final Bitmap bitmap = extras.getParcelable("data");
                        mAvatarImageView.setImageBitmap(bitmap);
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    iconUrl = saveToSdCard(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.run();
                        saveAvatar();
                    }
                }
                break;
        }
    }

    private void getAvatarFromCamera() {
        File f = new File(CacheUtils.getAvatarCacheDir(getActivity(),dateTime));
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(f);

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(camera, Constants.TAKE_PHOTO);
    }

    private void getAvatarFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.CHOOSE_PHOTO);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 120);
        intent.putExtra("outputY", 120);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constants.PHOTO_ZOOM);
    }

    private void saveAvatar() {
        if (iconUrl != null) {
            final BmobFile file = new BmobFile(new File(iconUrl));
            file.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        LogUtils.i(TAG, "upload avatar succeed:" + file.getFileUrl());
                        currentUser.setAvatar(file);
                        currentUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastFactory.show(getActivity(), getString(R.string.succeed_update_avatar));
                                    LogUtils.i(TAG, "update avatar succeed:" + currentUser.getNickname());
                                } else {
                                    LogUtils.i(TAG, "update avatar failed:" + e.getMessage());
                                }
                            }
                        });
                    } else {
                        LogUtils.i(TAG, "upload avatar failed:" + e.getMessage());
                    }
                }
            });
        }
    }

    private String saveToSdCard(Bitmap bitmap) throws IOException {
        String files = CacheUtils.getAvatarCacheDir(getActivity(),dateTime);
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
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
}
