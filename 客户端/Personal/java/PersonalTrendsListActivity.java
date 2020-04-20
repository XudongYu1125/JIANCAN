package com.example.user.jiancan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class PersonalTrendsListActivity extends AppCompatActivity {
    private ListView lvFoods;
    private PersonalTrendsAdapter adapter;
    private List<Food> foods = new ArrayList<>();

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
}
