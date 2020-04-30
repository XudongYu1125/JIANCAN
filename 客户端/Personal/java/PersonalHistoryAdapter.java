package com.example.user.jiancan.personal.util;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.entity.Food;

import java.util.List;

public class PersonalHistoryAdapter extends BaseAdapter {

    private List<Food> foods ;
    private int item_id;
    private Context context;
    private boolean isShowCheckBox = false;//表示当前是否是多选状态。
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中
    ViewHolder viewHolder;

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
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_id, null);
            viewHolder = new PersonalHistoryAdapter.ViewHolder();
            viewHolder.tvFoodName = convertView.findViewById(R.id.tv_item_name);
            viewHolder.llTrends = convertView.findViewById(R.id.ll_trends);
            viewHolder.imageView = convertView.findViewById(R.id.iv_trends_pic);
            viewHolder.checkBox = convertView.findViewById(R.id.cb_select_point);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (PersonalHistoryAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvFoodName.setText(foods.get(position).getName());
        showAndHideCheckBox();//控制CheckBox的那个的框显示与隐藏
        viewHolder.checkBox.setChecked(stateCheckedMap.get(position));//设置CheckBox是否选中
        //Glide.with(context).load(Constant.BASE_URL +"paperimg/"+ foods.get(position).getImageUrl()).into(viewHolder.imageView);
        return convertView;
    }
    public class ViewHolder{
        public TextView tvFoodName;
        public LinearLayout llTrends;
        public ImageView imageView;
        public AppCompatCheckBox checkBox;

    }
    private void showAndHideCheckBox() {
        if (isShowCheckBox) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
    }


    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
    }

}
