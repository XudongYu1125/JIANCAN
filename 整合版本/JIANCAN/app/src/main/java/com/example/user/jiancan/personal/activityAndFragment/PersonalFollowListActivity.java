package com.example.user.jiancan.personal.activityAndFragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.jiancan.Constant;
import com.example.user.jiancan.personal.util.PersonalFansAdapter;
import com.example.user.jiancan.personal.util.PersonalFollowAdapter;
import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.entity.User;
import com.example.user.jiancan.personal.util.PersonalHistoryAdapter;
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

public class PersonalFollowListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lvFollowers;
    private PersonalFollowAdapter adapter;
    private List<User> followers = new ArrayList<>();
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_followers);
        User user = new User();
        user.setNickname("山河永蔚");
        followers.add(user);
        User user1 = new User();
        user1.setNickname("山河永蔚1");
        followers.add(user1);
        User user2 = new User();
        user2.setNickname("山河永蔚2");
        followers.add(user2);
        findViews();
        lvFollowers.setOnItemClickListener(this);
//        requestData();
    }

    private void findViews() {
        lvFollowers = findViewById(R.id.lv_follwers);
        adapter = new PersonalFollowAdapter(followers, R.layout.followers_listview_item, PersonalFollowListActivity.this,mListener);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 实现类，响应按钮点击事件
     */
    private PersonalFollowAdapter.MyClickListener mListener = new PersonalFollowAdapter.MyClickListener() {
        @Override
        public void myOnClick(final int position, View v) {
            AlertDialog.Builder adBulider=new AlertDialog.Builder(PersonalFollowListActivity.this);
            //对构造器进行设置
            adBulider.setTitle("提示");
            adBulider.setMessage("您确定要取消关注此用户吗？");
            adBulider.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            followers.remove(position);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
            adBulider.setNegativeButton("取消",null);
            AlertDialog alertDialog = adBulider.create();
            //设置对话框不能被取消（点击页面其他地方，对话框自动关闭）
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    };
}
