package com.example.user.jiancan.message.activityAndFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText et;
    private String input;
    private User user;
    private ImageView imageView;
    /// private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences;
    int i = 0;

    int[] img = {R.drawable.back, R.drawable.back};
    String[] content = {"结界，我明天上班想吃大盘鸡，整个教程", "谢谢你啦"};
    // 0->i 1->u
    int[] who = {0, 1};

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("213", msg.what + "");
            switch (msg.what) {
                case 0:
                    chatRecordAdapter = new ChatRecordAdapter(com.example.user.jiancan.message.activityAndFragment.ChatRecordActivity.this, dataSource, R.layout.message_talk);
                    listView.setAdapter(chatRecordAdapter);
                    break;
                case 1:
                    Log.e("213", "21");
                    Toast.makeText(com.example.user.jiancan.message.activityAndFragment.ChatRecordActivity.this, "发送不能为空", Toast.LENGTH_SHORT);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_talk);
        //去除标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        textView = findViewById(R.id.tv_about);
        textView.setText("郜三");
        initDatalv1();

        listView = findViewById(R.id.con_lv);


    }



    /**
     * 分享信息传递
     * */
    private void initShareMessage(){

        Intent intent = getIntent ();
        String jsonStr = intent.getStringExtra ("分享信息");
        SharedItem sharedItem = new Gson ().fromJson (jsonStr,SharedItem.class);

    }

    private void initDatalv1() {

        dataSource = new ArrayList<>();
        for (i = 0; i < img.length; ++i) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", img[i]);
            map.put("who", img[i]);
            map.put("content", content[i]);
            dataSource.add(map);
        }

        Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);

    }

    public void send1() {

        et = findViewById(R.id.con_et);
        input = et.getText().toString();
        if (input == null) {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
        int[] img1 = {R.drawable.back, R.drawable.back, R.drawable.oop};
        String[] content1 = {"结界，我明天上班想吃大盘鸡，整个教程", "谢谢你啦", input};
        // 0->i 1->u
        int[] who1 = {0, 0, 1};

        Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);
    }

    // 查看个人信息
    public void toself(View view) {
        Intent it1 = new Intent();
        it1.setClass(com.example.user.jiancan.message.activityAndFragment.ChatRecordActivity.this, SelfActivity.class);
        // 判断点击的是sender还是receiver 传递id
        switch (view.getId()) {
            case R.id.con_imgi:
                it1.putExtra("userid", 1);
                break;
            case R.id.con_imgu:
                it1.putExtra("userid", 2);
                break;
        }
        startActivity(it1);
    }

}
