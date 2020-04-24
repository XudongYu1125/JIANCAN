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

public class PersonalHistoryAdapter extends BaseAdapter {

    private List<Food> foods ;
    private int item_id;
    private Context context;

    public PersonalHistoryAdapter(List<Food> foods, int item_id, Context context) {
        this.foods = foods;
        this.item_id = item_id;
        this.context = context;
    }
    @Override
    public int getCount() {
        if (foods.size() != 0){
            return foods.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (foods.size()!=0){
            return foods.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PersonalHistoryAdapter.ViewHolder viewHolder = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_id, null);
            viewHolder = new PersonalHistoryAdapter.ViewHolder();
            viewHolder.tvFoodName = convertView.findViewById(R.id.tv_item_name);
            viewHolder.llTrends = convertView.findViewById(R.id.ll_trends);
            viewHolder.imageView = convertView.findViewById(R.id.iv_trends_pic);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (PersonalHistoryAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvFoodName.setText(foods.get(position).getName());
        //Glide.with(context).load(Constant.BASE_URL +"paperimg/"+ foods.get(position).getImageUrl()).into(viewHolder.imageView);
        return convertView;
    }
    private class ViewHolder{
        public TextView tvFoodName;
        public LinearLayout llTrends;
        public ImageView imageView;
    }
}
