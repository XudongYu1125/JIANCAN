package com.example.user.jiancan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PersonalFollowListActivity extends AppCompatActivity {
    private ListView lvFollowers;
    private PersonalFollowAdapter adapter;
    private List<User> followers = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_followers);

        findViews();
    }
    private void findViews() {
        lvFollowers = findViewById(R.id.lv_follwers);
        adapter = new PersonalFollowAdapter(followers, R.layout.followers_listview_item, PersonalFollowListActivity.this);
        lvFollowers.setAdapter(adapter);
    }
}
