package com.example.user.jiancan.home.activityAndFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.user.jiancan.R;
import com.example.user.jiancan.home.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;


/**
 * @author june
 */
public class DownloadFileShow extends AppCompatActivity {
    private ImageView mDownloadBackImgView;
    private TextView mDownloadFoodDetailUser;
    private VideoView mJCVideo;
    private Banner mBanner;
    private TextView mDownloadFoodDetailTitle;
    private TextView mDownloadFoodDetailText;
    private String videoUrl;
    private String videoTitle;
    private final String BASE_PATH = Environment.getExternalStorageDirectory ( ).getPath ( );
    private final String DATA_PATH = "/downloadData/";
    private final String IMAGE_PATH = "/downloadImages/";
    private final String VIDEO_PATH = "/downloadVideos/";
    private String title = "美味的酱油炒饭！";
    private String nickName = "";
    private String src = "";
    private String content = "黄瓜半根，胡萝卜半根，豆腐干两块，香肠一根，一个鸡蛋。\n" +
            "拌汁料：两勺生抽，一勺耗油，半勺老抽，1/3白糖。\n" +
            "油热炒鸡蛋，盛出备用。再加入少许橄榄油，油热放入所有食材，翻炒熟后，放入米饭。中火翻炒均匀，把米饭铺在锅底，2-3分钟后，会有少许锅巴，味道更好哦！放入鸡蛋和拌好的汁料，翻炒混匀就可以出锅啦！\n" +
            "出锅咯！好吃到停不下来！";

    @SneakyThrows
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.download_show);
//        while (fileIsExits (String.valueOf (getDataFromIntent ( )) + ".txt")) {
//            String fileName = String.valueOf (getDataFromIntent ( )) + ".txt";
//            String text = readDownloadFile (BASE_PATH + DATA_PATH + fileName);
//            String[] split = text.split ("&&");
//            title = split[0];
//            src = split[1];
//            content = split[2];
//        }

        initView ( );
        initBanner ();

//        if (src.contains ("\\|")) {
//            mJCVideo.setVisibility (View.GONE);
//            initBanner ( );
//        } else {
//            mBanner.setVisibility (View.GONE);
//            initJCVideo ( );
//        }

    }

    private void initView() {
        mDownloadBackImgView = (ImageView) findViewById (R.id.download_back_imgView);
        mDownloadBackImgView.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick( View v ) {
                finish ();
            }
        });

        mJCVideo = (VideoView) findViewById (R.id.download_media_video);

        mBanner = (Banner) findViewById (R.id.download_img_banner);
        mDownloadFoodDetailTitle = (TextView) findViewById (R.id.download_food_detail_title);
        mDownloadFoodDetailTitle.setText (title);
        mDownloadFoodDetailText = (TextView) findViewById (R.id.download_food_detail_text);
        mDownloadFoodDetailText.setText (content);
    }

    /**
     * 从其他页面跳转过来接收foodId
     */
    private int getDataFromIntent() {
        Intent intent = getIntent ( );
        return intent.getIntExtra ("foodId" , -1);
    }

    /**
     * 初始化banner
     */
    private void initBanner() {
        mBanner.setBannerStyle (BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader (new GlideImageLoader ( ))
                //从Presenter中取出图片资源
                .setImages (getBannerImages ( ))
                .setBannerAnimation (Transformer.Default)
                .isAutoPlay (true)
                .setDelayTime (3000)
                .setIndicatorGravity (BannerConfig.CENTER)
                .start ( );
    }

    /**
     * 获取Banner的图片资源
     */
    public List <Integer> getBannerImages() {
        List<Integer> images = new ArrayList <> ();
        images.add (R.drawable.jkl);
        images.add (R.drawable.asd);
        images.add (R.drawable.qwe);
        return images;
    }


    /**
     * 初始化视频播放器
     */
    private void initJCVideo() {

        mJCVideo.setMediaController (new MediaController (this));
        mJCVideo.setVideoURI (Uri.parse (getVideosFromSdCard ()));
        mJCVideo.start ();

    }

    private String readDownloadFile( String filePath ) throws IOException {
        String path = filePath;
        String text = "";
        File file = new File (path);
        if (file.isDirectory ( )) {
            Log.e ("文件读取" , "the file doesn't exits");
        } else {
            InputStream is = new FileInputStream (file);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader (is);
                BufferedReader bf = new BufferedReader (isr);
                String line;
                int i = 1;
                while ((line = bf.readLine ( )) != null) {
                    //第一次进入循环，读取的第一行是title
                    //第二次读取的是图片或视频名
                    //第三次读取的是内容
                    //default只需将line拼接在content中即可
                    switch (i) {
                        case 1:
                            title = line;
                            break;
                        case 2:
                            src = line;
                            break;
                        case 3:
                            content = line;
                            break;
                        default:
                            content += line;
                            break;
                    }
                    i++;
                }
                text = title + "&&" + src + "&&" + content;
                is.close ( );
            }
        }
        return text;
    }

    private boolean fileIsExits( String path ) {
        try {
            File file = new File (path);
            if (file.exists ( )) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace ( );
            return false;
        }
        return false;
    }

    private List <String> getImageFromSDCard() {
        List<String> images = new ArrayList <> ();
        if (src.contains ("\\|")) {
            String[] split = src.split ("\\|");
            String pic1 = BASE_PATH+IMAGE_PATH+split[0];
            String pic2 = BASE_PATH+IMAGE_PATH+split[1];
            String pic3 = BASE_PATH+IMAGE_PATH+split[2];
            images.add (pic1);
            images.add (pic2);
            images.add (pic3);
        }
        return images;
    }

    private String getVideosFromSdCard(){
        if (!src.contains ("\\|")){
            videoUrl = BASE_PATH+VIDEO_PATH+src;
        }
        return videoUrl;
    }
}
