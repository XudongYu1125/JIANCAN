package com.example.user.jiancan.home.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.MyFriends;
import com.example.user.jiancan.home.entity.SharedItem;
import com.example.user.jiancan.message.activityAndFragment.ChatRecordActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.example.user.jiancan.home.activityAndFragment.ShareFriends.setHintVisible;
import static com.example.user.jiancan.home.activityAndFragment.ShareFriends.setHintGone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author june
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter <MyRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List <MyFriends> myFriendsList;
    private View inflater;
    private SharedItem sharedItem;

    public MyRecyclerAdapter( Context context , List <MyFriends> myFriendsList , SharedItem sharedItem ) {
        this.context = context;
        this.myFriendsList = myFriendsList;
        this.sharedItem = sharedItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup , int i ) {
        inflater = LayoutInflater.from (context).inflate (R.layout.item_share_friend , viewGroup , false);
        if (getItemCount ( ) == 0) {
            setHintVisible ( );
        } else {
            setHintGone ( );
        }
        return new MyViewHolder (inflater);
    }

    @Override
    public void onBindViewHolder( @NonNull MyRecyclerAdapter.MyViewHolder myViewHolder , final int i ) {

            myViewHolder.nameTextView.setText (myFriendsList.get (i).getName ( ));
            Glide.with (context)
                    .load ("http://39.97.3.111:8080/JianCanServerSystem/upload/avatarimgs/"+myFriendsList.get (i).getUserLogo ( ))
                    //.circleCrop ( )
                    .into (myViewHolder.logoImageView);
            myViewHolder.itemView.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick( View v ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder (context).setTitle ("分享?")
                            .setMessage ("是否分享给 "+myFriendsList.get (i).getName ()+" 吗？")
                            .setPositiveButton ("是" , new DialogInterface.OnClickListener ( ) {
                                @Override
                                public void onClick( DialogInterface dialog , int which ) {
                                    Intent intent = new Intent (context , ChatRecordActivity.class);
                                    Gson gson = new GsonBuilder ( ).create ( );
                                    String jsonStr = gson.toJson (sharedItem);
                                    intent.putExtra ("分享信息" , jsonStr);
                                    context.startActivity (intent);
                                }
                            })
                            .setNegativeButton ("否" , new DialogInterface.OnClickListener ( ) {
                                @Override
                                public void onClick( DialogInterface dialog , int which ) {
                                    dialog.dismiss ();
                                }
                            });

                }
            });
    }

    @Override
    public int getItemCount() {
        if (myFriendsList.size ( ) != 0) {
            return myFriendsList.size ( );
        } else {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView logoImageView;

        public MyViewHolder( View itemView ) {
            super (itemView);
            nameTextView = (TextView) itemView.findViewById (R.id.friend_name);
            logoImageView = (ImageView) itemView.findViewById (R.id.friend_logo);
        }
    }

    public void showQueryResult(List<MyFriends> friends){
        myFriendsList = new ArrayList <> ();
        myFriendsList.addAll (friends);
        notifyDataSetChanged ();
    }

}
