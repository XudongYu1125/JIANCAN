package com.example.user.jiancan.personal.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.activityAndFragment.OtherTrendsListActivity;
import com.example.user.jiancan.personal.entity.TUser;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonalFansAdapter extends BaseAdapter {
    private List<TUser> fans ;
    private int item_id;
    private Context context;
    List<TUser> followers ;
    ViewHolder viewHolder ;
    private PersonalFansAdapter.MyClickListener mListener;
    public PersonalFansAdapter(List<TUser> fans, List<TUser> followers,int item_id, Context context,PersonalFansAdapter.MyClickListener listener) {
        this.fans = fans;
        this.followers = followers;
        this.item_id = item_id;
        this.mListener = listener;
        this.context = context;
    }
    @Override
    public int getCount() {
        if (fans.size() != 0){
            return fans.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (fans.size()!=0){
            return fans.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_id, null);
            viewHolder = new ViewHolder();
            viewHolder.tvFansName = convertView.findViewById(R.id.tv_item_name);
            viewHolder.llFans = convertView.findViewById(R.id.ll_followers);
            viewHolder.ivUser = convertView.findViewById(R.id.iv_user_pic);
            viewHolder.cancel = convertView.findViewById(R.id.btn_unfollow);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (followers.contains(fans.get(position))){
            viewHolder.cancel.setText("已关注");
            viewHolder.cancel.setTextColor(Color.BLACK);
        }else {
            viewHolder.cancel.setText("关注");
            viewHolder.cancel.setTextColor(Color.BLUE);
        }
        viewHolder.tvFansName.setText(fans.get(position).getNickname());
        viewHolder.llFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context,OtherTrendsListActivity.class);
                intent.putExtra("fan",new Gson().toJson(fans.get(position)));
                context.startActivity(intent);
            }
        });
        viewHolder.cancel.setOnClickListener(mListener);
        viewHolder.cancel.setTag(position);
//        final ViewHolder finalViewHolder = viewHolder;
//        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//                if (finalViewHolder.cancel.getText().equals("已关注")){
//                    finalViewHolder.cancel.setText("关注");
//                    finalViewHolder.cancel.setTextColor(Color.BLUE);
//                }else{
//                    finalViewHolder.cancel.setText("已关注");
//                    finalViewHolder.cancel.setTextColor(Color.BLACK);
//                }
//                notifyDataSetChanged();
//            }
//        });
        RequestOptions options = new RequestOptions().error(R.drawable.default_vatar);
        Glide.with(context).load(Constant.BASE_URL + "/upload/avatarimgs/" + fans.get(position).getImageUrl())
                .apply(options).into(viewHolder.ivUser);
        return convertView;
    }
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }
    public  class ViewHolder{
        public TextView tvFansName;
        public LinearLayout llFans;
        public ImageView ivUser;
        public  Button cancel;
    }
}
