package com.example.user.jiancan;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
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

public class PersonalFollowListActivity extends AppCompatActivity {
    private ListView lvFollowers;
    private PersonalFollowAdapter adapter;
    private List<User> followers = new ArrayList<>();
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_followers);
        User user = new User();
        user.setNickname("aaaa");
        followers.add(user);
        findViews();
        requestData();
    }
    private void findViews() {
        lvFollowers = findViewById(R.id.lv_follwers);
        adapter = new PersonalFollowAdapter(followers, R.layout.followers_listview_item, PersonalFollowListActivity.this);
        lvFollowers.setAdapter(adapter);
    }
    private void requestData() {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),"1");
        Request request = new Request.Builder().url(Constant.URL_FOLLOW).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String followListStr = response.body().string();
                Type type = new TypeToken<List<User>>(){}.getType();
                followers.addAll((List<User>) new Gson().fromJson(followListStr,type));
                Log.e("followers",followers.toString());
            }
        });
    }
}
