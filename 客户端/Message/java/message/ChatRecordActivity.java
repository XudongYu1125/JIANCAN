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
import com.example.user.jiancan.User;
import com.example.user.jiancan.message.util.ChatRecordAdapter;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<String, Object> map1 = new HashMap<>();

    int i = 0;

    int[] img = {R.drawable.back, R.drawable.back, R.drawable.back};
    String[] content = {"结界，我明天上班想吃大盘鸡，整个教程", "谢谢你啦", "11"};
    // 0->i 1->u
    int[] who = {1, 1, 1};

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

                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_talk);
        //去除标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        /*
        okHttpClient = new OkHttpClient();
        sharedPreferences = getActivity().getSharedPreferences("loginInfo", MODE_PRIVATE);
        Gson gson = new GsonBuilder().serializeNulls() .create();
        user = gson.fromJson(sharedPreferences.getString("user", null), User.class);
        */

        textView = findViewById(R.id.tv_about);
        textView.setText("郜三");
        initDatalv1();

        listView = findViewById(R.id.con_lv);


    }

    private void initDatalv1() {

        dataSource = new ArrayList<>();
        for (i = 0; i < img.length; ++i) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", img[i]);
            map.put("who", who[i]);
            map.put("content", content[i]);
            dataSource.add(map);
        }

        Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);

    }

    private void initDatalv() {

    }

    public void send1(View view) {

        et = findViewById(R.id.con_et);
        input = et.getText().toString();
        if (input.equals("")) {
            Toast.makeText(ChatRecordActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        map1.put("img", R.drawable.oop);
        map1.put("who", 0);
        map1.put("content", input);
        chatRecordAdapter.addItem(map1);

    }

    //发送消息
    /* public void send(View view) {
        //发送消息
        Log.e("send", "aa");
        et = findViewById(R.id.con_et);
        input = et.getText().toString();
        if (input.equals("")) {
            Toast.makeText(.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatRecord chatrecord = new ChatRecord();

                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                Date dt = new Date();
                String date = sdf.format(dt.getTime());
                Log.e("时间哦", date);
                chatrecord.setContent(input);
                chatrecord.setDate(date);
                chatrecord.setSenderid(user.getUserid());
                chatrecord.setReceiverid(recevieid);
                chatrecord.setUUID(uuid);

                try {
                    URL url = new URL(Constant.URL_CHAT_LOCAL + "Add");
                    //获得连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.connect();
                    conn.getOutputStream().write(gson.toJson(chatrecord).getBytes("UTF-8"));
                    conn.getOutputStream().flush();

                    InputStream inputStream = conn.getInputStream();
                    byte[] buffer = new byte[2048];
                    int len;
                    StringBuffer stringBuffer = new StringBuffer();
                    while ((len = inputStream.read(buffer)) != -1) {
                        stringBuffer.append(new String(buffer, 0, len));
                    }
                    JSONObject response = new JSONObject(stringBuffer.toString());
                    boolean bl = (Boolean) response.get("isSuccess");
                    Log.e("成功", bl + "");
                    if (bl == true) {
                        Log.e("成功", bl + "");
                        if (uuid == 0) {
                            uuid = Integer.parseInt((String) response.get("UUID"));
                            Log.e("uud=的id", "" + uuid);
                            chatrecord.setUUID(uuid);
                        }
                        String strid = (String) response.get("chatRecordId");
                        int crid = Integer.parseInt(strid);
                        chatrecord.setChatrecordid(crid);
                        newid = crid;
                        Log.e("这是新的chatrecordid", String.valueOf(newid));

                        Bundle bundle = new Bundle();
                        Message msg = new Message();
                        msg.what = 2;
                        bundle.putSerializable("ChatRecord", (Serializable) chatrecord);
                        msg.setData(bundle);
                        handler.sendMessage(msg);


                    } else {
                        Log.e("12321321321", "2");
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                    inputStream.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }*/

    public void sendemoji(View view) {
        //表情
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

    // 返回时向MessageActivity传递数据
    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Log.e("返回事件", "123");
        String content = "1231";
        String date = "1232";
        int id = 123;
        /*
        for (int j = 0; j < dataSource.size(); j++) {
            if (dataSource.get(j).getChatrecordid() == newid) {
                content = dataSource.get(j).getContent();
                date = dataSource.get(j).getDate();
                id = dataSource.get(j).getUUID();
            }
        }
        i.putExtra("content", content);
        i.putExtra("date", date);
        i.putExtra("UUID", id);
        */

        ChatRecordActivity.this.setResult(1, i);
        ChatRecordActivity.this.finish();
    }
}
