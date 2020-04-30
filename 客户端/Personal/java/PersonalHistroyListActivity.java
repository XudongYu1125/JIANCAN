package com.example.user.jiancan.personal.activityAndFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
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
        setOnListViewItemClickListener();
        setOnListViewItemLongClickListener();

    }
    private class onClicked implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_personal_cancel:
                    //取消删除
                    cancel();
                    break;
                case R.id.ll_personal_select_all:
                    //全选
                    selectAll();
                    break;
                case R.id.ll_personal_delete:
                    //删除所选项
                    delete();
                    break;
            }
        }}
    private void delete() {
        if (mCheckedData.size() == 0) {
            Toast.makeText(PersonalHistroyListActivity.this, "您还没有选中任何数据！", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder adBulider=new AlertDialog.Builder(PersonalHistroyListActivity.this);
        //对构造器进行设置
        adBulider.setTitle("提示");
        adBulider.setMessage("您确定要删除您所选中的内容吗？");
        adBulider.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        beSureDelete();
                        dialog.dismiss();
                    }
                });
        adBulider.setNegativeButton("取消",null);
        AlertDialog alertDialog = adBulider.create();
        //设置对话框不能被取消（点击页面其他地方，对话框自动关闭）
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    private void beSureDelete() {
        foods.removeAll(mCheckedData);//删除选中数据
        setStateCheckedMap(false);//将CheckBox的所有选中状态变成未选中
        mCheckedData.clear();//清空选中数据
        adapter.notifyDataSetChanged();
        Toast.makeText(PersonalHistroyListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
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
    };


    private void cancel() {
        setStateCheckedMap(false);//将CheckBox的所有选中状态变成未选中
        mLlEditBar.setVisibility(View.GONE);//隐藏下方布局
        adapter.setShowCheckBox(false);//让CheckBox那个方框隐藏
        adapter.notifyDataSetChanged();//更新ListView
    }

    private void setOnListViewItemClickListener() {
        lvFoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateCheckBoxStatus(view, position);
            }
        });
    }

    /**
     * 如果返回false那么click仍然会被调用,,先调用Long click，然后调用click。
     * 如果返回true那么click就会被吃掉，click就不会再被调用了
     * 在这里click即setOnItemClickListener
     */
    private void setOnListViewItemLongClickListener() {
        lvFoods.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mLlEditBar.setVisibility(View.VISIBLE);//显示下方布局
                adapter.setShowCheckBox(true);//CheckBox的那个方框显示
                updateCheckBoxStatus(view, position);
                return true;
            }
        });
    }

    private void updateCheckBoxStatus(View view, int position) {
        PersonalHistoryAdapter.ViewHolder holder = (PersonalHistoryAdapter.ViewHolder) view.getTag();
        holder.checkBox.toggle();//反转CheckBox的选中状态
        lvFoods.setItemChecked(position, holder.checkBox.isChecked());//长按ListView时选中按的那一项
        stateCheckedMap.put(position, holder.checkBox.isChecked());//存放CheckBox的选中状态
        if (holder.checkBox.isChecked()) {
            mCheckedData.add(foods.get(position));//CheckBox选中时，把这一项的数据加到选中数据列表
        } else {
            mCheckedData.remove(foods.get(position));//CheckBox未选中时，把这一项的数据从选中数据列表移除
        }
        adapter.notifyDataSetChanged();
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
