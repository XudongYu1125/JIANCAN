package com.example.user.jiancan.personal.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.activityAndFragment.PersonalCollectionListActivity;
import com.example.user.jiancan.personal.activityAndFragment.PersonalFollowListActivity;
import com.example.user.jiancan.personal.entity.User;

import java.util.List;

public class PersonalFollowAdapter extends BaseAdapter {
    private List<User> followers ;
    private int item_id;
    private Context context;
    ViewHolder viewHolder ;
    private MyClickListener mListener;

    public PersonalFollowAdapter(List<User> followers, int item_id, Context context,MyClickListener listener) {
        this.followers = followers;
        this.item_id = item_id;
        this.context = context;
        this.mListener = listener;
    }
    @Override
    public int getCount() {
        if (followers.size() != 0){
            return followers.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (followers.size()!=0){
            return followers.get(position);
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
            viewHolder.tvFollowersName = convertView.findViewById(R.id.tv_item_name);
            viewHolder.llFollowers = convertView.findViewById(R.id.ll_followers);
            viewHolder.ivUser = convertView.findViewById(R.id.iv_user_pic);
            viewHolder.cancel = convertView.findViewById(R.id.btn_unfollow);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvFollowersName.setText(followers.get(position).getNickname());
        viewHolder.cancel.setOnClickListener(mListener);
        viewHolder.cancel.setTag(position);
        //Glide.with(context).load(Constant.BASE_URL +"paperimg/"+ followers.get(position).getImageUrl()).into(viewHolder.ivUser);
        return convertView;
    }
    public class ViewHolder{
        public TextView tvFollowersName;
        public LinearLayout llFollowers;
        public ImageView ivUser;
        public Button cancel;
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
}
