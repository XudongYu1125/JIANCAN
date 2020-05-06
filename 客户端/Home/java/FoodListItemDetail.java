package com.example.user.jiancan.home.activityAndFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.jiancan.LoginByNickNameActivity;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.CommentBean;
import com.example.user.jiancan.home.entity.CommentDetailBean;
import com.example.user.jiancan.home.entity.HomeListItemBean;
import com.example.user.jiancan.home.entity.ReplyDetailBean;
import com.example.user.jiancan.home.util.CommentExpandAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * @author june
 * 食物列表详情页
 */
public class FoodListItemDetail extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FoodListItemDetail";
    private boolean userStatus = true;
    private boolean likeStatus = true;
    private boolean followStatus = true;
    private AlertDialog.Builder builder;
    private ImageView mBackImgView;
    private ImageView mUserPhoto;
    private ImageView mFollowStatus;
    private TextView mTextUser;
    private HomeListItemBean bean;
    private BottomSheetDialog dialog;
    private int publisherId;
    private String userImg = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1141259048,554497535&fm=26&gp=0.jpg";
    private ImageView mShare;
    private TextView mDetailPageDoComment;
    private TextView mFoodDetailText;
    private TextView mFoodDetailTitle;
    private CommentExpandableListView mCommentExpandableListView;
    private CommentExpandAdapter adapter;
    private List <CommentDetailBean> commentsList;
    private String videoUrl = "http://www.jmzsjy.com/UploadFile/%E5%BE%AE%E8%AF%BE/%E5%9C%B0%E6%96%B9%E9%A3%8E%E5%91%B3%E5%B0%8F%E5%90%83%E2%80%94%E2%80%94%E5%AE%AB%E5%BB%B7%E9%A6%99%E9%85%A5%E7%89%9B%E8%82%89%E9%A5%BC.mp4";
    private String previewImgUrl = "http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190220/495bdabb092a4e608a705c82139269bb.jpeg";
    private JCVideoPlayerStandard mJCVideo;
    private ImageView mLike;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.food_list_item_detail);
        bean = getData ( );
        initView ( );
        setUserImg (userImg);
        commentsList = generateTestData ( );
        initExpandableListView (commentsList);
        initJCVideo ( );
    }

    /**
     * 注册组件
     */
    private void initView() {
        //后退按钮
        mBackImgView = (ImageView) findViewById (R.id.back_imgView);
        mBackImgView.setOnClickListener (this);
        //发布者头像
        mUserPhoto = (ImageView) findViewById (R.id.user_photo);
        mUserPhoto.setOnClickListener (this);
        //header中用户名
        mTextUser = (TextView) findViewById (R.id.food_detail_user);
        mTextUser.setText (bean.getUser ( ));
        //关注状态
        mFollowStatus = (ImageView) findViewById (R.id.follow_status);
        mFollowStatus.setOnClickListener (this);
        //分享按钮
        mShare = (ImageView) findViewById (R.id.share);
        mShare.setOnClickListener (this);
        //视频播放器
        mJCVideo = (JCVideoPlayerStandard) findViewById (R.id.media_video);
        //文字教程(标题)
        mFoodDetailTitle = (TextView) findViewById (R.id.food_detail_title);
        //文字教程(内容)
        mFoodDetailText = (TextView) findViewById (R.id.food_detail_text);

        //评论区
        mDetailPageDoComment = (TextView) findViewById (R.id.detail_page_do_comment);
        mDetailPageDoComment.setOnClickListener (this);
        mCommentExpandableListView = findViewById (R.id.detail_page_lv_comment);
        //点赞键
        mLike = (ImageView) findViewById (R.id.like_button);
        mLike.setOnClickListener (this);
    }


    /**
     * 获取Intent中的数据
     */
    private HomeListItemBean getData() {
        Intent intent = getIntent ( );
        String jsonString = intent.getStringExtra ("食物数据");
        Gson gson = new GsonBuilder ( ).create ( );
        return gson.fromJson (jsonString , HomeListItemBean.class);
    }

    /**
     * @param url 发布者头像地址
     *            发布者头像展示功能
     */
    private void setUserImg( String url ) {
        RequestOptions requestOptions = new RequestOptions ( );
        requestOptions
                .circleCrop ( )
                .error (R.mipmap.ic_launcher_round);
        Glide.with (this)
                .load (url)
                .apply (requestOptions)
                .into (mUserPhoto);

    }


    /**
     * 点击事件
     */
    @Override
    public void onClick( View v ) {
        switch (v.getId ( )) {
            //后退点击
            case R.id.back_imgView:
                finish ( );
                break;
            //关注状态
            case R.id.follow_status:
                if (userStatus) {
                    if (followStatus) {
                        alertLog (1);
                    } else {
                        Toast.makeText (this , "您已关注" + bean.getUser ( ) , Toast.LENGTH_SHORT).show ( );
                        followStatus = true;
                        mFollowStatus.setImageResource (R.drawable.havefollowed);
                    }
                } else {
                    alertLog (2);
                }

                break;
            //发布者头像点击
            case R.id.user_photo:
                publisherId = 1;
                Intent intent = new Intent (FoodListItemDetail.this , PublisherProduction.class);
                intent.putExtra ("发布者ID" , publisherId);
                startActivity (intent);
                break;
            //分享点击
            case R.id.share:

                break;
            //弹出评论框
            case R.id.detail_page_do_comment:
                if (userStatus) {
                    showCommentDialog ( );
                } else {
                    alertLog (2);
                }
                break;
            //点赞
            case R.id.like_button:
                if (likeStatus){
                    likeStatus = false;
                    mLike.setImageResource (R.drawable.like);
                }else {
                    likeStatus = true;
                    mLike.setImageResource (R.drawable.unlike);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 初始化评论回复列表
     */
    private void initExpandableListView( final List <CommentDetailBean> commentList ) {
        //将默认的展开图片关闭
        mCommentExpandableListView.setGroupIndicator (null);

        View.OnClickListener iv_expandClickListener = new View.OnClickListener ( ) {
            @Override
            public void onClick( View v ) {
                Map<String,Object> map = (Map <String, Object>) v.getTag ();
                int groupPosition = (int) map.get ("groupPosition");
                boolean isExpand = mCommentExpandableListView.isGroupExpanded (groupPosition);
                if (isExpand){
                    mCommentExpandableListView.collapseGroup (groupPosition);
                }else {
                    mCommentExpandableListView.expandGroup (groupPosition);
                }
            }
        };


        adapter = new CommentExpandAdapter (this , commentList,iv_expandClickListener);
        mCommentExpandableListView.setAdapter (adapter);
        //默认全部展开
        for (int i = 0; i < commentList.size ( ); i++) {
            mCommentExpandableListView.expandGroup (i);
        }
        mCommentExpandableListView.setOnGroupClickListener (new ExpandableListView.OnGroupClickListener ( ) {
            @Override
            public boolean onGroupClick( ExpandableListView expandableListView , View view , int groupPosition , long l ) {
                Log.e (TAG , "onGroupClick: 当前的评论id>>>" + commentList.get (groupPosition).getId ( ));
                if (userStatus) {
                    showReplyDialog (groupPosition);
                } else {
                    alertLog (2);
                }
                return true;
            }
        });

        mCommentExpandableListView.setOnChildClickListener (new ExpandableListView.OnChildClickListener ( ) {
            @Override
            public boolean onChildClick( ExpandableListView expandableListView , View view , int groupPosition , int childPosition , long l ) {
                if (userStatus) {
                    replyReply (groupPosition , childPosition);
                } else {
                    alertLog (2);
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
        dialog = new BottomSheetDialog (this);
        View commentView = LayoutInflater.from (this).inflate (R.layout.comment_dialog_layout , null);
        final EditText commentText = (EditText) commentView.findViewById (R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById (R.id.dialog_comment_bt);
        dialog.setContentView (commentView);
        //解决bsd显示不全的情况
        View parent = (View) commentView.getParent ( );
        BottomSheetBehavior behavior = BottomSheetBehavior.from (parent);
        commentView.measure (0 , 0);
        behavior.setPeekHeight (commentView.getMeasuredHeight ( ));

        bt_comment.setOnClickListener (new View.OnClickListener ( ) {

            @Override
            public void onClick( View view ) {
                String commentContent = commentText.getText ( ).toString ( ).trim ( );
                if (!TextUtils.isEmpty (commentContent)) {

                    dialog.dismiss ( );

                    CommentDetailBean detailBean = new CommentDetailBean ("小明" , commentContent , getTime ( ));
                    adapter.addTheCommentData (detailBean);
                    Toast.makeText (FoodListItemDetail.this , "评论成功" , Toast.LENGTH_SHORT).show ( );

                } else {
                    Toast.makeText (FoodListItemDetail.this , "评论内容不能为空" , Toast.LENGTH_SHORT).show ( );
                }
            }
        });
        commentText.addTextChangedListener (new TextWatcher ( ) {
            @Override
            public void beforeTextChanged( CharSequence charSequence , int i , int i1 , int i2 ) {

            }

            @Override
            public void onTextChanged( CharSequence charSequence , int i , int i1 , int i2 ) {
                if (!TextUtils.isEmpty (charSequence) && charSequence.length ( ) > 0) {
                    bt_comment.setBackgroundColor (Color.parseColor ("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor (Color.parseColor ("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged( Editable editable ) {

            }
        });
        dialog.show ( );
    }

    /**
     * by june on 2020/5/1
     * func:弹出回复框
     */
    private void showReplyDialog( final int position ) {
        dialog = new BottomSheetDialog (this);
        View commentView = LayoutInflater.from (this).inflate (R.layout.comment_dialog_layout , null);
        final EditText commentText = (EditText) commentView.findViewById (R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById (R.id.dialog_comment_bt);
        commentText.setHint ("回复 " + commentsList.get (position).getNickName ( ) + " 的评论:");
        dialog.setContentView (commentView);
        bt_comment.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick( View view ) {
                String replyContent = commentText.getText ( ).toString ( ).trim ( );
                if (!TextUtils.isEmpty (replyContent)) {

                    dialog.dismiss ( );
                    ReplyDetailBean detailBean = new ReplyDetailBean ("小红" , null , replyContent);
                    adapter.addTheReplyData (detailBean , position);
                    mCommentExpandableListView.expandGroup (position);
                    Toast.makeText (FoodListItemDetail.this , "回复成功" , Toast.LENGTH_SHORT).show ( );
                } else {
                    Toast.makeText (FoodListItemDetail.this , "回复内容不能为空" , Toast.LENGTH_SHORT).show ( );
                }
            }
        });
        commentText.addTextChangedListener (new TextWatcher ( ) {
            @Override
            public void beforeTextChanged( CharSequence charSequence , int i , int i1 , int i2 ) {

            }

            @Override
            public void onTextChanged( CharSequence charSequence , int i , int i1 , int i2 ) {
                if (!TextUtils.isEmpty (charSequence) && charSequence.length ( ) > 0) {
                    bt_comment.setBackgroundColor (Color.parseColor ("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor (Color.parseColor ("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged( Editable editable ) {

            }
        });
        dialog.show ( );
    }

    /**
     * 对于评论回复的回复
     *
     * @param groupPosition 点击位置
     */
    private void replyReply( final int groupPosition , final int childPosition ) {
        dialog = new BottomSheetDialog (this);
        View commentView = LayoutInflater.from (this).inflate (R.layout.comment_dialog_layout , null);
        final EditText commentText = (EditText) commentView.findViewById (R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById (R.id.dialog_comment_bt);
        commentText.setHint ("回复 " + commentsList.get (groupPosition).getReplyList ( ).get (childPosition).getNickName ( ) + " 的评论:");
        dialog.setContentView (commentView);
        bt_comment.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick( View view ) {
                String replyContent = commentText.getText ( ).toString ( ).trim ( );
                if (!TextUtils.isEmpty (replyContent)) {

                    dialog.dismiss ( );
                    ReplyDetailBean detailBean = new ReplyDetailBean ("xh" , commentsList.get (groupPosition).getReplyList ( ).get (childPosition).getNickName ( ) , replyContent);
                    adapter.addTheReplyData (detailBean , groupPosition);
                    mCommentExpandableListView.expandGroup (groupPosition);
                    Toast.makeText (FoodListItemDetail.this , "回复成功" , Toast.LENGTH_SHORT).show ( );
                } else {
                    Toast.makeText (FoodListItemDetail.this , "回复内容不能为空" , Toast.LENGTH_SHORT).show ( );
                }
            }
        });
        commentText.addTextChangedListener (new TextWatcher ( ) {
            @Override
            public void beforeTextChanged( CharSequence charSequence , int i , int i1 , int i2 ) {

            }

            @Override
            public void onTextChanged( CharSequence charSequence , int i , int i1 , int i2 ) {
                if (!TextUtils.isEmpty (charSequence) && charSequence.length ( ) > 2) {
                    bt_comment.setBackgroundColor (Color.parseColor ("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor (Color.parseColor ("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged( Editable editable ) {

            }
        });
        dialog.show ( );
    }

    /**
     * by june on 2020/5/1
     * 获取时间方法
     */
    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("MM月dd日 HH时mm分" , Locale.getDefault ( ));
        return simpleDateFormat.format (new Date (System.currentTimeMillis ( )));
    }

    /**
     * by june on 2020/5/1
     * func:生成测试数据
     *
     * @return 评论数据
     */
    private List <CommentDetailBean> generateTestData() {
        String testJson = "{\n" +
                "\t\"code\": 1000,\n" +
                "\t\"message\": \"查看评论成功\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"total\": 3,\n" +
                "\t\t\"list\": [{\n" +
                "\t\t\t\t\"id\": 42,\n" +
                "\t\t\t\t\"nickName\": \"程序猿\",\n" +
                "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
                "\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
                "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
                "\t\t\t\t\"replyTotal\": 1,\n" +
                "\t\t\t\t\"createDate\": \"三分钟前\",\n" +
                "\t\t\t\t\"replyList\": [{\n" +
                "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
                "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
                "\t\t\t\t\t\"id\": 40,\n" +
                "\t\t\t\t\t\"commentId\": \"42\",\n" +
                "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
                "\t\t\t\t\t\"status\": \"01\",\n" +
                "\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
                "\t\t\t\t}]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"id\": 41,\n" +
                "\t\t\t\t\"nickName\": \"设计狗\",\n" +
                "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
                "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
                "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
                "\t\t\t\t\"replyTotal\": 1,\n" +
                "\t\t\t\t\"createDate\": \"一天前\",\n" +
                "\t\t\t\t\"replyList\": [{\n" +
                "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
                "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
                "\t\t\t\t\t\"commentId\": \"41\",\n" +
                "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
                "\t\t\t\t\t\"status\": \"01\",\n" +
                "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
                "\t\t\t\t}]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"id\": 40,\n" +
                "\t\t\t\t\"nickName\": \"产品喵\",\n" +
                "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
                "\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。\",\n" +
                "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
                "\t\t\t\t\"replyTotal\": 0,\n" +
                "\t\t\t\t\"createDate\": \"三天前\",\n" +
                "\t\t\t\t\"replyList\": []\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";
        Gson gson = new Gson ( );

        CommentBean commentBean = gson.fromJson (testJson , CommentBean.class);
        List <CommentDetailBean> commentList = commentBean.getData ( ).getList ( );
        return commentList;
    }

    /**
     * 菜单点击响应事件
     */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        if (item.getItemId ( ) == R.id.home) {
            finish ( );
            return true;
        }
        return super.onOptionsItemSelected (item);
    }

    /**
     * 跳转至登陆界面
     */
    private void sendToLogin() {
        Intent intent = new Intent (FoodListItemDetail.this , LoginByNickNameActivity.class);
        intent.putExtra ("activityFrom" , TAG);
        startActivity (intent);
    }

    /**
     * 弹窗提醒
     *
     * @param i i=1:取消关注弹窗确认
     *          i=2:跳转登陆界面确认
     */
    private void alertLog( int i ) {
        switch (i) {
            case 1:
                builder = new AlertDialog.Builder (this).setIcon (R.drawable.cry).setTitle ("取消关注确认")
                        .setMessage ("您确定取消关注" + bean.getUser ( ) + "吗？").setPositiveButton ("确定" , new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick( DialogInterface dialog , int which ) {
                                followStatus = false;
                                mFollowStatus.setImageResource (R.drawable.unfollow);
                                Toast.makeText (FoodListItemDetail.this , "您已成功取消对" + bean.getUser ( ) + "的关注！" , Toast.LENGTH_SHORT).show ( );
                            }
                        })
                        .setNegativeButton ("取消" , new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick( DialogInterface dialog , int which ) {
                                dialog.dismiss ( );
                            }
                        });
                builder.create ( ).show ( );
                break;
            case 2:
                builder = new AlertDialog.Builder (this).setIcon (R.drawable.smile).setTitle ("前往登陆确认")
                        .setMessage ("您即将前往登陆界面").setPositiveButton ("确定" , new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick( DialogInterface dialog , int which ) {
                                sendToLogin ( );
                            }
                        })
                        .setNegativeButton ("取消" , new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick( DialogInterface dialog , int which ) {
                                dialog.dismiss ( );
                            }
                        });
                builder.create ( ).show ( );
                break;
            default:
                break;
        }

    }


    /**
     * 初始化视频播放器
     */
    private void initJCVideo() {

        mJCVideo.setUp (videoUrl , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL , "video-title");
        Glide.with (this)
                .load (previewImgUrl)
                .into (mJCVideo.thumbImageView);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress ( )) {
            return;
        }
        super.onBackPressed ( );
    }

    @Override
    protected void onPause() {
        super.onPause ( );
        JCVideoPlayer.releaseAllVideos ( );
    }

    /**本地实现，update更新数据*/
    private void addLikeNum(){

    }
    private void reduceLikeNum(){

    }

    @Override
    protected void onStart() {
        if (followStatus){mFollowStatus.setImageResource (R.drawable.havefollowed);}
        else {mFollowStatus.setImageResource (R.drawable.unfollow);}
        if (likeStatus){mLike.setImageResource (R.drawable.like);}
        else {mLike.setImageResource (R.drawable.unlike);}
        super.onStart ( );
    }
}
