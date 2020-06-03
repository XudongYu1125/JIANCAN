package com.example.user.jiancan.home.activityAndFragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.jiancan.R;
import com.example.user.jiancan.TabHostActivity;
import com.example.user.jiancan.home.entity.Food;
import com.example.user.jiancan.home.entity.HomeListItemBean;
import com.example.user.jiancan.home.entity.Image;
import com.example.user.jiancan.home.util.FormatNum;
import com.example.user.jiancan.home.util.RecyclerViewGridAdapter;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.GridLayout.VERTICAL;

/**
 * @author june
 */
public class FoodCategoryItemDetail extends AppCompatActivity {

    private TextView categoryTitle;
    private ImageView mImgBack;
    private SmartRefreshLayout smartRefreshLayout;
    private OkHttpClient okHttpClient;
    private List<Food> foodList;
    private User user;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    private RecyclerView recyclerView;
    private static ArrayList<HomeListItemBean> dateBeanArrayList;
    private RecyclerViewGridAdapter recyclerViewGridAdapter;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.food_list_category_item);
        initView ( );
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        loadTrueData ();
        categoryTitle.setText (getDataFromIntent ( ));
        refresh ( );
    }

    /**
     * 注册组件
     */
    private void initView() {
        okHttpClient = new OkHttpClient();
        mImgBack = (ImageView) findViewById (R.id.category_back);
        mImgBack.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick( View v ) {
                //finish ( );
                Intent intent = new Intent(FoodCategoryItemDetail.this, TabHostActivity.class);
                intent.putExtra("toIntent","HomeFragment");
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById (R.id.food_category_recyclerview_list);
        categoryTitle = (TextView) findViewById (R.id.category_title);
        smartRefreshLayout = (SmartRefreshLayout) findViewById (R.id.category_smartRefreshLayout);
        //刷新开启
        smartRefreshLayout.setEnableRefresh (true);
        //加载开启
        smartRefreshLayout.setEnableLoadmore (true);
    }


    /**
     * 接受intent数据
     *
     * @return String 接收到点击的食物类别并返回
     */
    private String getDataFromIntent() {
        return getIntent ( ).getStringExtra ("分类信息");
    }

    /**
     * 对接收的分类信息转换为id
     */
    private int getFoodCategoryId( String category ) {
        category = getDataFromIntent ( );
        switch (category) {
            case "便当":
                return 1;
            case "卤味":
                return 2;
            case "面食":
                return 3;
            case "汤品":
                return 4;
            case "甜点":
                return 5;
            case "冷饮":
                return 6;
            case "凉菜":
                return 7;
            case "热菜":
                return 8;
            default:
                return 0;
        }
    }

    /**
     * 上拉加载下拉刷新
     */
    public void refresh() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                recyclerViewGridAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                recyclerViewGridAdapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore();
            }
        });


    }

    /**
     * 数据库数据调用
     */
    private ArrayList<HomeListItemBean> getDataFromRequest() {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
        //Request request = new Request.Builder().url(Constant.URL_ALL_FOOD).post(body).build();
        int id = getFoodCategoryId(getDataFromIntent());
        Request request = new Request.Builder().url("http://39.97.3.111:8080/JianCanServerSystem/food/getByVegetables/"+id).post(body).build();
        Call call = okHttpClient.newCall(request);
        final ArrayList<HomeListItemBean> dateBeanList = new ArrayList<>();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1", e.getMessage());
            }

            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String dishListStr = response.body().string();
                List<Food> foodList1 = new ArrayList<>();
                Log.e("111", dishListStr);
                try {
                    JSONObject jsonObject = new JSONObject(dishListStr);
                    JSONArray array = (JSONArray) jsonObject.get("list");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Food food = new Food();
                        food.setFoodName(object.getString("foodName"));
                        food.setContent(object.getString("content"));
                        food.setDownload(object.getInt("download"));
                        food.setFabulous(object.getInt("fabulous"));
                        food.setId(object.getInt("id"));
                        food.setTitle(object.getString("title"));
                        food.setUploadDate(object.getString("uploadDate"));
                        food.setUserId(object.getInt("userId"));
                        food.setVegetablesId(object.getInt("vegetablesId"));
                        food.setType(object.getInt("type"));
                        switch (food.getType()) {
                            case 1:
                                food.setVideo(object.getString("video"));
                                break;
                            case 0:
                                JSONArray imageArray = new JSONArray(object.getString("images"));
                                Set<Image> imageSet = new HashSet<>();
                                for (int j = 0; j < imageArray.length(); j++) {
                                    JSONObject imageObject = imageArray.getJSONObject(j);
                                    Image image = new Image();
                                    image.setId(imageObject.getInt("id"));
                                    image.setImageName(imageObject.getString("imageName"));
                                    imageSet.add(image);
                                }
                                food.setImages(imageSet);
                                break;
                        }
                        foodList1.add(food);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (Food food : foodList1) {
                    HomeListItemBean homeBean = new HomeListItemBean();
                    switch (food.getType()){
                        case 1:
                            homeBean.setVideoUrl(food.getVideo());
                            //截取视频第一帧作为展示图 实参：直接通过url访问云服务器的视频
                            Log.e("homeBean.getVideoUrl()",homeBean.getVideoUrl());
                            Bitmap bitmap = createVideoThumbnail("http://39.97.3.111:8080/JianCanServerSystem/upload/videos/" + homeBean.getVideoUrl());
                            String showImagePath = saveBitmap( bitmap,food);
                            homeBean.setShowImg(showImagePath);
                            break;
                        case 0:
                            Set<Image> images = food.getImages();
                            List<Image> imageList = new ArrayList<Image>(images);//B是set型的
                            //选用Image列表第一张图片作为展示图片
                            //直接通过url访问云服务器的图片
                            homeBean.setShowImg("http://39.97.3.111:8080/JianCanServerSystem/upload/images/" + imageList.get(0).getImageName());
                            break;
                    }
                    homeBean.setFoodId(food.getId());
                    homeBean.setUserId(food.getUserId());
                    homeBean.setName(food.getFoodName());
                    homeBean.setLikeNum(FormatNum.formatBigNum(String.valueOf(food.getFabulous())));
                    homeBean.setTitle(food.getTitle());
                    homeBean.setContent(food.getContent());
                    getUser(homeBean);

                    FoodCategoryItemDetail.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(recyclerViewGridAdapter);
                        }
                    });
                }
            }
        });
        return dateBeanList;
    }

    /**
     * 获取视频某一帧图片作为预览图
     */
    public static Bitmap createVideoThumbnail(String filePath)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try
        {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://"))
            {
                retriever.setDataSource(filePath, new Hashtable<String, String>());
            }
            else
            {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(1*1000*500, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (IllegalArgumentException ex)
        {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        }
        catch (RuntimeException ex)
        {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                retriever.release();
            }
            catch (RuntimeException ex)
            {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap == null)
        {
            return null;
        }
        return bitmap;
    }

    /**
     * 保存bitmap到本地
     * @param mBitmap
     * @return
     */

    public  static String saveBitmap(Bitmap mBitmap, Food food) {
        String bitName = "IMG_"+food.getUserId()+"_"+food.getVideo() ;

        File f = new File("/sdcard/Pics/" + bitName + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            Log.e("在保存图片时出错",e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 30, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    /**
     * 真实数据设置adapter
     */
    private void loadTrueData() {
        dateBeanArrayList = getDataFromRequest();
        //创建适配器adapter对象 参数1.上下文 2.数据加载集合
        recyclerViewGridAdapter = new RecyclerViewGridAdapter(this, dateBeanArrayList);
        //设置适配器
        //recyclerView.setAdapter(recyclerViewGridAdapter);

        //布局管理器对象 参数1.上下文 2.规定显示的行数
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2) {
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
     * 查询用户信息
     */
    private List<HomeListItemBean> getUser(final HomeListItemBean homeListItemBean) {
        //RequestBody body = RequestBody.create (MediaType.parse ("text/plain"),"1");
        final List<HomeListItemBean> homeListItemBeanList = new ArrayList<>();
        //final Request request = new Request.Builder ().url (Constant.URL_USER_INFO+id).post (body).build ();
        Request request = new Request.Builder().url("http://39.97.3.111:8080/JianCanServerSystem/user/getu/" + homeListItemBean.getUserId()).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userJsonStr = response.body().string();
                Log.e("user", userJsonStr);
                //Type type = new TypeToken<User> (){}.getType ();
                user = new Gson().fromJson(userJsonStr, User.class);
                homeListItemBean.setUser(user.getNickname());

                dateBeanArrayList.add(homeListItemBean);

            }
        });
        return homeListItemBeanList;
    }

    /**
     * 对外提供的方法
     */
    public static ArrayList<HomeListItemBean> getHomeListItemBean() {
        return dateBeanArrayList;
    }
}
