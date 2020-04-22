package com.example.user.jiancan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PersonalFansAdapter extends BaseAdapter {
    private List<User> fans ;
    private int item_id;
    private Context context;

    public PersonalFansAdapter(List<User> fans, int item_id, Context context) {
        this.fans = fans;
        this.item_id = item_id;
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
        PersonalFansAdapter.ViewHolder viewHolder = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_id, null);
            viewHolder = new PersonalFansAdapter.ViewHolder();
            viewHolder.tvFansName = convertView.findViewById(R.id.tv_item_name);
            viewHolder.llFans = convertView.findViewById(R.id.ll_followers);
            viewHolder.ivUser = convertView.findViewById(R.id.iv_user_pic);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (PersonalFansAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvFansName.setText(fans.get(position).getNickname());

        Glide.with(context).load(Constant.BASE_URL +"paperimg/"+ fans.get(position).getImageUrl()).into(viewHolder.ivUser);
        return convertView;
    }
    private class ViewHolder{
        public TextView tvFansName;
        public LinearLayout llFans;
        public ImageView ivUser;
    }
}
