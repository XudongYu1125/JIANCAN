package com.example.user.jiancan.home.activityAndFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.TabHostActivity;
import com.example.user.jiancan.home.entity.Food;
import com.example.user.jiancan.personal.activityAndFragment.PersonalTrendsListActivity;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomePublishUpdatesActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView cancel;
    private Button addPic;
    private Dialog mCameraDialog;
    private Button sure;
    private EditText title;
    private EditText content;
    private ImageView pictuer0;
    private ImageView pictuer1;
    private ImageView pictuer2;

    public final int REQUEST_CAMERA_CODE_IMG = 1;
    public final int REQUEST_CAMERA_CODE_VIDEO = 2;
    //上传文件的类型
    private int type;
    //文件地址列表
    List<String> paths = new ArrayList<>();

    private OkHttpClient okHttpClient;
    private Message msg = Message.obtain();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_updates);
        //去除标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        okHttpClient = new OkHttpClient();

        init();


    }

    /**
     * 更新UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(HomePublishUpdatesActivity.this, "上传失败，网络有问题", Toast.LENGTH_LONG);
                    break;
                case 2:
                    Toast.makeText(HomePublishUpdatesActivity.this, "上传成功，即将前往个人动态", Toast.LENGTH_LONG);
                    Intent intent = new Intent(HomePublishUpdatesActivity.this, PersonalTrendsListActivity.class);
                    intent.putExtra("activityFrom","HomePublishUpdatesActivity");
                    startActivity(intent);
                    break;
                case 3:
                    Toast.makeText(HomePublishUpdatesActivity.this, "上传失败", Toast.LENGTH_LONG);
                    break;
            }
        }
    };

    /**
     * 初始化控件
     */
    public void init() {
        cancel = findViewById(R.id.img_cancel);
        addPic = findViewById(R.id.btn_publish_add);
        sure = findViewById(R.id.btn_publish_sure);
        title = findViewById(R.id.et_publish_title);
        content = findViewById(R.id.et_publish_content);
        pictuer0 = findViewById(R.id.img_publish_pic0);
        pictuer1 = findViewById(R.id.img_publish_pic1);
        pictuer2 = findViewById(R.id.img_publish_pic2);

        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);

        addPic.setOnClickListener(this);
        cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    /**
     * 底部弹出菜单
     */
    private void setDialog() {
        //设置弹出动画
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        //设置弹出样式
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.publish_bottom_dialog, null);
        mCameraDialog.setContentView(root);
        //设置点击事件
        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        root.findViewById(R.id.btn_choose_video).setOnClickListener(this);
        //设置弹出位置
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_publish_add:
                //弹出对话框
                setDialog();
                break;
            case R.id.btn_choose_img:
                //选择照片
                openPics();
                destroyDialog();
                break;
            case R.id.btn_choose_video:
                //选择视频
                openVideos();
                destroyDialog();
                break;
            case R.id.img_cancel:
                //取消
                Intent back = new Intent(HomePublishUpdatesActivity.this, TabHostActivity.class);
                startActivity(back);
                break;
            case R.id.btn_publish_sure:
                //上传
                String url = (Constant.HOME_BASE_URL+"uploadVideo");
                uploadFiles(url);
               // uploadFiles("http://192.168.31.208:8080/JianCanServerSystem/food/uploadVideo");
                break;

        }
    }

    /**
     * 选择图片/视频后回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        List<LocalMedia> videos;
        switch (requestCode) {
            //如果选择是图片
            case REQUEST_CAMERA_CODE_IMG:
                if (resultCode == RESULT_OK) {
                    type = 0;
                    images = PictureSelector.obtainMultipleResult(data);
                    int length = images.size();
                    LocalMedia media0 = images.get(0);
                    paths.add(media0.getPath());
                    LocalMedia media1 = null;
                    LocalMedia media2 = null;
                    //获取到图片信息
                    if (length == 3) {
                        media1 = images.get(1);
                        media2 = images.get(2);
                        paths.add(media1.getPath());
                        paths.add(media2.getPath());
                    } else if (length == 2) {
                        media1 = images.get(1);
                        paths.add(media1.getPath());
                    }

                    //将图片显示到imageView
                    if (media2 != null) {
                        mediaImg(HomePublishUpdatesActivity.this, media1, pictuer1);
                        mediaImg(HomePublishUpdatesActivity.this, media2, pictuer2);
                    } else if (media1 != null) {
                        mediaImg(HomePublishUpdatesActivity.this, media1, pictuer1);
                    }
                    mediaImg(HomePublishUpdatesActivity.this, media0, pictuer0);

                }
                break;

            //如果选择的视频
            case REQUEST_CAMERA_CODE_VIDEO:
                if (resultCode == RESULT_OK) {
                    type = 1;
                    videos = PictureSelector.obtainMultipleResult(data);
                    LocalMedia media = videos.get(0);
                    paths.add(media.getPath());
                    getVideoThumb(HomePublishUpdatesActivity.this, media, pictuer0);
                }
                break;
        }
    }

    /**
     * 选择图片
     */
    public void openPics() {
        PictureSelector.create(HomePublishUpdatesActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(4)
                .minSelectNum(1)
                .maxSelectNum(3)
                .previewImage(false)// 是否可预览图片
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(200, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .rotateEnabled(false) // 裁剪是否可旋转图片
                .forResult(REQUEST_CAMERA_CODE_IMG);
    }

    /**
     * 裁剪图片
     *
     * @param activity
     * @param media
     * @param imageView
     */
    private void mediaImg(Activity activity, LocalMedia media, ImageView imageView) {
        String path = "";
        if (media.isCut() && !media.isCompressed()) {
            // 裁剪过
            path = media.getCutPath();
        } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            path = media.getCompressPath();
        } else {
            // 原图
            path = media.getPath();
        }
        Glide.with(activity)
                .load(path)
                .into(imageView);
    }

    /**
     * 选择视频
     */
    public void openVideos() {
        PictureSelector.create(HomePublishUpdatesActivity.this)
                .openGallery(PictureMimeType.ofVideo())
                .minSelectNum(1)
                .videoQuality(0)// 视频录制质量 0 or 1 int
                .videoMaxSecond(60)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60)//视频秒数录制 默认60s int
                .previewImage(false)// 是否可预览图片
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .isCamera(true)// 是否显示拍照按钮
                .forResult(REQUEST_CAMERA_CODE_VIDEO);
    }

    /**
     * 获取视频第一帧图片作为预览图
     */
    public Bitmap getVideoThumb(Activity activity, LocalMedia media, ImageView imageView) {

        MediaMetadataRetriever date = new MediaMetadataRetriever();
        String path = media.getPath();
        date.setDataSource(path);
        Bitmap bitmap = date.getFrameAtTime();
        Glide.with(activity)
                .asBitmap()
                .load(bitmap)
                .into(imageView);

        return bitmap;
    }

    /**
     * 销毁dialog
     */
    public void destroyDialog() {
        if (mCameraDialog != null && mCameraDialog.isShowing()) {
            mCameraDialog.cancel();
            mCameraDialog = null;
        }
    }

    /**
     * 上传动态
     */

    public void uploadFiles(String url) {
        RequestBody requestBody = null;
        RequestBody fileBody = null;
        File file = null;
        File file1;
        Food foods = new Food();
        foods.setContent( content.getText().toString());
        foods.setTitle(title.getText().toString());
        Gson gson = new Gson();
        String foodGson = gson.toJson(foods,Food.class);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .addFormDataPart("foods",foodGson)
                .addFormDataPart("user",sharedPreferences.getString("user",null));
        switch (type) {
            case 0:
                for (int i = 0; i < paths.size(); i++) {
                    file = new File(paths.get(i));
                    fileBody = RequestBody
                            .create(MediaType.parse("image/*"), file);
                    builder.addFormDataPart("images",file.getName(),fileBody);
                }
                requestBody = builder.build();

                break;
            case 1:
                file1 = new File(paths.get(0));
                fileBody = RequestBody.create(MediaType.parse("video/*"),file1);
                requestBody = builder
                        .addFormDataPart("video", file1.getName(), fileBody)
                        .build();
                break;
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("错误信息", e.getMessage());
                msg.what = 1;
                handler.sendMessage(msg);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                JsonObject msgResponse = new Gson().fromJson(jsonStr, JsonObject.class);
                if (msgResponse.equals("isSuccess:true")) {
                    msg.what = 2;
                    handler.sendMessage(msg);
                } else {
                    msg.what = 3;
                    handler.sendMessage(msg);

                }
            }
        });
    }

}
