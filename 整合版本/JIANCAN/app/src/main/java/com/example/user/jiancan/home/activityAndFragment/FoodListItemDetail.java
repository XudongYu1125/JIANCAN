package com.example.user.jiancan.home.activityAndFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.LoginByNickNameActivity;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.Comment;
import com.example.user.jiancan.home.entity.CommentDetailBean;
import com.example.user.jiancan.home.entity.HomeListItemBean;
import com.example.user.jiancan.home.entity.ReplyDetailBean;
import com.example.user.jiancan.home.entity.SharedItem;
import com.example.user.jiancan.home.util.CommentExpandAdapter;
import com.example.user.jiancan.personal.entity.TUser;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @author june
 * 食物列表详情页
 */
public class FoodListItemDetail extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FoodListItemDetail";
    private boolean userStatus = true;
    private boolean likeStatus = false;
    private boolean followStatus = false;
    private AlertDialog.Builder builder;
    private ImageView mBackImgView;
    private ImageView mUserPhoto;
    private ImageView mFollowStatus;
    private TextView mTextUser;
    private Banner mBanner;
    private HomeListItemBean bean;
    private BottomSheetDialog dialog;
    private String userImg = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1141259048,554497535&fm=26&gp=0.jpg";
    private ImageView mShare;
    private TextView mDetailPageDoComment;
    private TextView mFoodDetailText;
    private TextView mFoodDetailTitle;
    private ImageView mMoreImg;
    private CommentExpandableListView mCommentExpandableListView;
    private CommentExpandAdapter adapter;
    private List<CommentDetailBean> commentDetailBeanList = new ArrayList<>();
    private String videoUrl = "http://vfx.mtime.cn/Video/2019/06/27/mp4/190627231412433967.mp4";
    private String coverImgUrl = "http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190220/495bdabb092a4e608a705c82139269bb.jpeg";
    private String videoTitle = "videoTitle";
    private JCVideoPlayerStandard mJCVideo;
    private ImageView mLike;
    private TUser user;
    private User loginUser;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private List<Comment> comments = new ArrayList<>();
    private static RelativeLayout mHint;
    private SharedPreferences sharedPreferences = null;
    private Gson gson;
    private final static int FOOD_TAG = 1;
    private final static int COMMENT_TAG = 2;
    private TextView mLikeNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list_item_detail);
//        sharedPreferences = getSharedPreferences ("loginInfo",MODE_PRIVATE);
//        loginUser = gson.fromJson (sharedPreferences.getString ("user",null),User.class);
//        initLoginUserStatus ();
        bean = getData();
        //commentDetailBeanList = getContent ( 9 );

        videoTitle = bean.getName();
