package com.example.user.jiancan.message.activityAndFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.SharedItem;
import com.example.user.jiancan.message.util.ChatRecordAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dxn
 */
public class ChatRecordActivity extends AppCompatActivity {

    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private ListView listView;
    private ChatRecordAdapter chatRecordAdapter = null;
    private TextView textView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_talk);
        //去除标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        textView = findViewById(R.id.tv_about);
        textView.setText("加人");
        initDatalv();

        listView = findViewById(R.id.con_lv);
        chatRecordAdapter = new ChatRecordAdapter(ChatRecordActivity.this, dataSource, R.layout.message_talk);
        listView.setAdapter(chatRecordAdapter);


    }

    private void initDatalv() {

        int i = 0;

        int[] img = {R.drawable.back, R.drawable.back};
        String[] content = {"结界，我明天上班想吃大盘鸡，整个教程", "谢谢你啦"};
        // 0->i 1->u
        int[] who = {0, 1};

        dataSource = new ArrayList<>();
        for (i = 0; i < img.length; ++i) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", img[i]);
            map.put("who", img[i]);
            map.put("content", content[i]);
            dataSource.add(map);
        }

    }


    /**
     * 分享信息传递
     * */
    private void initShareMessage(){

        Intent intent = getIntent ();
        String jsonStr = intent.getStringExtra ("分享信息");
        SharedItem sharedItem = new Gson ().fromJson (jsonStr,SharedItem.class);

    }

}
