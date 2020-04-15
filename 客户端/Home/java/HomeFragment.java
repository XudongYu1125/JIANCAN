package com.example.user.jiancan;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.GridLayout.VERTICAL;

public class HomeFragment extends Fragment {

    //搜索框
    private SearchView searchView;
    //发布
    private Button publish;
    //轮播
    private Banner banner;
    //Classification
    private GridView gridView;
    private List<Map<String, Object>> dataClassificationList;
    private SimpleAdapter adapter;

    //GridView
    private RecyclerView recyclerView;
    private ArrayList<HomeListItemBean> dateBeanArrayList;
    private RecyclerViewGridAdapter recyclerViewGridAdapter;

    //SmartRefreshLayout
    private SmartRefreshLayout smartRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(view);
        toPublish();
        initBanner();
        bannerOnClick(banner);
        initClassificationData();
        setClassification();
        loadGridDate();
        refresh();
        return view;
    }

    public void findViews(View view) {
        searchView = view.findViewById(R.id.home_searchView);
        publish = view.findViewById(R.id.btn_home_publish);
        banner = view.findViewById(R.id.home_banner);
        gridView = view.findViewById(R.id.home_classification);
        recyclerView = view.findViewById(R.id.home_recycler_view);
        smartRefreshLayout = view.findViewById(R.id.home_smartRefreshLayout);
        smartRefreshLayout.setEnableRefresh(true);//启用刷新
        smartRefreshLayout.setEnableLoadmore(true);//启用加载
    }

    /**
     * 发布
     */
    public void toPublish(){
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPublish = new Intent(getActivity(),PublishUpdatesActivity.class);
                startActivity(toPublish);
            }
        });
    }
    /**
     * 设置banner的各种属性
     */
    public void initBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .setImages(getBannerImages()) //从Presenter中取出图片资源
                .setBannerAnimation(Transformer.Default)
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }

    /**
     * 获取Banner的图片资源
     * 测试数据
     *
     * @return
     */
    public List<Integer> getBannerImages() {
        List<Integer> mBannerImages = new ArrayList<>();
        mBannerImages.add(R.mipmap.banner7);
        mBannerImages.add(R.mipmap.banner8);
        mBannerImages.add(R.mipmap.banner9);
        return mBannerImages;
    }

    /**
     * Banner点击事件
     */
    //banner点击事件
    public void bannerOnClick(final Banner banner) {
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                //在数据库中有对应banner的position 根据position得到链接页
                Log.e("position", "你点了第" + position + "张轮播图");
            }
        });
    }

    /**
     * 初始化Classification数据
     */
    public void initClassificationData() {
        //图标
        int icon[] = {R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4,
                R.drawable.i5, R.drawable.i6, R.drawable.i7, R.drawable.i8};
        //图标下的文字
        String name[] = {"便当", "卤味", "面食", "汤品", "甜点", "冷饮", "凉菜", "热菜"};
        dataClassificationList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            map.put("text", name[i]);
            dataClassificationList.add(map);
        }
    }

    /**
     * 通过适配器将数据添加到GridView控件中
     */
    public void setClassification() {
        String[] from = {"img", "text"};
        int[] to = {R.id.home_classification_img, R.id.home_classification_text};

        adapter = new SimpleAdapter(this.getContext(), dataClassificationList, R.layout.home_classification_item, from, to);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Log.e("text", dataClassificationList.get(arg2).get("text").toString());
            }
        });
    }

    /**
     * 列表测试数据
     */
    private void loadGridDate() {
        //集合对象
        dateBeanArrayList = new ArrayList<>();
        //给Bean类放数据，把装好数据的Bean类放到集合里
        for (int i = 0; i < 10; i++) {
            //创建Bean类对象
            HomeListItemBean dateBean = new HomeListItemBean();
            //给benu类对象添加图片和信息
            dateBean.showImg = R.drawable.i5;
            dateBean.name = "蛋糕" + i;
            dateBean.user = "于旭东";
            dateBean.likeNum = String.valueOf(i);
            //把Bean类放入集合
            dateBeanArrayList.add(dateBean);
        }
        //创建适配器adapter对象 参数1.上下文 2.数据加载集合
        recyclerViewGridAdapter = new RecyclerViewGridAdapter(this.getContext(), dateBeanArrayList);
        //设置适配器
        recyclerView.setAdapter(recyclerViewGridAdapter);

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
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * 上拉加载下拉刷新
     */
    public void refresh() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                dateBeanArrayList.clear();
                recyclerViewGridAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                for (int i = 0; i < 10; i++) {
                    //创建Bean类对象
                    HomeListItemBean dateBean = new HomeListItemBean();
                    //给benu类对象添加图片和信息
                    dateBean.showImg = R.mipmap.ic_launcher;
                    dateBean.name = "红烧排骨" + i;
                    dateBean.user = "于旭东";
                    dateBean.likeNum = String.valueOf(i);
                    //把Bean类放入集合
                    dateBeanArrayList.add(dateBean);
                }
                //设置适配器
                recyclerView.setAdapter(recyclerViewGridAdapter);
                recyclerViewGridAdapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore();
            }
        });

    }
}
