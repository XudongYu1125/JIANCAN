package com.example.user.jiancan.personal.activityAndFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.jiancan.Constant;
import com.example.user.jiancan.personal.util.PersonalHistoryAdapter;
import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.entity.Food;
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

public class PersonalHistroyListActivity extends AppCompatActivity {
    //https://blog.csdn.net/ymszzu/article/details/81240040
    private ListView lvFoods;
    private LinearLayout mLlEditBar;//控制下方那一行的显示与隐藏
    private PersonalHistoryAdapter adapter;
    private List<Food> foods = new ArrayList<>();//将所有数据放进去
    private List<Food> mCheckedData = new ArrayList<>();//将选中数据放入里面
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中
    private boolean isSelectedAll = true;//用来控制点击全选，全选和全不选相互切换
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_trends);
        Food food = new Food();
        food.setName("蛋挞");
        foods.add(food);
        findViews();
    }
    private class onClicked implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_personal_cancel:
                    cancel();
                    break;
                case R.id.ll_personal_select_all:
                    selectAll();
                    break;
                case R.id.ll_personal_delete:
                    delete();
                    break;
            }
        }}
    private void delete() {
        if (mCheckedData.size() == 0) {
            Toast.makeText(PersonalHistroyListActivity.this, "您还没有选中任何数据！", Toast.LENGTH_SHORT).show();
            return;
        }
//        final CustomDialog dialog = new CustomDialog(this);
//        dialog.show();
//        dialog.setHintText("是否删除？");
//        dialog.setLeftButton("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.setRightButton("确定", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                beSureDelete();
//                dialog.dismiss();
//            }
//        });
    }

    private void selectAll() {
        mCheckedData.clear();//清空之前选中数据
        if (isSelectedAll) {
            setStateCheckedMap(true);//将CheckBox的所有选中状态变成选中
            isSelectedAll = false;
            mCheckedData.addAll(foods);//把所有的数据添加到选中列表中
        } else {
            setStateCheckedMap(false);//将CheckBox的所有选中状态变成未选中
            isSelectedAll = true;
        }
        adapter.notifyDataSetChanged();
    }


    private void cancel() {
        setStateCheckedMap(false);//将CheckBox的所有选中状态变成未选中
        mLlEditBar.setVisibility(View.GONE);//隐藏下方布局
        adapter.setShowCheckBox(false);//让CheckBox那个方框隐藏
        adapter.notifyDataSetChanged();//更新ListView
    }
    private void findViews() {
        lvFoods = findViewById(R.id.lv_trends);
        adapter = new PersonalHistoryAdapter(foods, R.layout.trends_listview_item, PersonalHistroyListActivity.this);
        lvFoods.setAdapter(adapter);
    }
    private void requestData() {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),"1");
        Request request = new Request.Builder().url( Constant.URL_COLLECTION).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("error",e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String foodListStr = response.body().string();
                Type type = new TypeToken<List<Food>>(){}.getType();
                foods.addAll((List<Food>) new Gson().fromJson(foodListStr,type));
                Log.e("fans",foods.toString());
            }
        });
    }

    /**
     * 设置所有CheckBox的选中状态
     * */
    private void setStateCheckedMap(boolean isSelectedAll) {
        for (int i = 0; i < foods.size(); i++) {
            stateCheckedMap.put(i, isSelectedAll);
            lvFoods.setItemChecked(i, isSelectedAll);
        }
    }
}
