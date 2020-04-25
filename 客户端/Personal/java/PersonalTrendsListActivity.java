package com.example.user.jiancan.personal.activityAndFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.user.jiancan.Constant;
import com.example.user.jiancan.personal.util.PersonalTrendsAdapter;
import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.entity.Food;
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

public class PersonalTrendsListActivity extends AppCompatActivity {
    private ListView lvFoods;
    private PersonalTrendsAdapter adapter;
    private List<Food> foods = new ArrayList<>();
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_trends);
        Food food = new Food();
        food.setName("蛋挞");
        foods.add(food);
        findViews();
    }
    private void findViews() {
        lvFoods = findViewById(R.id.lv_trends);
        adapter = new PersonalTrendsAdapter(foods, R.layout.trends_listview_item, PersonalTrendsListActivity.this);
        lvFoods.setAdapter(adapter);
    }
    private void requestData() {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),"1");
        Request request = new Request.Builder().url( Constant.URL_TRENDS).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("error",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String foodListStr = response.body().string();
                Type type = new TypeToken<List<Food>>(){}.getType();
                foods.addAll((List<Food>) new Gson().fromJson(foodListStr,type));
                Log.e("fans",foods.toString());
            }
        });
    }
}
