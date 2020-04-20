package com.example.user.jiancan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class PersonalFollowAdapter extends BaseAdapter {
    private List<User> followers ;
    private int item_id;
    private Context context;

    public PersonalFollowAdapter(List<User> followers, int item_id, Context context) {
        this.followers = followers;
        this.item_id = item_id;
        this.context = context;
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
        PersonalFollowAdapter.ViewHolder viewHolder = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_id, null);
            viewHolder = new PersonalFollowAdapter.ViewHolder();
            viewHolder.tvFollowersName = convertView.findViewById(R.id.tv_item_name);
            viewHolder.llFollowers = convertView.findViewById(R.id.ll_followers);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (PersonalFollowAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvFollowersName.setText(followers.get(position).getNickname());
        return convertView;
    }
    private class ViewHolder{
        public TextView tvFollowersName;
        public LinearLayout llFollowers;
    }
}
