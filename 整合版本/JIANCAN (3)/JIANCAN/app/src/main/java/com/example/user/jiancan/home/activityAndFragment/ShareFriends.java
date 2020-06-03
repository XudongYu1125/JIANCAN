package com.example.user.jiancan.home.activityAndFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.MyFriends;
import com.example.user.jiancan.home.entity.SharedItem;
import com.example.user.jiancan.home.util.MyRecyclerAdapter;
import com.example.user.jiancan.personal.entity.TUser;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author june
 */
public class ShareFriends extends AppCompatActivity {

    private SearchView mSearchFriend;
    private RecyclerView mRvFriends;
    private static TextView mFriendHint;
    private String logo = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1141259048,554497535&fm=26&gp=0.jpg";
    private List <MyFriends> myFriendsList = new ArrayList <> ( );
    private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences;
    private User loginUser;
    private Gson gson = new Gson ( );
    private MyRecyclerAdapter myRecyclerAdapter;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.friends_list);
//        sharedPreferences = getSharedPreferences ("loginInfo",MODE_PRIVATE);
//        loginUser = gson.fromJson (sharedPreferences.getString ("user",null),User.class);
        initView ( );
        initList ( );
        monitorSearchView ();
    }

    private void initView() {
        mSearchFriend = (SearchView) findViewById (R.id.search_friend);
        mRvFriends = (RecyclerView) findViewById (R.id.rv_friends);
        mFriendHint = (TextView) findViewById (R.id.friend_hint);
        int id = mSearchFriend.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView mHintText = (TextView) mSearchFriend.findViewById (id);
        mHintText.setTextSize (15);
    }

    /**
     * 测试方法，获得friends数据
     */
    private List <MyFriends> testData() {
        List <MyFriends> friends = new ArrayList <> ( );
        for (int i = 0; i < 10; i++) {
            MyFriends friend = new MyFriends ( );
            friend.setId (i*10);
            friend.setName ("好友" + i);
            friend.setUserLogo (logo);
            friends.add (friend);
        }
        return friends;
    }

    /**
     * 获取分享信息
     */
    private SharedItem getSharedItem() {
        Intent intent = getIntent ( );
        String jsonStr = intent.getStringExtra ("分享内容");
        SharedItem sharedItem = new Gson ( ).fromJson (jsonStr , SharedItem.class);
        return sharedItem;
    }

    /**
     * 初始化recyclerList列表
     */
    private void initList() {
        myFriendsList = getMyFriendsList ();
        Log.e ("ll",myFriendsList.toString ());
        SharedItem sharedItem = getSharedItem ( );
        myRecyclerAdapter = new MyRecyclerAdapter (ShareFriends.this , myFriendsList , sharedItem);
        LinearLayoutManager manager = new LinearLayoutManager (this);
        manager.setOrientation (LinearLayoutManager.VERTICAL);
        mRvFriends.setLayoutManager (manager);
        mRvFriends.setAdapter (myRecyclerAdapter);

    }


    /**
     * 设置hint显示
     */
    public static void setHintVisible() {
        mFriendHint.setVisibility (View.VISIBLE);
    }

    /**
     * 设置hint隐藏
     */
    public static void setHintGone() {
        mFriendHint.setVisibility (View.GONE);
    }

    /**
     * 搜索框监听事件
     */
    private void monitorSearchView() {
        mSearchFriend.setOnQueryTextListener (new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit( String query ) {
                return true;
            }

            @Override
            public boolean onQueryTextChange( String newText ) {
                List <MyFriends> searchFriends = filterFriends (myFriendsList,newText);
                myRecyclerAdapter.showQueryResult (searchFriends);
                return true;
            }
        });
    }

    /**
     * 搜索查询方法
     */
    public static ArrayList <MyFriends> filterFriends( List <MyFriends> friends , String query ) {
        query = query.toLowerCase ( );
        final ArrayList <MyFriends> filterModeList = new ArrayList <> ( );
        for (MyFriends friend : friends) {

            String name = friend.getName ( ).toLowerCase ( );
            int id = friend.getId ();
            if (name.contains (query) || String.valueOf (id).contains (query)) {
                filterModeList.add (friend);
            }
        }
        return filterModeList;
    }

    private List<TUser> getAllUsers(){
        final List<TUser> users = new ArrayList <> ();
        OkHttpClient okHttpClient = new OkHttpClient ();
        Request request = new Request.Builder ()
                .url (Constant.URL_GET_ALL_USER)
                .build ();
        okHttpClient.newCall (request).enqueue (new Callback ( ) {
            @Override
            public void onFailure( Call call , IOException e ) {
                System.out.println (e.getMessage () );
            }

            @Override
            public void onResponse( Call call , Response response ) throws IOException {
                String str = response.body ().string ();
                List<TUser> userList = new Gson ().fromJson (str,new TypeToken<List<TUser>> (){}.getType ());
                Log.e ("rr",userList.toString ());
                users.addAll (userList);
            }
        });
        return users;
    }

    private List<MyFriends> getMyFriendsList(){
        List<MyFriends> friendsList = new ArrayList <> ();
        List<TUser> users = getAllUsers ();
        Log.e ("uu",users.toString ());
        for (TUser u : users){
            MyFriends friends = new MyFriends ();
            friends.setId (u.getId ());
            friends.setName (u.getNickname ());
            friends.setUserLogo (u.getImageUrl ());
            friendsList.add (friends);
        }
        return friendsList;
    }

}
