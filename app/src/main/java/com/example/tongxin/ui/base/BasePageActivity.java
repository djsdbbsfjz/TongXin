package com.example.tongxin.ui.base;

import android.os.Bundle;

import com.example.tongxin.utils.Constants;

import cn.bmob.v3.Bmob;

/**
 * Created by djs on 2017/5/1.
 */

public abstract class BasePageActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bmob.initialize(this, Constants.BMOB_APP_ID);
        init(bundle);
    }

    private void init(Bundle bundle) {
        // TODO Auto-generated method stub
        initViews();
        setupViews(bundle);
        setListener();
        fetchData();
    }

    protected abstract void initViews();

    protected abstract void setupViews(Bundle bundle);

    protected abstract void setListener();

    protected abstract void fetchData();
}
