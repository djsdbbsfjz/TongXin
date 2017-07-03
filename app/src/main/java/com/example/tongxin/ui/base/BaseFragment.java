package com.example.tongxin.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.tongxin.entity.User;
import com.example.tongxin.proxy.UserProxy;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.Sputil;

/**
 * Created by djs on 2017/5/2.
 */

public abstract class BaseFragment extends Fragment {

    protected static String TAG ;

    protected Context mContext;

    protected Sputil sputil;

    protected UserProxy mUserProxy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        TAG = this.getClass().getSimpleName();

        mContext = getActivity();

        if(null == sputil){
            sputil = new Sputil(mContext, Constants.PRE_NAME);
        }

        mUserProxy = new UserProxy(mContext);
    }
}
