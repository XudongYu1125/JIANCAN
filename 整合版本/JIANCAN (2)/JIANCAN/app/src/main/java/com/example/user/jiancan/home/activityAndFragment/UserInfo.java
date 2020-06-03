package com.example.user.jiancan.home.activityAndFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.Food;
import com.example.user.jiancan.home.entity.HomeListItemBean;

import com.example.user.jiancan.home.util.RecyclerViewDisplayAdapter;
import com.example.user.jiancan.personal.entity.TUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.GridLayout.VERTICAL;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.example.user.jiancan.home.util.FormatNum.formatBigNum;

/**
 * @author june
 */
public class UserInfo extends AppCompatActivity {

    private ImageView mHeaderBackImg;
    private ImageView mHeaderPhotoImg;
    private TextView mHeaderUserName;
    private TextView mHeaderFansNum;
    private RecyclerView mRecyclerView;
    private ArrayList <HomeListItemBean> dataBeanList;
    private String imgUrl;
    private RecyclerViewDisplayAdapter adapter;
    private OkHttpClient okHttpClient;
    private List<Food> foods;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.show_user_info);
        initView ( );
        loadTestData ( );
        setHeaderImg ( );
        testDisplayWorks ( );
    }

    /**
     * 注册组件
     */
    private void initView() {
        mHeaderBackImg = findViewById (R.id.h_back);
        mHeaderPhotoImg = findViewById (R.id.h_head);
        mHeaderUserName = findViewById (R.id.user_name);
        mHeaderFansNum = findViewById (R.id.user_fans_num);
        mRecyclerView = findViewById (R.id.user_info_publish_list);
    }

    @SuppressLint("SetTextI18n")
    private void loadTestData() {
        TUser user = getUserFromIntent ();
        imgUrl = "http://img3.imgtn.bdimg.com/it/u=1974743131,3417705072&fm=26&gp=0.jpg";
        String userName = user.getNickname ();
        int fansNum = 12000;
        String fans = formatBigNum (String.valueOf (fansNum));
        mHeaderUserName.setText (userName);
        mHeaderFansNum.setText (fans + "粉丝");
    }

    private TUser getUserFromIntent() {
        Intent intent = getIntent ();
        String userInfo = intent.getStringExtra ("用户信息");
        TUser user = new Gson ().fromJson (userInfo,TUser.class);
        return user;
    }

    /**
     * 对头像及背景进行处理
     */
    private void setHeaderImg() {
        MultiTransformation multi = new MultiTransformation (
                new BlurTransformation (25) ,
                new CenterCrop ( )
        );
        Glide.with (this).load (imgUrl)
                .apply (bitmapTransform (multi))
                .into (mHeaderBackImg);

        Glide.with (this).load (imgUrl)
                .apply (bitmapTransform (new CropCircleTransformation ( )))
                .into (mHeaderPhotoImg);
    }

    /**
     * 模拟测试recycler view数据展示
     */
    private void testDisplayWorks() {

        dataBeanList = new ArrayList <> ( );
        for (int i = 0; i < 7; i++) {
            HomeListItemBean bean = new HomeListItemBean ( );
            bean.setShowImg ("http://img1.imgtn.bdimg.com/it/u=1141259048,554497535&fm=26&gp=0.jpg");
            bean.setName ("1" + i + "1");
            bean.setLikeNum (String.valueOf (1000 * i));
            bean.setUser (getUserFromIntent ().getNickname ());
            dataBeanList.add (bean);
        }
        adapter = new RecyclerViewDisplayAdapter (this , dataBeanList);
        mRecyclerView.setAdapter (adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager (this , 2) {
            @Override
            public boolean canScrollVertically() {
                // 直接禁止垂直滑动
                return false;
            }
        };
        //通过布局管理器可以控制条目排列的顺序
        gridLayoutManager.setReverseLayout (false);
        //设置RecycleView显示的方向
        gridLayoutManager.setOrientation (VERTICAL);
        //设置布局管理器， 参数linearLayoutManager对象
        mRecyclerView.setLayoutManager (gridLayoutManager);
    }

    /**
     * 从后台取得有关此用户的作品信息
     * */
    private List<HomeListItemBean> getPublisherWorks(){
        TUser u = getUserFromIntent ();
        Request request = new Request.Builder ()
                .url (Constant.URL_GET_FOOD_BY_USER+u.getId ())
                .get ()
                .build ();
        Call call = okHttpClient.newCall (request);
        call.enqueue (new Callback ( ) {
            @Override
            public void onFailure( Call call , IOException e ) {
                e.printStackTrace ();
                Log.e ("UserInfo",e.getMessage ());
            }

            @Override
            public void onResponse( Call call , Response response ) throws IOException {
                String jsonStr = response.body ().string ();
                Gson gson = new Gson ();
                Type type = new TypeToken<List<Food>> (){}.getType ();
                foods = gson.fromJson (jsonStr,type);
            }
        });

        for (Food f : foods){
            HomeListItemBean homeListItemBean = new HomeListItemBean ();
            homeListItemBean.setFoodId (f.getId ());
            homeListItemBean.setUserId (f.getUserId ());
            homeListItemBean.setName (f.getFoodName ());
            //homeListItemBean.setShowImg (f.getImages ());
            homeListItemBean.setVideoUrl (f.getVideo ());
            dataBeanList.add (homeListItemBean);
        }

        return dataBeanList;

    }
}
