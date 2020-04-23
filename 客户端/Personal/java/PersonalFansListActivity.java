package com.example.user.jiancan;

import android.os.Bundle;
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

public class PersonalFansListActivity extends AppCompatActivity {
    private ListView lvFans;
    private PersonalFansAdapter adapter;
    private List<User> fans = new ArrayList<>();
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_fans);
        User user = new User();
        user.setNickname("bbbbb");
        fans.add(user);
        findViews();
        requestData();
    }

    private void findViews() {
        lvFans = findViewById(R.id.lv_fans);
        adapter = new PersonalFansAdapter(fans, R.layout.followers_listview_item, PersonalFansListActivity.this);
        lvFans.setAdapter(adapter);
    }
    private void requestData() {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),"1");
        Request request = new Request.Builder().url( Constant.URL_FAN).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("error",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String fanListStr = response.body().string();
                Type type = new TypeToken<List<User>>(){}.getType();
                fans.addAll((List<User>) new Gson().fromJson(fanListStr,type));
                Log.e("fans",fans.toString());
            }
        });
    }
}