//        initModuleData ();
        initView();
        setUserImg(userImg);
        getCommentByFoodId(9);
    }

    /**
     * 初始化用户的状态
     */
    private void initLoginUserStatus() {
        followStatus = checkFollowStatus(loginUser.getId(), bean.getUserId());
        videoUrl = bean.getVideoUrl();
        coverImgUrl = bean.getShowImg();
        videoTitle = bean.getName();
    }

    /**
     * 注册组件
     */
    private void initView() {
        //后退按钮
        mBackImgView = (ImageView) findViewById(R.id.back_imgView);
        mBackImgView.setOnClickListener(this);
        //发布者头像
        mUserPhoto = (ImageView) findViewById(R.id.user_photo);
        mUserPhoto.setOnClickListener(this);
        //header中用户名
        mTextUser = (TextView) findViewById(R.id.food_detail_user);
        mTextUser.setText(bean.getUser());
        //关注状态
        mFollowStatus = (ImageView) findViewById(R.id.follow_status);
        mFollowStatus.setOnClickListener(this);
        //更多按钮
        mMoreImg = findViewById(R.id.more_menu);
        mMoreImg.setOnClickListener(this);
        //视频播放器
        mJCVideo = (JCVideoPlayerStandard) findViewById(R.id.media_video);
        //文字教程(标题)
        mFoodDetailTitle = (TextView) findViewById(R.id.food_detail_title);
        mFoodDetailTitle.setText(bean.getTitle());
        //文字教程(内容)
        mFoodDetailText = (TextView) findViewById(R.id.food_detail_text);
        mFoodDetailText.setText(bean.getContent());
        //评论区
        mDetailPageDoComment = (TextView) findViewById(R.id.detail_page_do_comment);
        mDetailPageDoComment.setOnClickListener(this);
        mCommentExpandableListView = findViewById(R.id.detail_page_lv_comment);
        //点赞键
        mLike = (ImageView) findViewById(R.id.like_button);
        mLike.setOnClickListener(this);
        //点赞数
        mLikeNum = (TextView) findViewById(R.id.like_num);
        //无评论时显示内容
        mHint = findViewById(R.id.no_comment_hint);
        //展示图片还是视频
        mBanner = findViewById(R.id.img_banner);
        switch (bean.getType()){
            case 0:
                mJCVideo.setVisibility(View.GONE);
                initBanner();
                break;
            case 1:
                mBanner.setVisibility (View.GONE);
                initJCVideo();
                break;
        }
    }

    /**
     * 初始化各组件数据
     */
    private void initModuleData() {
        userImg = getUserById(bean.getUserId()).getImageUrl();
        coverImgUrl = bean.getShowImg();
        videoUrl = bean.getVideoUrl();
        mFoodDetailTitle.setText(bean.getTitle());
        mFoodDetailText.setText(bean.getContent());
    }

    /**
     * 初始化banner
     */
    private void initBanner() {
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new MyLoader())
                //从Presenter中取出图片资源
                .setImages(getBannerImages())
                .setBannerAnimation(Transformer.Default)
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }

    /**
     * 获取Banner的图片资源
     */
    public ArrayList<String> getBannerImages() {
        Set<com.example.user.jiancan.home.entity.Image> images1 = bean.getImages();
        ArrayList<String> listPath = new ArrayList<>();
        List<com.example.user.jiancan.home.entity.Image> mBannerImages = new ArrayList<com.example.user.jiancan.home.entity.Image>(images1);//B是set型的
        for (com.example.user.jiancan.home.entity.Image image : images1){
            listPath.add("http://39.97.3.111:8080/JianCanServerSystem/upload/images/" + image.getImageName() );
        }
        return listPath;
    }

    /**
     *
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load((String) path)
                    .into(imageView);
        }
    }

    /**
     * 获取Intent中的数据
     */
    private HomeListItemBean getData() {
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("食物数据");
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, HomeListItemBean.class);
    }

    /**
     * @param url 发布者头像地址
     *            发布者头像展示功能
     */
    @SuppressLint("CheckResult")
    private void setUserImg(String url) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions
                .circleCrop()
                .error(R.mipmap.ic_launcher_round);
        Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(mUserPhoto);

    }

    /**
     * 初始化popupMenu
     */
    private void initPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(FoodListItemDetail.this, mMoreImg);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download_video:
                        Toast.makeText(FoodListItemDetail.this, "下载", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.share:
                        Toast.makeText(FoodListItemDetail.this, "分享", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FoodListItemDetail.this, ShareFriends.class);
                        SharedItem sharedItem = new SharedItem();
                        sharedItem.setId(bean.getFoodId());
                        sharedItem.setCoverImg(bean.getShowImg());
                        sharedItem.setTitle(bean.getName());
                        Log.e("sharedItem", sharedItem.toString());
                        String jsonStr = new Gson().toJson(sharedItem);
                        intent.putExtra("分享内容", jsonStr);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        setIconEnable(popupMenu.getMenu(), true);
        popupMenu.show();
    }

    /**
     * 强制显示PopupMenu中item的icon
     */
    private void setIconEnable(Menu menu, boolean enable) {
        try {
            @SuppressLint("PrivateApi") Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            @SuppressLint("DiscouragedPrivateApi") Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //传入参数
            m.invoke(menu, enable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //后退点击
            case R.id.back_imgView:
                finish();
                break;
            //关注状态
            case R.id.follow_status:
                if (userStatus) {
                    if (followStatus) {
                        alertLog(1);
                    } else {
                        Toast.makeText(this, "您已关注" + bean.getUser(), Toast.LENGTH_SHORT).show();
                        followStatus = true;
                        mFollowStatus.setImageResource(R.drawable.havefollowed);
                    }
                } else {
                    alertLog(2);
                }

                break;
            //发布者头像点击
            case R.id.user_photo:
                TUser user = getUserById(bean.getUserId());

//                TUser user = new TUser ( );
                user.setId(bean.getUserId());
                user.setNickname(bean.getUser());
                user.setImageUrl(userImg);

                Intent intent = new Intent(FoodListItemDetail.this, UserInfo.class);
                String jsonStr = new Gson().toJson(user);
                intent.putExtra("用户信息", jsonStr);
                startActivity(intent);
                break;
            //更多点击
            case R.id.more_menu:
                initPopupMenu();
                break;
            //弹出评论框
            case R.id.detail_page_do_comment:
                if (userStatus) {
                    showCommentDialog();
                } else {
                    alertLog(2);
                }
                break;
            //点赞
            case R.id.like_button:
                Log.e("点击了点赞", "执行了onClick");
                if (userStatus) {
                    if (likeStatus) {
                        likeStatus = false;
                        mLike.setImageResource(R.drawable.unlike);
//                        unLikeItem (FOOD_TAG,bean.getUserId ());

                        Log.e("取消赞", String.valueOf(likeStatus));
                    } else {
                        likeStatus = true;
//                        likeItem (FOOD_TAG,bean.getUserId ());
                        mLike.setImageResource(R.drawable.like);
                        Log.e("赞", String.valueOf(likeStatus));
                    }
                } else {
                    alertLog(2);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 初始化评论回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList) {
        //将默认的展开图片关闭
        mCommentExpandableListView.setGroupIndicator(null);

        View.OnClickListener iv_expandClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = (Map<String, Object>) v.getTag();
                int groupPosition = (int) map.get("groupPosition");
                boolean isExpand = mCommentExpandableListView.isGroupExpanded(groupPosition);
                if (isExpand) {
                    mCommentExpandableListView.collapseGroup(groupPosition);
                } else {
                    mCommentExpandableListView.expandGroup(groupPosition);
                }
            }
        };


        adapter = new CommentExpandAdapter(this, commentList, iv_expandClickListener);
        mCommentExpandableListView.setAdapter (adapter);
        //默认全部展开
        for (int i = 0; i < commentList.size(); i++) {
            mCommentExpandableListView.expandGroup(i);
        }
        mCommentExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                Log.e(TAG, "onGroupClick: 当前的评论id>>>" + commentList.get(groupPosition).getId());
                if (userStatus) {
                    showReplyDialog(groupPosition);
                } else {
                    alertLog(2);
                }
                return true;
            }
        });

        mCommentExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                if (userStatus) {
                    replyReply(groupPosition, childPosition);
                } else {
                    alertLog(2);
                }
                return false;
            }
        });


    }

    /**
     * by june on 2020/5/1
     * func:弹出评论框
     */
    private void showCommentDialog() {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        //解决bsd显示不全的情况
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0, 0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {

                    dialog.dismiss();

                    CommentDetailBean detailBean = new CommentDetailBean("测试用户", commentContent, getTime());
                    adapter.addTheCommentData(detailBean);
                    Toast.makeText(FoodListItemDetail.this, "评论成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(FoodListItemDetail.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 0) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by june on 2020/5/1
     * func:弹出回复框
     */
    private void showReplyDialog(final int position) {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentDetailBeanList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(replyContent)) {

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean("测试用户", null, replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    mCommentExpandableListView.expandGroup(position);
                    Toast.makeText(FoodListItemDetail.this, "回复成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FoodListItemDetail.this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 0) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * 对于评论回复的回复
     *
     * @param groupPosition 点击位置
     */
    private void replyReply(final int groupPosition, final int childPosition) {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentDetailBeanList.get(groupPosition).getReplyList().get(childPosition).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(replyContent)) {

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean("测试用户", commentDetailBeanList.get(groupPosition).getReplyList().get(childPosition).getNickName(), replyContent);
                    adapter.addTheReplyData(detailBean, groupPosition);
                    mCommentExpandableListView.expandGroup(groupPosition);
                    Toast.makeText(FoodListItemDetail.this, "回复成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FoodListItemDetail.this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by june on 2020/5/1
     * 获取时间方法
     */
    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH时mm分", Locale.getDefault());
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 时间转化方法
     */
    private String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH时mm分", Locale.getDefault());
        return simpleDateFormat.format(date);
    }


    /**
     * 菜单点击响应事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 跳转至登陆界面
     */
    private void sendToLogin() {
        Intent intent = new Intent(FoodListItemDetail.this, LoginByNickNameActivity.class);
        intent.putExtra("activityFrom", TAG);
        startActivity(intent);
    }

    /**
     * 弹窗提醒
     *
     * @param i i=1:取消关注弹窗确认
     *          i=2:跳转登陆界面确认
     */
    private void alertLog(int i) {
        switch (i) {
            case 1:
                builder = new AlertDialog.Builder(this).setIcon(R.drawable.cry).setTitle("取消关注确认")
                        .setMessage("您确定取消关注" + bean.getUser() + "吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                followStatus = false;
                                mFollowStatus.setImageResource(R.drawable.unfollow);
                                Toast.makeText(FoodListItemDetail.this, "您已成功取消对" + bean.getUser() + "的关注！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case 2:
                builder = new AlertDialog.Builder(this).setIcon(R.drawable.smile).setTitle("前往登陆确认")
                        .setMessage("您即将前往登陆界面").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendToLogin();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            default:
                break;
        }

    }


    /**
     * 初始化视频播放器
     */
    private void initJCVideo() {

        mJCVideo.setUp(bean.getVideoUrl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, videoTitle);
        Glide.with(this)
                .load(bean.getShowImg())
                .into(mJCVideo.thumbImageView);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 通过userId查询user信息
     */
    private TUser getUserById(int id) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
        final Request request = new Request.Builder().url(Constant.URL_USER_INFO + id).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userJsonStr = response.body().string();
                user = new Gson().fromJson(userJsonStr, TUser.class);
            }
        });
        return user;
    }

    ;

    /**
     * 查询user信息,评论列表使用
     */
    private void getUser(Comment comment, final CommentDetailBean commentDetailBean,
                                            final List<CommentDetailBean> commentDetailBeans,
                                            final ReplyDetailBean replyDetailBean,
                                            final List<ReplyDetailBean> replyDetailBeans) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
        final Request request = new Request.Builder().url(Constant.URL_USER_INFO + comment.getUserId()).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userJsonStr = response.body().string();
                user = new Gson().fromJson(userJsonStr, TUser.class);
                if (commentDetailBean != null){
                    commentDetailBean.setNickName(user.getNickname());
                    commentDetailBean.setUserLogo(user.getImageUrl());
                    commentDetailBeans.add(commentDetailBean);
                }
                if (replyDetailBean != null){
                    replyDetailBean.setNickName(user.getNickname());
                    replyDetailBean.setImageUrl(user.getImageUrl());
                    //一级回复
                    if (replyDetailBean.getReplyId() != replyDetailBean.getParentId()){
                        String name = getUserById(replyDetailBean.getReplyId()).getNickname();
                        replyDetailBean.setReplyToOtherUser(name);
                    }
                    replyDetailBeans.add(replyDetailBean);
                }
            }
        });
        return ;
    }

    /**
     * 检查用户与发布视频者的关注关系
     *
     * @param userId      用户id
     * @param publisherId 发布视频者id
     */
    private boolean checkFollowStatus(int userId, final int publisherId) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
        final Request request = new Request.Builder().url(Constant.URL_FOLLOW_LIST + userId).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Type type = new TypeToken<List<User>>() {
                }.getType();
                List<User> userList = new Gson().fromJson(jsonStr, type);
                for (User u : userList) {
                    if (u.getId() == publisherId) {
                        followStatus = true;
                    } else {
                        followStatus = false;
                    }
                }
            }
        });
        return followStatus;
    }


    /**
     * 在onStart生命周期中将状态的图片进行初始化
     */
    @Override
    protected void onStart() {
        if (followStatus) {
            mFollowStatus.setImageResource(R.drawable.havefollowed);
        } else {
            mFollowStatus.setImageResource(R.drawable.unfollow);
        }
        if (likeStatus) {
            mLike.setImageResource(R.drawable.like);
        } else {
            mLike.setImageResource(R.drawable.unlike);
        }
        super.onStart();
    }

    /**
     * 通过菜的ID得到所有有关的评论
     */
    private void getCommentByFoodId(int id) {

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
        final Request request = new Request.Builder().url(Constant.URL_COMMENT_BY_FOOD_ID + id).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1", e.getMessage());
            }

            @SneakyThrows
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String objectStr = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(objectStr);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<Comment> comments1 = JSONArray.parseArray(jsonArray.toJSONString(), Comment.class);
                Log.e("评论+回复列表", comments1.toString());

                List<CommentDetailBean> cdBeans = new ArrayList<>();
                List<ReplyDetailBean> rdBeans = new ArrayList<>();


                if (comments1.size() != 0 || null == comments1) {
                    //得到父评论列表
                    for (Comment c : comments1) {
                        //判断是否为回复
                        //若parentId为-1，则该条数据为评论
                        //否则，这条数据是对某个评论的回复
                        if (c.getParentId() == -1) {
                            //CommentDetailBean需要id,userName,userImg,content,date,likeNum,replyList
                            CommentDetailBean cdb = new CommentDetailBean();
                            cdb.setId(c.getId());
                            cdb.setContent(c.getContent());
                            cdb.setDate(formatDate(c.getDate()));
                            cdb.setLikeNum(c.getFabulous());
                            getUser(c, cdb, cdBeans,null,null);
                        }
                    }
                    for (CommentDetailBean cdb : cdBeans){
                        for (Comment c:comments1){
                            if (c.getParentId() == cdb.getId()){
                                ReplyDetailBean replyDetailBean = new ReplyDetailBean();
                                replyDetailBean.setContent(c.getContent());
                                replyDetailBean.setId(c.getId());
                                replyDetailBean.setParentId(c.getParentId());
                                replyDetailBean.setReplyId(c.getReplyId());
                                getUser(c, null,null,replyDetailBean, rdBeans);
                            }
                        }
                    }

                }
                final List<CommentDetailBean> commentDetailBeanArrayList = new ArrayList<>(assembleTestCDB(cdBeans, rdBeans));
                if (commentDetailBeanArrayList.size() != 0){
                    FoodListItemDetail.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initExpandableListView(commentDetailBeanArrayList);
                        }
                    });
                }

            }
        });
        return;
    }

    /**
     * 测试与实际通用
     * 组装CommentDetailBean
     *
     * @param commentDetailBeans 评论的list列表
     * @param replyDetailBeans   回复的list列表
     * @return 返回一个组装完成的CommentDetailBean类型的列表
     */
    private List<CommentDetailBean> assembleTestCDB(List<CommentDetailBean> commentDetailBeans, List<ReplyDetailBean> replyDetailBeans) {
        if (commentDetailBeans.size() != 0) {
            for (CommentDetailBean commentDetailBean : commentDetailBeans) {
                List<ReplyDetailBean> rdBeans = new ArrayList<>();
                for (ReplyDetailBean rdb : replyDetailBeans) {
                    if (rdb.getParentId() == commentDetailBean.getId()) {
                        rdBeans.add(rdb);
                    }
                    commentDetailBean.setReplyList(rdBeans);

                }


            }
        }
        return commentDetailBeans;
    }


    /**
     * 设置hint显示
     */
    public static void setHintVisible() {
        Log.e("评论区提示信息", "提示信息显示");
        mHint.setVisibility(View.VISIBLE);
    }

    /**
     * 设置hint不显示
     */
    public static void setHintInVisible() {
        Log.e("评论区提示信息", "提示信息隐藏");
        mHint.setVisibility(View.GONE);
    }

    /**
     * 点赞的功能
     */
    public static void likeItem(int tag, int id) {
        String url = "";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
        if (tag == FOOD_TAG) {
            url = Constant.URL_FOOD_LIKE;
        } else {
            url = Constant.URL_COMMENT_LIKE;
        }
        final Request request = new Request.Builder().url(url + id).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Boolean mResponse = new Gson().fromJson(jsonStr, Boolean.class);
            }
        });
    }

    /**
     * 取消赞功能
     */
    public static void unLikeItem(int tag, int id) {
        String url = "";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
        if (tag == FOOD_TAG) {
            url = Constant.URL_FOOD_UNLIKE;
        } else {
            url = Constant.URL_COMMENT_UNLIKE;
        }
        final Request request = new Request.Builder().url(url + id).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Boolean mResponse = new Gson().fromJson(jsonStr, Boolean.class);
            }
        });
    }

    private void downloadFood() {

    }

}
