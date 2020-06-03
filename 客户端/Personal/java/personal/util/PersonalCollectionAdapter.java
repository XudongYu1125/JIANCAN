package com.example.user.jiancan.personal.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.activityAndFragement.FoodItemListDetail;
import com.example.user.jiancan.personal.entity.Food;
import com.example.user.jiancan.personal.entity.HomeListItemBean;
import com.example.user.jiancan.personal.entity.Image;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PersonalCollectionAdapter extends BaseAdapter {
    private List<Food> foods ;
    private int item_id;
    private User user;
    private Context context;
    private boolean isShowCheckBox = false;//表示当前是否是多选状态。
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中
    ViewHolder viewHolder;

    public PersonalCollectionAdapter(List<Food> foods, User user, int item_id, Context context, SparseBooleanArray stateCheckedMap) {
        this.foods = foods;
        this.user = user;
        this.item_id = item_id;
        this.context = context;
        this.stateCheckedMap = stateCheckedMap;
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
            viewHolder = new PersonalCollectionAdapter.ViewHolder();
            viewHolder.tvFoodName = convertView.findViewById(R.id.tv_item_name);
            viewHolder.llTrends = convertView.findViewById(R.id.ll_trends);
            viewHolder.imageView = convertView.findViewById(R.id.iv_trends_pic);
            viewHolder.checkBox = convertView.findViewById(R.id.cb_select_point);
            viewHolder.tvAuthorName = convertView.findViewById(R.id.tv_item_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (PersonalCollectionAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.llTrends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeListItemBean homeListItemBean = new HomeListItemBean();
                homeListItemBean.setUserId(user.getId());
                homeListItemBean.setUser(user.getNickname());
                homeListItemBean.setContent(foods.get(position).getContent());
                homeListItemBean.setImages(foods.get(position).getImages());
                homeListItemBean.setFoodId(foods.get(position).getId());
                homeListItemBean.setLikeNum(foods.get(position).getFabulous()+"");
                homeListItemBean.setName(foods.get(position).getFoodName());
                homeListItemBean.setShowImg("");
                homeListItemBean.setTitle(foods.get(position).getTitle());
                homeListItemBean.setType(foods.get(position).getType());
                homeListItemBean.setVideoUrl(foods.get(position).getVideo());
                Intent intent = new Intent();
                intent.setClass(context,FoodItemListDetail.class);
                intent.putExtra("食物数据",new Gson().toJson(homeListItemBean));
                context.startActivity(intent);
            }
        });
        viewHolder.tvFoodName.setText(foods.get(position).getFoodName());
        viewHolder.tvAuthorName.setText(foods.get(position).getContent().substring(0, 10)+"...");
        showAndHideCheckBox();//控制CheckBox的那个的框显示与隐藏
        viewHolder.checkBox.setChecked(stateCheckedMap.get(position));//设置CheckBox是否选中
        Set<Image> image = foods.get(position).getImages();
        List <Image> list = new ArrayList<Image>(image);
//        Log.e("image",list.get(0).getImageName().toString());
        RequestOptions options = new RequestOptions().error(R.drawable.danta);
        if (list.size() == 0){
            Glide.with(context)
                    .load(R.drawable.danta)
                    .into(viewHolder.imageView);
        }else{
            Glide.with(context)
                    .load(Constant.BASE_URL + "/upload/images/" + list.get(0).getImageName().toString())
                    .apply(options)
                    .into(viewHolder.imageView);
        }
        return convertView;
    }
    public class ViewHolder{
        public TextView tvFoodName;
        public LinearLayout llTrends;
        public ImageView imageView;
        public TextView tvAuthorName;
        public AppCompatCheckBox checkBox;
    }
    private void showAndHideCheckBox() {
        if (isShowCheckBox()) {
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
