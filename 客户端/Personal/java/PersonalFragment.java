package com.example.user.jiancan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {
    private LinearLayout llMessage;
    private LinearLayout llTrends;
    private LinearLayout llFollow;
    private LinearLayout llFan;
    private LinearLayout llHistroy;
    private LinearLayout llCollection;
    private LinearLayout llDownload;
    private LinearLayout llSettings;
    private Intent intent;
    private ImageView imageView;
    private TextView textView;
    private User user;
    private SharedPreferences sharedPreferences ;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_personal,container,false );
        findViews(view);
        sharedPreferences = getActivity().getSharedPreferences("loginInfo", MODE_PRIVATE);
        user = new Gson().fromJson(sharedPreferences.getString("user", null), User.class);
        setOnclicked();String time = sharedPreferences.getString("time",null);
        if (time == null){
            RequestOptions options = new RequestOptions().error(R.drawable.default_vatar);
//            Glide.with(this)
//                    .load    .into(imageView);
        }else {
            RequestOptions options = new RequestOptions().signature(new ObjectKey(time)).error(R.drawable.default_vatar);
            Glide.with(this)
                    .load(Constant.BASE_URL + "avatarimg/" + user.getImageurl())
                    .apply(options)
                    .into(imageView);
        }
        textView.setText(user.getNickname().toString());
        return view;
    }

    private void findViews(View view) {
        imageView = view.findViewById(R.id.iv_person);
        textView = view.findViewById(R.id.tv_person_nickname);
        llMessage = view.findViewById(R.id.ll_personal_message);
        llTrends = view.findViewById(R.id.ll_trends);
        llFollow = view.findViewById(R.id.ll_follow);
        llFan = view.findViewById(R.id.ll_fan);
        llHistroy = view.findViewById(R.id.ll_histroy);
        llCollection = view.findViewById(R.id.ll_collection);
        llDownload = view.findViewById(R.id.ll_download);
        llSettings = view.findViewById(R.id.ll_settings);
    }
    private void setOnclicked() {
        llMessage.setOnClickListener(new onClicked());
        llTrends.setOnClickListener(new onClicked());
        llFollow.setOnClickListener(new onClicked());
        llFan.setOnClickListener(new onClicked());
        llHistroy.setOnClickListener(new onClicked());
        llCollection.setOnClickListener(new onClicked());
        llDownload.setOnClickListener(new onClicked());
        llSettings.setOnClickListener(new onClicked());
    }

    private class onClicked implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_personal_message:
                    //跳转到修改个人信息界面
                    intent = new Intent();
                    intent.setClass(getContext(),PersonalChangeMessageActivity.class);
                    startActivityForResult(intent,100);
                    break;
                case R.id.ll_trends:
                    //跳转到用户自己的发布的动态界面
                    break;
                case R.id.ll_follow:
                    //跳转到用户的关注列表界面
                    break;
                case R.id.ll_fan:
                    //跳转到用户的粉丝列表
                    break;
                case R.id.ll_histroy:
                    //跳转到用户的历史记录界面
                    break;
                case R.id.ll_collection:
                    //跳转到用户的收藏界面
                    break;
                case R.id.ll_download:
                    //跳转到用户的下载记录界面
                    break;
                case R.id.ll_settings:
                    //跳转到用户的设置界面
                    break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String time = sharedPreferences.getString("time",null);
        if (time == null){
            RequestOptions options = new RequestOptions().error(R.drawable.default_vatar);
            Glide.with(this)
                    .load(Constant.BASE_URL + "avatarimg/" + user.getImageurl())
                    .apply(options)
                    .into(imageView);
        }else {
            RequestOptions options = new RequestOptions().signature(new ObjectKey(time)).error(R.drawable.default_vatar);
            Glide.with(this)
                    .load(Constant.BASE_URL + "avatarimg/" + user.getImageurl())
                    .apply(options)
                    .into(imageView);
        }
    }
}
