package com.example.user.jiancan.home.activityAndFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.home.entity.Food;
import com.example.user.jiancan.home.entity.Image;
import com.example.user.jiancan.home.util.FormatNum;
import com.example.user.jiancan.home.util.GlideImageLoader;
import com.example.user.jiancan.home.entity.HomeListItemBean;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.util.RecyclerViewGridAdapter;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.GridLayout.VERTICAL;

public class HomeFragment extends Fragment {


    private ImageView searchImg;

    private Button publish;

    private Banner banner;
    private GridView gridView;
    private List<Map<String, Object>> dataClassificationList;
    private SimpleAdapter adapter;

    private RecyclerView recyclerView;
    private static ArrayList<HomeListItemBean> dateBeanArrayList;
    private RecyclerViewGridAdapter recyclerViewGridAdapter;

    private SmartRefreshLayout smartRefreshLayout;
    private OkHttpClient okHttpClient;
    private List<Food> foodList;
    private User user;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(view);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) this.getContext(), PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        toPublish();
        initBanner();
        bannerOnClick(banner);
        initClassificationData();
        setClassification();
//        loadGridDate();
        loadTrueData();
        //getDataFromRequest();
        refresh();
        searchContent();
        return view;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    public void findViews(View view) {
        searchImg = view.findViewById(R.id.search_img);
        publish = view.findViewById(R.id.btn_home_publish);
        banner = view.findViewById(R.id.home_banner);
        gridView = view.findViewById(R.id.home_classification);
        recyclerView = view.findViewById(R.id.home_recycler_view);
        smartRefreshLayout = view.findViewById(R.id.home_smartRefreshLayout);
        //启用刷新
        smartRefreshLayout.setEnableRefresh(true);
        //启用加载
        smartRefreshLayout.setEnableLoadMore(true);
        //
        okHttpClient = new OkHttpClient();

    }

    /**
     * 搜索点击事件
     */
    private void searchContent() {
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchResult.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 发布按钮点击事件
     */
    public void toPublish() {
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPublish = new Intent(getActivity(), HomePublishUpdatesActivity.class);
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
                //从Presenter中取出图片资源
                .setImages(getBannerImages())
                .setBannerAnimation(Transformer.Default)
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }

    /**
     * 获取Banner的图片资源
     */
    public List<Integer> getBannerImages() {
        List<Integer> mBannerImages = new ArrayList<>();
        mBannerImages.add(R.mipmap.banner0);
        mBannerImages.add(R.mipmap.banner1);
        mBannerImages.add(R.mipmap.banner2);
        return mBannerImages;

    }

    /**
     * Banner点击事件
     */
    public void bannerOnClick(final Banner banner) {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
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
                Toast.makeText(getContext(), dataClassificationList.get(arg2).get("text").toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), FoodCategoryItemDetail.class);
                intent.putExtra("分类信息", dataClassificationList.get(arg2).get("text").toString());
                startActivity(intent);
                //getActivity().onBackPressed();

            }
        });
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
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {


                recyclerViewGridAdapter.notifyDataSetChanged();
                refreshLayout.finishLoadMore();
            }
        });

    }

    /**
     * 数据库数据调用
     */
    private ArrayList<HomeListItemBean> getDataFromRequest() {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
        //Request request = new Request.Builder().url(Constant.URL_ALL_FOOD).post(body).build();
        Request request = new Request.Builder().url("http://39.97.3.111:8080/JianCanServerSystem/food/getAll").post(body).build();
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
                            homeBean.setImages(images);
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
                    homeBean.setType(food.getType());
                    getUser(homeBean);

                    getActivity().runOnUiThread(new Runnable() {
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

    public  static String saveBitmap( Bitmap mBitmap,Food food) {
        String bitName = "IMG_"+food.getUserId()+"_"+food.getVideo() ;

        File f = new File("/sdcard/Pictures/" + bitName + ".png");
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
        recyclerViewGridAdapter = new RecyclerViewGridAdapter(this.getContext(), dateBeanArrayList);
        //设置适配器
        //recyclerView.setAdapter(recyclerViewGridAdapter);

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
                homeListItemBean.setUserLogo(user.getImageUrl());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
