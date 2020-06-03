package com.example.user.jiancan.personal.activityAndFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.jiancan.Constant;
import com.example.user.jiancan.personal.entity.Food;
import com.example.user.jiancan.personal.entity.TUser;
import com.example.user.jiancan.personal.util.PersonalFansAdapter;
import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.entity.User;
import com.example.user.jiancan.personal.util.PersonalFollowAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.user.jiancan.personal.util.PersonalFansAdapter.*;

public class PersonalFansListActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {
    private ListView lvFans;
    private PersonalFansAdapter adapter;
    private List<TUser> fans = new ArrayList<>();
    private OkHttpClient okHttpClient;
    private User user;
    private List<TUser> followers = new ArrayList<>();
    private Intent myIntent;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1 :
                    setAdapter();
                    break;
                case 2:
                    Toast.makeText(PersonalFansListActivity.this, "关注失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 3 :
                    //关注成功
                    PersonalFansAdapter.ViewHolder viewHolder = (PersonalFansAdapter.ViewHolder) getViewByPosition((Integer) msg.obj,lvFans).getTag();
                    viewHolder.cancel.setText("已关注");
                    viewHolder.cancel.setTextColor(Color.BLACK);
                    break;
                case 4:
                    Toast.makeText(PersonalFansListActivity.this, "取消关注失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    //取消关注
                    PersonalFansAdapter.ViewHolder viewHolder1 = (PersonalFansAdapter.ViewHolder) getViewByPosition((Integer) msg.obj,lvFans).getTag();
                    viewHolder1.cancel.setText("关注");
                    viewHolder1.cancel.setTextColor(Color.BLUE);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_fans);
        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取和更新个人信息
        user = new Gson().fromJson(sharedPreferences.getString("user", null), User.class);
        okHttpClient = new OkHttpClient();
        findViews();
        requestFansData();
        myIntent = getIntent();
        setResult(103,myIntent);
    }

    private void findViews() {
        lvFans = findViewById(R.id.lv_fans);
        requestFollowData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    /**
     * 实现类，响应按钮点击事件
     */
    private MyClickListener myClickListener = new MyClickListener() {
        @Override
        public void myOnClick(final int position, View v) {
            if (followers.contains(fans.get(position))){
                AlertDialog.Builder adBulider=new AlertDialog.Builder(PersonalFansListActivity.this);
                //对构造器进行设置
                adBulider.setTitle("提示");
                adBulider.setMessage("您确定要取消关注此用户吗？");
                adBulider.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelFollow(position);
                                dialog.dismiss();
                            }
                        });
                adBulider.setNegativeButton("取消",null);
                AlertDialog alertDialog = adBulider.create();
                //设置对话框不能被取消（点击页面其他地方，对话框自动关闭）
                alertDialog.setCancelable(false);
                alertDialog.show();
            }else{
                followFans(position);
            }

        }
    };
    /**
     * 获取listView中item的布局
     * @param pos 位置
     * @param listView listView
     * @return
     */
    private View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    private void followFans(final int position) {
        Log.e("follow",Constant.URL_FOLLOW + user.getId()+"/"+fans.get(position).getId());
        Request request = new Request.Builder().url(Constant.URL_FOLLOW + user.getId()+"/"+fans.get(position).getId()).build();
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
                String fanListStr = response.body().string();
                if (fanListStr.equals(0)){
                    Log.e("str",fanListStr);
                    Message message = new Message();
                    message.what = 2;
                    message.obj = "";
                    myHandler.sendMessage(message);
                }else {
                    Log.e("str",fanListStr);
                    Message message = new Message();
                    message.obj = position;
                    message.what = 3;
                    myHandler.sendMessage(message);
                }
            }
        });
    }

    private void cancelFollow(final int position) {
        Log.e("follow",Constant.URL_UNFOLLOW + user.getId()+"/"+fans.get(position).getId());
        Request request = new Request.Builder().url(Constant.URL_UNFOLLOW + user.getId()+"/"+fans.get(position).getId()).build();
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
                String fanListStr = response.body().string();
                if (fanListStr.equals("0")){
                    Log.e("str",fanListStr);
                    Message message = new Message();
                    message.what = 4;
                    message.obj = "";
                    myHandler.sendMessage(message);
                }else {
                    Log.e("str",fanListStr);
                    Message message = new Message();
                    message.obj = position;
                    message.what = 5;
                    myHandler.sendMessage(message);
                }
            }
        });
    }

    private void setAdapter() {
        adapter = new PersonalFansAdapter(fans,followers ,R.layout.followers_listview_item,
                PersonalFansListActivity.this,myClickListener);
        lvFans.setAdapter(adapter);
    }

    private void requestFollowData() {
        Request request = new Request.Builder().url(Constant.URL_FOLLOW_LIST + user.getId()).build();
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
                String fanListStr = response.body().string();
                Type type = new TypeToken<List<TUser>>(){}.getType();
                Log.e("follow",fanListStr.toString());
                Gson gson = new GsonBuilder().serializeNulls().create();
                followers.addAll((List<TUser>) gson.fromJson(fanListStr,type));
            }
        });
    }
    private void requestFansData() {
        Request request = new Request.Builder().url(Constant.URL_FAN + user.getId()).build();
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
                String fanListStr = response.body().string();
                Type type = new TypeToken<List<TUser>>(){}.getType();
                Log.e("fanListStr",fanListStr.toString());
                Gson gson = new GsonBuilder().serializeNulls().create();
                fans.addAll((List<TUser>) gson.fromJson(fanListStr,type));
                Message message = new Message();
                message.what = 1;
                message.obj = "";
                myHandler.sendMessage(message);
            }
        });
    }

}
