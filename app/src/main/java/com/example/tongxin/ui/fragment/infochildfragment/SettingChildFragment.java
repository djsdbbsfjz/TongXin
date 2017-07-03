package com.example.tongxin.ui.fragment.infochildfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tongxin.R;
import com.example.tongxin.message.NotifyMessageHandler;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.utils.CacheUtils;
import com.example.tongxin.utils.ToastFactory;

import java.io.File;

import cn.bmob.v3.BmobUser;

/**
 * Created by djs on 2017/5/18.
 */

public class SettingChildFragment extends BaseFragment {

    private Button mClearCacheButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cf_info_setting, container, false);
        mClearCacheButton = (Button) v.findViewById(R.id.button_clear_cache);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_child_info_setting);
        toolbar.setTitle(getString(R.string.setting));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setListener();
        return v;
    }

    private void setListener(){
        mClearCacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.ensure_clear_cache))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String postPic = CacheUtils.getCacheDirectory(getActivity(), true, "pic") + "/post";
                                File postFile = new File(postPic);
                                deleteAllFilesOfDir(postFile);
                                String commentPic = CacheUtils.getCacheDirectory(getActivity(), true, "pic") + "/comment";
                                File commentFile = new File(commentPic);
                                deleteAllFilesOfDir(commentFile);
                                ToastFactory.show(mContext, getString(R.string.clear_cache_succeed));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .create()
                        .show();
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

    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }
}
