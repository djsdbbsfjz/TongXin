package com.example.tongxin.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tongxin.R;
import com.example.tongxin.ui.base.BaseFragment;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;

import static com.example.tongxin.message.NotifyMessageHandler.updateMessage;
import static com.example.tongxin.message.NotifyMessageHandler.sMessageAdapter;

/**
 *
 * 消息界面逻辑
 * Created by djs on 2017/5/3.
 */

public class MessageFragment extends BaseFragment implements MessageListHandler{

    private RecyclerView mRecyclerView;
    private View mProgressView;
    private TextView mTextView;
    private Button mButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message,container,false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.message_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_message);
        mProgressView = v.findViewById(R.id.view_message_progress);
        mTextView = (TextView) v.findViewById(R.id.text_view);
        mButton = (Button) v.findViewById(R.id.button);

        setListener();
        setView();
        return v;
    }

    private void setView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(sMessageAdapter);
    }

    private void setListener(){
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMessage();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        updateMessage();
        BmobIM.getInstance().addMessageListHandler(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BmobIM.getInstance().removeMessageListHandler(this);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
    }
}
