package com.example.test12;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author june
 */
public class GridFragment extends Fragment{


    private RecyclerView mRecycleViewColor;
    private LinearLayoutManager mManagerColor;
    private List <String> mData;
    private MyRecyclerViewAdapter mRecycleViewAdapter;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_layout, container, false);

        mRecycleViewColor = (RecyclerView) view.findViewById(R.id.recycleview_color);

        //设置颜色分割线
        mManagerColor = new GridLayoutManager (getActivity(), 2);
        mRecycleViewColor.setLayoutManager(mManagerColor);
        mRecycleViewColor.addItemDecoration(new GridDivider(getActivity(), 20, this.getResources().getColor(R.color.colorAccent)));

        //初始化数据
        mData = new ArrayList <String> ();
        initData(mData);
        mRecycleViewAdapter = new MyRecyclerViewAdapter (getActivity(), R.layout.grid_item, mData);

        mRecycleViewColor.setAdapter(mRecycleViewAdapter);


        return view;
    }

    private void initData(List<String> dataList) {
        for (int i = 0; i < 100; i++) {
            dataList.add("item" + i);
        }
    }


}