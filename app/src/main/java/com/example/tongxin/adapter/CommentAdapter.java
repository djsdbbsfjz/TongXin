package com.example.tongxin.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.example.tongxin.R;
import com.example.tongxin.entity.Comment;
import com.example.tongxin.entity.Post;
import com.example.tongxin.entity.User;
import com.example.tongxin.message.MessageUtils;
import com.example.tongxin.ui.activity.PersonalInfoActivity;
import com.example.tongxin.ui.activity.RegisterAndLoginActivity;
import com.example.tongxin.utils.Constants;
import com.example.tongxin.utils.LogUtils;
import com.example.tongxin.utils.PictureUtils;
import com.example.tongxin.utils.StringUtils;
import com.example.tongxin.utils.ToastFactory;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * 查看帖子时加载帖子对应的评论，以及字评论
 * Created by djs on 2017/5/3.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "CommentAdapter";

    private List<Comment> mComments;

    private Post mPost;

    private enum ITEM_TYPE{
        POST_DETAIL_TYPE,//头布局
        COMMENT_TYPE//评论布局
    }

    /**
     *
     * @param comments 通过查询服务器得到的评论表
     * @param post  帖子
     */
    public CommentAdapter(List<Comment> comments, Post post){
        mComments = comments;
        mPost = post;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder{

        private Post mPost;
        private Comment mComment;
        private int mPosition;
        private boolean isShowAllReply = false;
        List<Comment> comments;

        private ReplyOfCommentAdapter mReplyOfCommentAdapter;

        private Button sendReplyButton;
        private EditText replyContentEditView;

        private TextView mUserNameTextView;
        private TextView mContentTextView;
        private TextView mCommentTimeTextView;
        private TextView mFloorNumTextView;
        private ImageButton mReplyCommentImageButton;
        private LinearLayout mReplyContainerLinearLayout;
        private RecyclerView mReplyCommentRecyclerView;
        private TextView mAuthorLabelTextView;
        private Button mShowAllReplyButton;
        private CircleImageView mAvatarImageView;
        private LinearLayout mCommentImageLinearLayout;
        private ImageView mCommentPicImageView0;
        private ImageView mCommentPicImageView1;
        private ImageView mCommentPicImageView2;

        public CommentViewHolder(View view) {
            super(view);

            mUserNameTextView = (TextView) view.findViewById(R.id.text_view_comment_user_name);
            mContentTextView = (TextView) view.findViewById(R.id.text_view_post_title);
            mCommentTimeTextView = (TextView) view.findViewById(R.id.text_view_post_time);
            mFloorNumTextView = (TextView) view.findViewById(R.id.text_view_floor_num);
            mReplyCommentImageButton = (ImageButton) view.findViewById(R.id.image_button_reply_comment);
            mAuthorLabelTextView = (TextView) view.findViewById(R.id.text_view_author_label);
            mShowAllReplyButton = (Button) view.findViewById(R.id.button_show_all_reply);
            mAvatarImageView = (CircleImageView) view.findViewById(R.id.image_view_comment_user_head_picture);
            mCommentImageLinearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_comment_image);
            mCommentPicImageView0 = (ImageView) view.findViewById(R.id.image_view_comment_image0);
            mCommentPicImageView1 = (ImageView) view.findViewById(R.id.image_view_comment_image1);
            mCommentPicImageView2 = (ImageView) view.findViewById(R.id.image_view_comment_image2);
            mReplyContainerLinearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_reply_container);
            mReplyCommentRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_comment_reply_container);

            mReplyCommentImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(BmobUser.getCurrentUser() == null){
                        gotoLogin();
                    }else {
                        editReply();
                    }
                }
            });

            mShowAllReplyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowAllReply == false){
                        fetchAllReply();
                        isShowAllReply = true;
                        mShowAllReplyButton.setText("收起回复");
                    }else {
                        fetchTwoReply();
                        isShowAllReply = false;
                        mShowAllReplyButton.setText("查看全部回复");
                    }
                }
            });

            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mComment.getUser() != null) {
                        Intent intent = PersonalInfoActivity.newIntent(v.getContext(), mComment.getUser());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bindCommentView(Post post,final Comment comment, int position) {
            mPost = post;
            mComment = comment;
            mPosition = position;

            mReplyCommentRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            PictureUtils.loadAuthorAvatar(itemView.getContext(),mAvatarImageView,comment.getUser());

            mUserNameTextView.setText(mComment.getUser().getNickname());
            mContentTextView.setText(mComment.getContent());
            if (position == 1){
                mFloorNumTextView.setBackgroundColor(Color.parseColor("#BDBDBD"));//灰色背景
                mFloorNumTextView.setText("沙发");
                mFloorNumTextView.setTextColor(Color.parseColor("#FFFFFF"));//白色字体
            }else if (position == 2){
                mFloorNumTextView.setBackgroundColor(Color.parseColor("#BDBDBD"));
                mFloorNumTextView.setText("板凳");
                mFloorNumTextView.setTextColor(Color.parseColor("#FFFFFF"));
            }else if (position == 3){
                mFloorNumTextView.setBackgroundColor(Color.parseColor("#BDBDBD"));
                mFloorNumTextView.setText("地板");
                mFloorNumTextView.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                mFloorNumTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));//白色背景
                mFloorNumTextView.setText("第" + position + "楼");
                mFloorNumTextView.setTextColor(Color.parseColor("#757575"));//
            }
            mCommentTimeTextView.setText("  " + StringUtils.timeDiff(StringUtils.string2date(comment.getCreatedAt())));
            if (post.getUser().getObjectId().equals(comment.getUser().getObjectId())) {
                mAuthorLabelTextView.setVisibility(View.VISIBLE);
            } else {
                mAuthorLabelTextView.setVisibility(View.GONE);
            }

            if (comment.getImage0() != null){
                mCommentImageLinearLayout.setVisibility(View.VISIBLE);
                PictureUtils.loadCommentPic(itemView.getContext(), mCommentPicImageView0,comment.getImage0());
                if (comment.getImage1() != null){
                    PictureUtils.loadCommentPic(itemView.getContext(), mCommentPicImageView1,comment.getImage1());
                    if (comment.getImage1() != null){
                        PictureUtils.loadCommentPic(itemView.getContext(), mCommentPicImageView2,comment.getImage2());
                    }else {
                        mCommentPicImageView2.setVisibility(View.GONE);
                    }
                }else {
                    mCommentPicImageView1.setVisibility(View.GONE);
                    mCommentPicImageView2.setVisibility(View.GONE);
                }
            }else {
                mCommentImageLinearLayout.setVisibility(View.GONE);
            }

            comments = new ArrayList<>();
            mReplyOfCommentAdapter = new ReplyOfCommentAdapter(comments);
            mReplyCommentRecyclerView.setAdapter(mReplyOfCommentAdapter);
            if (comments.size() == 0) {
                fetchTwoReply();
            }
        }

        /**
         * 首先加载两条子评论，若有更多子评论，通过点击按钮加载更多子评论
         */
        private void fetchTwoReply() {
            comments.clear();
            BmobQuery<Comment> eq1 = new BmobQuery<>();
            eq1.addWhereEqualTo("mPost",mPost);
            BmobQuery<Comment> eq2 = new BmobQuery<>();
            eq2.addWhereEqualTo("mParentId",mComment.getObjectId());

            List<BmobQuery<Comment>> andQueries = new ArrayList<>();
            andQueries.add(eq1);
            andQueries.add(eq2);

            BmobQuery<Comment> query = new BmobQuery<>();
            query.and(andQueries)
                    .setLimit(3)
                    .order("createdAt")
                    .include("mUser");
            query.findObjects(new FindListener<Comment>() {
                @Override
                public void done(List<Comment> list, BmobException e) {
                    if (e == null){
                        if (list.size() < 3){
                            comments.addAll(list);
                        }else {
                            comments.add(list.get(0));
                            comments.add(list.get(1));
                        }

                        LogUtils.i(TAG,"query reply of comment succeed");
                        updateReplyUI(list.size());
                        mReplyOfCommentAdapter.notifyDataSetChanged();
                    }else {
                        LogUtils.i(TAG,"query reply of comment failed" + e.getMessage());
                    }
                }
            });
        }

        /**
         * 获取所有子评论
         */
        private void fetchAllReply(){
            comments.clear();
            BmobQuery<Comment> eq1 = new BmobQuery<>();
            eq1.addWhereEqualTo("mPost",mPost);
            BmobQuery<Comment> eq2 = new BmobQuery<>();
            eq2.addWhereEqualTo("mParentId",mComment.getObjectId());

            List<BmobQuery<Comment>> andQueries = new ArrayList<>();
            andQueries.add(eq1);
            andQueries.add(eq2);

            BmobQuery<Comment> query = new BmobQuery<>();
            query.and(andQueries)
                    .order("createdAt")
                    .include("mUser");
            query.findObjects(new FindListener<Comment>() {
                @Override
                public void done(List<Comment> list, BmobException e) {
                    if (e == null){
                        LogUtils.i(TAG,"query reply of comment succeed");
                        comments.addAll(list);
                        updateReplyUI(list.size());
                        mReplyOfCommentAdapter.notifyDataSetChanged();
                    }else {
                        LogUtils.i(TAG,"query reply of comment failed" + e.getMessage());
                    }
                }
            });
        }

        private void updateReplyUI(int size){
            if(size == 0){
                mReplyContainerLinearLayout.setVisibility(View.GONE);
            }else {
                mReplyContainerLinearLayout.setVisibility(View.VISIBLE);
                if (size < 3){
                    mShowAllReplyButton.setVisibility(View.GONE);
                } else {
                    mShowAllReplyButton.setVisibility(View.VISIBLE);
                }
            }
        }

        /**
         * 对某一楼层进行评论，弹出一个对话框
         */
        private void editReply(){
            View v = LayoutInflater.from(itemView.getContext()).inflate(R.layout.dialog_edit_reply,null);

            final AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext()).setView(v)
                    .setTitle("回复第" + mPosition + "楼:")
                    .create();

            replyContentEditView = (EditText) v.findViewById(R.id.edit_text_reply_content);
            replyContentEditView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0){
                        sendReplyButton.setEnabled(false);
                    }else {
                        sendReplyButton.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            sendReplyButton = (Button) v.findViewById(R.id.button_send_reply);
            sendReplyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = replyContentEditView.getText().toString();
                    final Comment newComment = new Comment();
                    newComment.setUser(BmobUser.getCurrentUser(User.class));
                    newComment.setPost(mPost);
                    newComment.setParentId(mComment.getObjectId());
                    newComment.setContent(content);
                    newComment.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                LogUtils.i(TAG,"reply comment succeed!");
                                if (isShowAllReply){
                                    fetchAllReply();
                                }else {
                                    fetchTwoReply();
                                }

                                if(mComment.getUser().getObjectId().equals(mPost.getUser().getObjectId())) {
                                    if (!BmobUser.getCurrentUser().getObjectId().equals(mComment.getUser().getObjectId())){
                                        MessageUtils.sendMessage(itemView.getContext(),
                                                mComment.getUser(), newComment,Constants.sMessageType[1]);
                                    }
                                }else {
                                    if (!BmobUser.getCurrentUser().getObjectId().equals(mPost.getUser().getObjectId())){
                                        MessageUtils.sendMessage(itemView.getContext(),
                                                mPost.getUser(), newComment,Constants.sMessageType[0]);
                                    }
                                    if (!BmobUser.getCurrentUser().getObjectId().equals(mComment.getUser().getObjectId())){
                                        MessageUtils.sendMessage(itemView.getContext(),
                                                mComment.getUser(), newComment,Constants.sMessageType[1]);
                                    }
                                }
                            }else {
                                LogUtils.i(TAG,"reply comment failed" + e.getMessage());
                            }
                        }
                    });
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

        private void gotoLogin(){
            ToastFactory.show(itemView.getContext(),"登陆之后更精彩");
            Intent intent = new Intent(itemView.getContext(), RegisterAndLoginActivity.class);
            itemView.getContext().startActivity(intent);
        }
    }

    static class PostDetailViewHolder extends RecyclerView.ViewHolder{

        private Post mPost;

        private TextView mUserNameTextView;
        private TextView mPostTimeTextView;
        private TextView mSubjectTextView;
        private TextView mContentTextView;
        private TextView mSelfIntroductionTextView;
        private TextView mSelfIntroductionContentTextView;
        private TextView mSectionTextView;
        private CircleImageView mAvatarImageView;
        private LinearLayout mPostImageLinearLayout;
        private LinearLayout mLinearLayout;
        private ImageView mPostPicImageView0;
        private ImageView mPostPicImageView1;
        private ImageView mPostPicImageView2;
        private TextView mTeammateRequireEditTex0;
        private TextView mTeammateRequireEditTex1;
        private TextView mTeammateRequireEditTex2;
        private TextView mTeammateRequireEditTex3;
        private LinearLayout mTeammateLinearLayout;
        private LinearLayout mTeammateLinearLayout1;
        private LinearLayout mTeammateLinearLayout2;
        private LinearLayout mTeammateLinearLayout3;

        public PostDetailViewHolder(View v){
            super(v);

            mUserNameTextView = (TextView) v.findViewById(R.id.text_view_post_detail_user_name);
            mPostTimeTextView = (TextView) v.findViewById(R.id.text_view_post_detail_time);
            mSubjectTextView = (TextView) v.findViewById(R.id.text_view_post_detail_subject);
            mContentTextView = (TextView) v.findViewById(R.id.text_view_post_detail_content);
            mSelfIntroductionTextView = (TextView) v.findViewById(R.id.text_view_self_introduction);
            mSelfIntroductionContentTextView = (TextView) v.findViewById(R.id.text_view_self_introduction_content);
            mSectionTextView = (TextView) v.findViewById(R.id.text_view_section);
            mAvatarImageView = (CircleImageView) v.findViewById(R.id.image_view_post_detail_user_head_picture);
            mPostImageLinearLayout = (LinearLayout) v.findViewById(R.id.linear_layout_post_detail_image);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.relative_layout_self_introduction);
            mPostPicImageView0 = (ImageView) v.findViewById(R.id.image_view_post_detail_image0);
            mPostPicImageView1 = (ImageView) v.findViewById(R.id.image_view_post_detail_image1);
            mPostPicImageView2 = (ImageView) v.findViewById(R.id.image_view_post_detail_image2);
            mTeammateRequireEditTex0 = (TextView) v.findViewById(R.id.text_view_teammate0_require);
            mTeammateRequireEditTex1 = (TextView) v.findViewById(R.id.text_view_teammate1_require);
            mTeammateRequireEditTex2 = (TextView) v.findViewById(R.id.text_view_teammate2_require);
            mTeammateRequireEditTex3 = (TextView) v.findViewById(R.id.text_view_teammate3_require);
            mTeammateLinearLayout = (LinearLayout) v.findViewById(R.id.linear_layout_teammate_main);
            mTeammateLinearLayout1 = (LinearLayout) v.findViewById(R.id.linear_layout_teammate1);
            mTeammateLinearLayout2 = (LinearLayout) v.findViewById(R.id.linear_layout_teammate2);
            mTeammateLinearLayout3 = (LinearLayout) v.findViewById(R.id.linear_layout_teammate3);

            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPost.getUser() != null) {
                        Intent intent = PersonalInfoActivity.newIntent(v.getContext(), mPost.getUser());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        /**
         * 加载帖子内容
         * @param post
         */
        public void bindPostDetailView(Post post){
            mPost = post;
            String postType = post.getPostType();

            PictureUtils.loadAuthorAvatar(itemView.getContext(),mAvatarImageView,post.getUser());
            mUserNameTextView.setText(post.getUser().getNickname());
            mPostTimeTextView.setText(StringUtils.timeDiff(StringUtils.string2date(post.getCreatedAt())));
            mSubjectTextView.setText(post.getPostSubject());
            mContentTextView.setText(post.getPostContent());
            mSectionTextView.setText(post.getSection());

            if (postType.equals(Constants.FIND_TEAM)) {
                mTeammateLinearLayout.setVisibility(View.GONE);
                if (!post.getIntroduction().equals("")) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    mSelfIntroductionTextView.setText(itemView.getContext().getString(R.string.self_introduction_center));
                    mSelfIntroductionContentTextView.setText(post.getIntroduction());
                } else {
                    mLinearLayout.setVisibility(View.GONE);
                }
            }else if (postType.equals(Constants.FIND_TEAMMATE)){
                mTeammateLinearLayout.setVisibility(View.VISIBLE);
                if (!post.getIntroduction().equals("")) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    mSelfIntroductionTextView.setText(itemView.getContext().getString(R.string.team_introduction_center));
                    mSelfIntroductionContentTextView.setText(post.getIntroduction());
                } else {
                    mLinearLayout.setVisibility(View.GONE);
                }
                mTeammateRequireEditTex0.setText(post.getTeammate0());
                if (!post.getTeammate1().equals("")){
                    mTeammateLinearLayout1.setVisibility(View.VISIBLE);
                    mTeammateRequireEditTex1.setText(post.getTeammate1());
                    if (!post.getTeammate2().equals("")){
                        mTeammateLinearLayout2.setVisibility(View.VISIBLE);
                        mTeammateRequireEditTex2.setText(post.getTeammate1());
                        if (!post.getTeammate3().equals("")){
                            mTeammateLinearLayout3.setVisibility(View.VISIBLE);
                            mTeammateRequireEditTex3.setText(post.getTeammate1());
                        }else {
                            mTeammateLinearLayout3.setVisibility(View.GONE);
                        }
                    }else {
                        mTeammateLinearLayout2.setVisibility(View.GONE);
                    }
                }else {
                    mTeammateLinearLayout1.setVisibility(View.GONE);
                }
            }

            //若帖子没有图片，则不显示图片区域
            if (post.getImage0() != null){
                mPostImageLinearLayout.setVisibility(View.VISIBLE);
                PictureUtils.loadPostDetailPic(itemView.getContext(),mPostPicImageView0,post.getImage0());
                if (post.getImage1() != null){
                    PictureUtils.loadPostDetailPic(itemView.getContext(),mPostPicImageView1,post.getImage1());
                    if (post.getImage1() != null){
                        PictureUtils.loadPostDetailPic(itemView.getContext(),mPostPicImageView2,post.getImage2());
                    }else {
                        mPostPicImageView2.setVisibility(View.GONE);
                    }
                }else {
                    mPostPicImageView1.setVisibility(View.GONE);
                    mPostPicImageView2.setVisibility(View.GONE);
                }
            }else {
                mPostImageLinearLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE.POST_DETAIL_TYPE.ordinal()) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_post_detail, parent, false);
            return new PostDetailViewHolder(view);
        }else{
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_comment, parent, false);
            return new CommentViewHolder(view);
        }
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostDetailViewHolder){
            ((PostDetailViewHolder) holder).bindPostDetailView(mPost);
        }else if(holder instanceof CommentViewHolder){
            ((CommentViewHolder) holder).bindCommentView(mPost,mComments.get(position - 1),position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return ITEM_TYPE.POST_DETAIL_TYPE.ordinal();
        }else {
            return ITEM_TYPE.COMMENT_TYPE.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size() + 1;
    }
}
