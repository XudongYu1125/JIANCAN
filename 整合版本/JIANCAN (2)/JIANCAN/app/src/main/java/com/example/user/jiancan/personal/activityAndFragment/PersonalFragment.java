package com.example.user.jiancan.personal.activityAndFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.entity.Food;
import com.example.user.jiancan.personal.entity.TUser;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {
    private LinearLayout llMessage;
    private LinearLayout llTrends;
    private LinearLayout llFollow;
    private LinearLayout llFan;
    private LinearLayout llHistroy;
    private LinearLayout llCollection;
    private LinearLayout llDownload;
    private TextView tvTrend;
    private TextView tvFollower;
    private TextView tvFans;

    private LinearLayout llSettings;
    private Intent intent;
    private ImageView imageView;
    private TextView textView;
    private User user ;
    private OkHttpClient okHttpClient;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {      //判断标志位
                case 1:
                    Log.e("111",msg.obj.toString());
                    tvTrend.setText(msg.obj.toString());
                    break;
                case 2:
                    Log.e("222",msg.obj.toString());
                    tvFollower.setText(msg.obj.toString());
                    break;
                case 3:
                    Log.e("333",msg.obj.toString());
                    tvFans.setText(msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private SharedPreferences sharedPreferences ;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_personal,container,false );
        findViews(view);
        setOnclicked();
        user = new User();
        user.setId(1);
        user.setSex("男");
        user.setNickname("郜三");
        user.setPhone("15130709309");
        user.setImageUrl("1.jpg");
        user.setPassword("123456");
        okHttpClient = new OkHttpClient();
        sharedPreferences = getActivity().getSharedPreferences("loginInfo", MODE_PRIVATE);
        //添加到loginInfo
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("loginInfo",MODE_PRIVATE).edit();
        editor.putString("user",new Gson().toJson(user));
        editor.commit();
        //获取和更新个人信息
        Gson gson = new GsonBuilder().serializeNulls() .create();
        user = gson.fromJson(sharedPreferences.getString("user", null), User.class);
//        Log.e("user",user.toString());
        requestTrendsData();
        requestFollowersData(user.getId());
        requestFansData(user.getId());
        String time = sharedPreferences.getString("time",null);
        if (time == null){
            RequestOptions options = new RequestOptions().error(R.drawable.default_vatar);
            Glide.with(this)
                    .load(Constant.BASE_URL + "/upload/avatarimgs/" + user.getImageUrl())
                    .apply(options)
                    .into(imageView);
        }else {
            RequestOptions options = new RequestOptions().signature(new ObjectKey(time)).error(R.drawable.default_vatar);
            Glide.with(this)
                    .load(Constant.BASE_URL + "/upload/avatarimgs/" + user.getImageUrl())
                    .apply(options)
                    .into(imageView);
        }
        textView.setText(user.getNickname().toString());

        return view;
    }

    private void requestFollowersData(int id) {
        Log.e("fol",Constant.URL_FOLLOW_LIST + user.getId());
        Request request = new Request.Builder().url(Constant.URL_FOLLOW_LIST + id).build();
        final Call call = okHttpClient.newCall(request);
        //发送请求
        call.enqueue(new Callback() {
            @Override
            //请求失败时回调
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            //请求成功后回调
            public void onResponse(Call call, Response response) throws IOException {
                //不直接更新界面
                String fanListStr = response.body().string();
                Type type = new TypeToken<List<TUser>>(){}.getType();
                Log.e("follow",fanListStr.toString());
                Gson gson = new GsonBuilder().serializeNulls().create();
                List<TUser> followers = new ArrayList<>();
                followers.addAll((List<TUser>) gson.fromJson(fanListStr,type));
                Message message = new Message();
                message.what = 2;
                message.obj = ""+followers.size();
                myHandler.sendMessage(message);
            }
        });
    }

    private void requestFansData(int id) {
        Log.e("fol",Constant.URL_FAN + id);
        Request request = new Request.Builder().url(Constant.URL_FAN + id).build();
        final Call call = okHttpClient.newCall(request);
        //发送请求
        call.enqueue(new Callback() {
            @Override
            //请求失败时回调
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            //请求成功后回调
            public void onResponse(Call call, Response response) throws IOException {
                //不直接更新界面
                String fanListStr = response.body().string();
                Type type = new TypeToken<List<TUser>>(){}.getType();
                Log.e("fanListStr",fanListStr.toString());
                Gson gson = new GsonBuilder().serializeNulls().create();
                List<TUser> fans = new ArrayList<>();
                fans.addAll((List<TUser>) gson.fromJson(fanListStr,type));
                Message message = new Message();
                message.what = 3;
                message.obj = ""+fans.size();
                myHandler.sendMessage(message);
            }
        });
    }

    private void requestTrendsData() {
        Request request = new Request.Builder().url(Constant.URL_TRENDS + user.getId()).build();
        final Call call = okHttpClient.newCall(request);
        //发送请求
        call.enqueue(new Callback() {
            @Override
            //请求失败时回调
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            //请求成功后回调
            public void onResponse(Call call, Response response) throws IOException {
                //不直接更新界面
                String foodListStr = response.body().string();
                Log.e("foods",foodListStr);
                String newFood = truncateHeadString(foodListStr,8);
                newFood = newFood .substring(0, newFood.length() - 1);
                Log.e("newfood",newFood);
                Type type = new TypeToken<List<Food>>(){}.getType();
                Gson gson = new GsonBuilder().serializeNulls().create();
                List<Food> foods = new ArrayList<>();
                foods.addAll((List<Food>) gson.fromJson(newFood,type));
                Message message = new Message();
                message.what = 1;
                message.obj = ""+foods.size();
                myHandler.sendMessage(message);
            }

        });
    }
    public static String truncateHeadString(String origin, int count) {
        if (origin == null || origin.length() < count) {
            return null;
        }
        char[] arr = origin.toCharArray();
        char[] ret = new char[arr.length - count];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = arr[i + count];
        }
        return String.copyValueOf(ret);
    }
    private void findViews(View view) {
        tvTrend = view.findViewById(R.id.tv_trends_num);
        tvFollower = view.findViewById(R.id.tv_follow_num);
        tvFans = view.findViewById(R.id.tv_fans_num);
        imageView = view.findViewById(R.id.iv_person);
        textView = view.findViewById(R.id.tv_person_nickname);
        llMessage = view.findViewById(R.id.ll_personal_message);
        llTrends = view.findViewById(R.id.ll_trends);
        llFollow = view.findViewById(R.id.ll_follow);
        llFan = view.findViewById(R.id.ll_fan);
        llHistroy = view.findViewById(R.id.ll_histroy);
        llCollection = view.findViewById(R.id.ll_collection);
        llDownload = view.findViewById(R.id.ll_download);
        llSettings = view.findViewById(R.id.ll_settings);
    }
    private void setOnclicked() {
        llMessage.setOnClickListener(new onClicked());
        llTrends.setOnClickListener(new onClicked());
        llFollow.setOnClickListener(new onClicked());
        llFan.setOnClickListener(new onClicked());
        llHistroy.setOnClickListener(new onClicked());
        llCollection.setOnClickListener(new onClicked());
        llDownload.setOnClickListener(new onClicked());
        llSettings.setOnClickListener(new onClicked());
    }

    private class onClicked implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_personal_message:
                    //跳转到修改个人信息界面
                    Log.e("跳转到","个人信息界面");
                    intent = new Intent();
                    intent.setClass(getContext(),PersonalChangeMessageActivity.class);
                    startActivityForResult(intent,100);
                    break;
                case R.id.ll_trends:
                    //跳转到用户自己的发布的动态界面
                    Log.e("跳转到","个人动态界面");
                    intent = new Intent();
                    intent.setClass(getContext(),PersonalTrendsListActivity.class);
                    startActivityForResult(intent,101);
                    break;
                case R.id.ll_follow:
                    //跳转到用户的关注列表界面
                    Log.e("跳转到","个人关注列表界面");
                    intent = new Intent();
                    intent.setClass(getContext(),PersonalFollowListActivity.class);
                    startActivityForResult(intent,102);
                    break;
                case R.id.ll_fan:
                    //跳转到用户的粉丝列表
                    Log.e("跳转到","个人粉丝列表界面");
                    intent = new Intent();
                    intent.setClass(getContext(),PersonalFansListActivity.class);
                    startActivityForResult(intent,103);
                    break;
                case R.id.ll_histroy:
                    //跳转到用户的历史记录界面
                    Log.e("跳转到","历史记录列表界面");
                    intent = new Intent();
                    intent.setClass(getContext(),PersonalHistroyListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_collection:
                    //跳转到用户的收藏界面
                    Log.e("跳转到","收藏列表界面");
                    intent = new Intent();
                    intent.setClass(getContext(),PersonalCollectionListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_download:
                    //跳转到用户的下载记录界面
                    Log.e("跳转到","收藏设置界面");
                    intent = new Intent();
                    intent.setClass(getContext(),PersonalDownloadListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_settings:
                    //跳转到用户的设置界面
                    Log.e("跳转到","收藏设置界面");
                    intent = new Intent();
                    intent.setClass(getContext(),PersonlSettingsActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 101:
                String time = sharedPreferences.getString("time",null);
                if (time == null){
                    RequestOptions options = new RequestOptions().error(R.drawable.default_vatar);
                    Glide.with(this)
                            .load(Constant.BASE_URL + "/upload/avatarimgs/" + user.getImageUrl())
                            .apply(options)
                            .into(imageView);
                }else {
                    RequestOptions options = new RequestOptions().signature(new ObjectKey(time)).error(R.drawable.default_vatar);
                    Glide.with(this)
                            .load(Constant.BASE_URL + "/upload/avatarimgs/" + user.getImageUrl())
                            .apply(options)
                            .into(imageView);
                }
                user = new Gson().fromJson(sharedPreferences.getString("user", null), User.class);
                textView.setText(user.getNickname());
                break;
            case 102:
                requestFollowersData(user.getId());
                break;
            case 103:
                requestFollowersData(user.getId());
                break;
            case 200:
               requestTrendsData();
                break;
        }
    }
}
