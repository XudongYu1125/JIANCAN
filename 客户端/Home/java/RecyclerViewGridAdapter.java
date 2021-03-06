package com.example.user.jiancan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewGridAdapter extends RecyclerView.Adapter<RecyclerViewGridAdapter.GridViewHolder>{

    private Context mContext;
    private List<HomeListItemBean> dateBean;

    public RecyclerViewGridAdapter(Context context, List<HomeListItemBean> datesBean){
        this.mContext = context;
        this.dateBean = datesBean;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //转换一个ViewHolder对象，决定了item的样式，参数1.上下文 2.XML布局资源 3.null
        View itemView = View.inflate(mContext, R.layout.grid_item, null);
        //创建一个ViewHodler对象
        GridViewHolder gridViewHolder = new GridViewHolder(itemView);
        //把ViewHolder传出去
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder gridViewHolder, int i) {
        //从集合里拿对应的item的数据对象
        HomeListItemBean listBean = dateBean.get(i);
        //给Holder里面的控件对象设置数据
        gridViewHolder.setData(listBean);

    }

    @Override
    public int getItemCount() {
        if (dateBean != null && dateBean.size() > 0) {
            return dateBean.size();
        }
        return 0;
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{
        /**
         * 1.显示图片
         * 2.简介
         * 3.用户名
         * 4.点赞数
         */
        private ImageView showImage;
        private TextView myName;
        private TextView myUser;
        private TextView myLikeNum;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            showImage = itemView.findViewById(R.id.item_list_icon);
            myName = itemView.findViewById(R.id.item_list_name);
            myUser = itemView.findViewById(R.id.item_list_user);
            myLikeNum = itemView.findViewById(R.id.item_list_num);
        }

        public void setData(HomeListItemBean data) {
            showImage.setImageResource(data.showImg);
            myName.setText(data.name);
            myUser.setText(data.user);
            myLikeNum.setText(data.likeNum);
        }
    }


}
