package com.example.user.jiancan.home.activityAndFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;

import com.example.user.jiancan.home.util.RecyclerViewUserGridAdapter;
import com.example.user.jiancan.personal.entity.TUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.GridLayout.VERTICAL;

/**
 * @author june
 */
public class UserSearchResultFragment extends Fragment {
    private View view;
    private RecyclerView mUserSearchResultRecyclerview;
    private SmartRefreshLayout mUserSmartRefreshLayout;
    private static ArrayList<TUser> dateBeanArrayList;
    private String imgUrl ;
    private static RecyclerViewUserGridAdapter recyclerViewGridAdapter;
    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState ) {
        view = inflater.inflate (R.layout.user_search_result , null);
        initView (view);
//        loadGridDate ();
        loadTrueData ();
        return view;
    }


    private void initView( View view ) {
        mUserSearchResultRecyclerview = (RecyclerView) view.findViewById (R.id.user_search_result_recyclerview);
        mUserSmartRefreshLayout = (SmartRefreshLayout) view.findViewById (R.id.user_smartRefreshLayout);
        mUserSmartRefreshLayout.setEnableRefresh (true);
        mUserSmartRefreshLayout.setEnableLoadMore (true);
    }

    /**
     * 测试数据
     * */
    private void loadGridDate() {
        //集合对象
        dateBeanArrayList = new ArrayList <> ();
        //给Bean类放数据，把装好数据的Bean类放到集合里
        for (int i = 0; i < 5; i++) {
            //创建Bean类对象
            TUser dateBean = new TUser ();
            //给bean类对象添加图片和信息
            dateBean.setNickname ("憨憨用户"+i);
            dateBean.setImageUrl (imgUrl);
            //把Bean类放入集合
            dateBeanArrayList.add(dateBean);
        }
        for (int i = 0; i < 5; i++) {
            //创建Bean类对象
            TUser dateBean = new TUser ();
            //给benu类对象添加图片和信息
            dateBean.setNickname ("笨逼用户"+i);
            dateBean.setImageUrl (imgUrl);
            //把Bean类放入集合
            dateBeanArrayList.add(dateBean);
        }
        //创建适配器adapter对象 参数1.上下文 2.数据加载集合
        recyclerViewGridAdapter = new RecyclerViewUserGridAdapter (getContext(), dateBeanArrayList);
        //设置适配器
        mUserSearchResultRecyclerview.setAdapter(recyclerViewGridAdapter);

        //布局管理器对象 参数1.上下文 2.规定显示的行数
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                // 直接禁止垂直滑动
                return false;
            }
        };
        //通过布局管理器可以控制条目排列的顺序
        gridLayoutManager.setReverseLayout(false);
        //设置RecycleView显示的方向
        gridLayoutManager.setOrientation(VERTICAL);
        //设置布局管理器， 参数linearLayoutManager对象
        mUserSearchResultRecyclerview.setLayoutManager(gridLayoutManager);
    }



    /**
     * 搜索查询方法
     * */
    public static ArrayList<TUser> filterUser(ArrayList<TUser> TUsers,String query){
        query = query.toLowerCase ( );
        final ArrayList <TUser> filterUserModeList = new ArrayList <> ();
        for (TUser TUserBean : TUsers){

            String foodName = TUserBean.getNickname ().toLowerCase ();
            if (foodName.contains (query)){

                filterUserModeList.add (TUserBean);

            }


        }

        return filterUserModeList;
    }

    public static RecyclerViewUserGridAdapter getUserAdapter(){return recyclerViewGridAdapter;}
    public static ArrayList<TUser> getTUserList(){
        return dateBeanArrayList;
    }

    private void loadTrueData(){
        dateBeanArrayList = getAllUsers ();
        //创建适配器adapter对象 参数1.上下文 2.数据加载集合
        recyclerViewGridAdapter = new RecyclerViewUserGridAdapter (getContext(), dateBeanArrayList);
        //设置适配器
        mUserSearchResultRecyclerview.setAdapter(recyclerViewGridAdapter);

        //布局管理器对象 参数1.上下文 2.规定显示的行数
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                // 直接禁止垂直滑动
                return false;
            }
        };
        //通过布局管理器可以控制条目排列的顺序
        gridLayoutManager.setReverseLayout(false);
        //设置RecycleView显示的方向
        gridLayoutManager.setOrientation(VERTICAL);
        //设置布局管理器， 参数linearLayoutManager对象
        mUserSearchResultRecyclerview.setLayoutManager(gridLayoutManager);

    }

    private ArrayList<TUser> getAllUsers(){
        final ArrayList<TUser> users = new ArrayList <> ();
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
                users.addAll (userList);
                Log.e ("response",userList.toString () );
            }
        });
        Log.e ("user",users.toString ());
        return users;
    }
}
