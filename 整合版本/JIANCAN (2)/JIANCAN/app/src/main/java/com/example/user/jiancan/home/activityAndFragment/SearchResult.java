package com.example.user.jiancan.home.activityAndFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import static com.example.user.jiancan.home.activityAndFragment.FoodSearchResultFragment.filterFood;
import static com.example.user.jiancan.home.activityAndFragment.FoodSearchResultFragment.getRecyclerGridAdapter;
import static com.example.user.jiancan.home.activityAndFragment.HomeFragment.getHomeListItemBean;
import static com.example.user.jiancan.home.activityAndFragment.UserSearchResultFragment.filterUser;
import static com.example.user.jiancan.home.activityAndFragment.UserSearchResultFragment.getTUserList;
import static com.example.user.jiancan.home.activityAndFragment.UserSearchResultFragment.getUserAdapter;

import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.HomeListItemBean;
import com.example.user.jiancan.home.util.FragmentAdapter;
import com.example.user.jiancan.personal.entity.TUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author june
 */
public class SearchResult extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImgCancel;
    private SearchView mSearchView;
    private ViewPager mViewPager;
    private String[] tabTitles = new String[]{
            "食品" ,
            "用户"
    };

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {

        super.onCreate (savedInstanceState);
        setContentView (R.layout.search_result);
        initViews ();
        initTab ();
        monitorSearchView ();
    }

    /**
     * 注册组件
     * */
    private void initViews(){
        mSearchView = (SearchView) findViewById (R.id.search_sv);
        mImgCancel = (ImageView) findViewById (R.id.search_cancel);
        mImgCancel.setOnClickListener (this);
    }


    /**
     * 初始化TabLayout，viewpager
     * */
    private void initTab() {
        List <Fragment> fragments = new ArrayList <> ( );
        TabLayout tabLayout = findViewById (R.id.search_tab);
        mViewPager = findViewById (R.id.search_vp);
        mViewPager.setOffscreenPageLimit (0);
        for (String tab : tabTitles) {
            tabLayout.addTab (tabLayout.newTab ( ).setText (tab));
        }

        fragments.add (new FoodSearchResultFragment ( ));
        fragments.add (new UserSearchResultFragment ( ));
        FragmentAdapter fragmentAdapter = new FragmentAdapter (getSupportFragmentManager ( ) , fragments , tabTitles);
        mViewPager.setAdapter (fragmentAdapter);
        mViewPager.setOffscreenPageLimit (0);
        tabLayout.setupWithViewPager (mViewPager);
    }

    /**
     * 搜索框监听事件
     * */
    private void monitorSearchView(){
        mSearchView.setOnQueryTextListener (new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit( String query ) {
                return true;
            }

            @Override
            public boolean onQueryTextChange( String newText ) {
                //为0时显示食品的搜索信息
                if (mViewPager.getCurrentItem () == 0){
                    ArrayList<HomeListItemBean> homeListItemBeans = filterFood ((ArrayList<HomeListItemBean>) getHomeListItemBean (),newText);
                    getRecyclerGridAdapter ().showFoodSearchResult (homeListItemBeans);
                }
                //为1时显示用户的搜索信息
                if(mViewPager.getCurrentItem () == 1){
                    ArrayList<TUser> showUsers = filterUser (getTUserList (),newText);
                    getUserAdapter ().showUserSearchResult (showUsers);
                }

                return true;
            }
        });

    }

    @Override
    public void onClick( View v ) {
        switch (v.getId ()){
            case R.id.search_cancel:
                finish ();
                break;
            default:
                break;
        }
    }

}
