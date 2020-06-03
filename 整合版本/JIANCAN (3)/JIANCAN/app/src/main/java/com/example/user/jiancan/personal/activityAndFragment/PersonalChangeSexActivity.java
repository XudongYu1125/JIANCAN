package com.example.user.jiancan.personal.activityAndFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalChangeSexActivity extends AppCompatActivity {

    private Intent intent;
    private TextView btnMale;
    private TextView btnFemale;
    private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences;
    private User user;
    private static Activity ac;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(ac,"修改信息失败！",Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_sex);
        ac =this;
        sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);

        user = new Gson().fromJson(sharedPreferences.getString("user",null),User.class);
        intent = getIntent();
        findViews();
        btnMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("changsex","男");
                setResult(201,intent);
                okHttpClient = new OkHttpClient();
                user.setSex("男");
                requestData(user);
            }
        });
        btnFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("changsex","女");
                setResult(201,intent);
                okHttpClient = new OkHttpClient();
                user.setSex("女");
                requestData(user);
            }
        });
    }

    private void requestData(final User user) {
        Request request = new Request.Builder().url(Constant.URL_CHANGE_NAME + new Gson().toJson(user)).build();
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
                String result = response.body().string();
                if (result.equals("0")){
                    Message message = new Message();
                    message.obj = "";
                    myHandler.sendMessage(message);
                }else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user",new Gson().toJson(user));
                    editor.commit();
                    Log.e("222","成功");
                    finish();
                }
            }

        });

    }

    private void findViews() {
        btnFemale = findViewById(R.id.btn_change_female);
        btnMale = findViewById(R.id.btn_change_male);
    }
}
