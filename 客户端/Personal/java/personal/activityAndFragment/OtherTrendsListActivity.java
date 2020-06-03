package com.example.user.jiancan.personal.activityAndFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.entity.Food;
import com.example.user.jiancan.personal.entity.TUser;
import com.example.user.jiancan.personal.util.OtherTrendsAdapter;
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

public class OtherTrendsListActivity extends AppCompatActivity {
    private String str =null;
    private Intent intent;
    private TUser tUser;
    private List<Food> foods = new ArrayList<>();
    private OkHttpClient okHttpClient;
    private ListView lvFoods;
    private ImageView imageView;
    private TextView textView;
    private TextView tvSex;
    private OtherTrendsAdapter adapter;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            setAdapter();
            super.handleMessage(msg);
        }
    };

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_trends);
        findView();
        okHttpClient = new OkHttpClient();
        intent = getIntent();
        str = intent.getStringExtra("fan");
        tUser = new Gson().fromJson(str,TUser.class);
        setContent();
        requestData();
    }

    private void setAdapter() {
        adapter = new OtherTrendsAdapter(foods,tUser, R.layout.trends_listview_item,OtherTrendsListActivity .this);
        lvFoods.setAdapter(adapter);
    }

    private void findView() {
        lvFoods = findViewById(R.id.lv_other_trend);
        imageView = findViewById(R.id.iv_tuser);
        textView = findViewById(R.id.tv_tuser_nickname);
        tvSex = findViewById(R.id.tv_tuser_sex);
    }

    private void setContent() {
        RequestOptions options = new RequestOptions().error(R.drawable.default_vatar);
        Glide.with(this)
                .load(Constant.BASE_URL + "/upload/avatarimgs/" + tUser.getImageUrl())
                .apply(options)
                .into(imageView);
        textView.setText(tUser.getNickname());
        if (tUser.getSex().equals("女")){
            tvSex.setText("她的动态");
        }
    }
    private void requestData() {
        Request request = new Request.Builder().url(Constant.URL_HISTORY + tUser.getId()).build();
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
                Log.e("foodstr",foodListStr.toString());
                Type type = new TypeToken<List<Food>>(){}.getType();
                Gson gson = new GsonBuilder().serializeNulls().create();
                foods.addAll((List<Food>) gson.fromJson(foodListStr,type));
                Log.e("food",foods.toString());
                Message message = new Message();
                message.obj = "";
                myHandler.sendMessage(message);
            }

        });
    }

}
