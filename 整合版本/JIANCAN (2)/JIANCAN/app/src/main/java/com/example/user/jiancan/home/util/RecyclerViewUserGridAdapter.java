package com.example.user.jiancan.home.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.activityAndFragment.UserInfo;
import com.example.user.jiancan.personal.entity.TUser;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

/**
 * @author june
 */
public class RecyclerViewUserGridAdapter extends RecyclerView.Adapter <RecyclerViewUserGridAdapter.GridViewHolder> {

    private List<TUser> users;
    private static Context mContext;

    public RecyclerViewUserGridAdapter(Context context,List<TUser> userList){
        mContext = context;
        this.users = userList;
    }


    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup , int i ) {
        //转换一个ViewHolder对象，决定了item的样式，参数1.上下文 2.XML布局资源 3.null
        View itemView = View.inflate (mContext , R.layout.show_user_item , null);
        //创建一个ViewHolder对象
        //把ViewHolder传出去
        return new GridViewHolder (itemView);
    }

    @Override
    public void onBindViewHolder( @NonNull GridViewHolder gridViewHolder , final int i ) {
        final TUser tUser = users.get (i);
        gridViewHolder.setData (tUser);
        gridViewHolder.itemView.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick( View v ) {
                Toast.makeText(mContext, "点击的第"+i+"个", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (mContext, UserInfo.class);
                String jsonStr = new Gson ().toJson (tUser);
                intent.putExtra ("用户信息",jsonStr);
                mContext.startActivity (intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (users != null && users.size ( ) > 0) {
            return users.size ( );
        }
        return 0;
    }


    public static class GridViewHolder extends RecyclerView.ViewHolder {
        /**
         * 1.头像
         * 2.用户名
         * 3.粉丝数
         */
        private ImageView userPic;
        private TextView userName;


        public GridViewHolder( @NonNull View itemView ) {
            super (itemView);
            userPic = itemView.findViewById (R.id.item_list_icon);
            userName = itemView.findViewById (R.id.item_list_name);

        }

        public void setData( TUser data ) {
            Glide.with (mContext)
                    .load (data.getImageUrl ())
                    .into (userPic);
            userName.setText (data.getNickname ());

        }
    }

    /**
     * 搜索结果展示方法
     */
    public void showUserSearchResult( ArrayList <TUser> tUsers ) {
        users = new ArrayList <> ( );
        users.addAll (tUsers);
        notifyDataSetChanged ( );
    }

}
