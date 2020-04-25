package com.example.user.jiancan;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.user.jiancan.bean.HomeListItemBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author june
 */
public class Detail extends AppCompatActivity {
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.detail);
        Intent intent = getIntent ();
        String jsonString = intent.getStringExtra("食物数据");
        Log.e ("3",jsonString );
        Gson gson = new GsonBuilder ().create ();
        HomeListItemBean bean = gson.fromJson (jsonString,HomeListItemBean.class);

        TextView tv_name = findViewById (R.id.detail_tv_name);
        TextView tv_user = findViewById (R.id.detail_tv_user);
        TextView tv_likeNum = findViewById (R.id.detail_tv_likeNum);
        tv_name.setText (bean.getName ());
        tv_user.setText (bean.getUser ());
        tv_likeNum.setText (bean.getLikeNum ());
    }
}
