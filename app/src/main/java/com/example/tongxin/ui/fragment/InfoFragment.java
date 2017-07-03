package com.example.tongxin.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tongxin.R;
import com.example.tongxin.ui.base.BaseFragment;
import com.example.tongxin.ui.fragment.infochildfragment.EditChildFragment;
import com.example.tongxin.ui.fragment.infochildfragment.MainChildFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * 个人信息界面父碎片
 * Created by djs on 2017/5/3.
 */

public class InfoFragment extends BaseFragment {

    FragmentManager mChildFM;

    MainChildFragment mMainChildFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChildFM = getChildFragmentManager();
        mMainChildFragment = new MainChildFragment();
        mChildFM.beginTransaction().add(R.id.frame_layout_info_container,mMainChildFragment)
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info,container,false);
        return v;
    }
}
