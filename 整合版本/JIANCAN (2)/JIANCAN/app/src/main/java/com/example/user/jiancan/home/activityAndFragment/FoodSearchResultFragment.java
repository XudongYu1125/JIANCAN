package com.example.user.jiancan.home.activityAndFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.HomeListItemBean;
import com.example.user.jiancan.home.util.RecyclerViewGridAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import static android.widget.GridLayout.VERTICAL;

/**
 * @author june
 */
public class FoodSearchResultFragment extends Fragment {

    private View view;
    private SmartRefreshLayout mFoodSmartRefreshLayout;
    private static RecyclerView mFoodSearchResultRecyclerview;
    private ArrayList<HomeListItemBean> dateBeanArrayList;
    private static RecyclerViewGridAdapter recyclerViewGridAdapter;
    private String imgUrl = "http://img1.imgtn.bdimg.com/it/u=1141259048,554497535&fm=26&gp=0.jpg";
    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState ) {
        view = inflater.inflate (R.layout.food_search_result , null);
        initView (view);
        loadGridDate ();
        return view;
    }

    /**
     * 注册组件
     * */
    private void initView( View view ) {
        mFoodSearchResultRecyclerview = (RecyclerView) view.findViewById (R.id.food_search_result_recyclerview);
        mFoodSmartRefreshLayout = (SmartRefreshLayout) view.findViewById (R.id.food_smartRefreshLayout);
        mFoodSmartRefreshLayout.setEnableRefresh (true);
        mFoodSmartRefreshLayout.setEnableLoadMore (true);
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
            HomeListItemBean dateBean = new HomeListItemBean();
            //给bean类对象添加图片和信息
            dateBean.showImg = imgUrl;
            dateBean.name = "蛋糕" + i;
            dateBean.user = "简餐用户"+i;
            dateBean.likeNum = String.valueOf(i);
            //把Bean类放入集合
            dateBeanArrayList.add(dateBean);
        }
        for (int i = 0; i < 5; i++) {
            //创建Bean类对象
            HomeListItemBean dateBean = new HomeListItemBean();
            //给bean类对象添加图片和信息
            dateBean.showImg = imgUrl;
            dateBean.name = "菜" + i;
            dateBean.user = "用户"+i;
            dateBean.likeNum = String.valueOf(i);
            //把Bean类放入集合
            dateBeanArrayList.add(dateBean);
        }
        //创建适配器adapter对象 参数1.上下文 2.数据加载集合
        recyclerViewGridAdapter = new RecyclerViewGridAdapter (getContext(), dateBeanArrayList);
        //设置适配器
        mFoodSearchResultRecyclerview.setAdapter(recyclerViewGridAdapter);

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
        mFoodSearchResultRecyclerview.setLayoutManager(gridLayoutManager);
    }

    public static RecyclerView getFoodRecyclerView(){return mFoodSearchResultRecyclerview;}
    public static RecyclerViewGridAdapter getRecyclerGridAdapter(){ return recyclerViewGridAdapter; }
    /**
     * 搜索查询方法
     * */
    public static ArrayList<HomeListItemBean> filterFood(ArrayList<HomeListItemBean> homeListItemBeans,String query){
        query = query.toLowerCase ( );
        final ArrayList <HomeListItemBean> filterFoodModeList = new ArrayList <> ();
        for (HomeListItemBean homeListItemBean : homeListItemBeans){

            String foodName = homeListItemBean.getName ().toLowerCase ();
            if (foodName.contains (query)){

                filterFoodModeList.add (homeListItemBean);

            }


        }

        return filterFoodModeList;
    }



}
