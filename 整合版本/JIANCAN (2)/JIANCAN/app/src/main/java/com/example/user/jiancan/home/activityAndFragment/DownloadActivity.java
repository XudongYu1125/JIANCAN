package com.example.user.jiancan.home.activityAndFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.Food;
import com.example.user.jiancan.home.entity.Image;
import com.example.user.jiancan.personal.activityAndFragment.PersonalDownloadListActivity;
import com.example.user.jiancan.personal.entity.TUser;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author june
 */
public class DownloadActivity extends AppCompatActivity {
    private ImageView mDownloadBack;
    private ProgressBar mDownloadBar;
    private TextView mDownloadFoodName;
    private OkHttpClient okHttpClient;
    private Food bean;
    private TextView mDownloadPercentage;
    private LinearLayout mToDownload;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private TextView mCountItem;
    private String nickName;
    private int userId;

    @SneakyThrows
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.downloading_layout);
        initView ( );

        Intent intent = getIntent ( );
        int foodId = intent.getIntExtra("foodInfo",-1);
        userId = intent.getIntExtra ("userId",-1);

        //bean = getFoodFromRequest (foodId);

    }

    private void initView() {

        mDownloadBack = (ImageView) findViewById (R.id.download_back);
        mDownloadBack.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick( View v ) {
                finish ( );
            }
        });

        mDownloadBar = (ProgressBar) findViewById (R.id.download_bar);

        mDownloadPercentage = (TextView) findViewById (R.id.percentage);

        mDownloadFoodName = (TextView) findViewById (R.id.download_food_name);

        mToDownload = (LinearLayout) findViewById (R.id.to_download);
        mToDownload.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent (DownloadActivity.this , PersonalDownloadListActivity.class);
                startActivity (intent);
            }
        });
        mCountItem = (TextView) findViewById (R.id.count_item);

    }

    private Food getFoodFromRequest( int foodId ) {
        final  Food food = new Food();
        okHttpClient = new OkHttpClient ( );
        RequestBody requestBody = RequestBody.create (MediaType.parse("text/plain"), "1");
        final Request request = new Request.Builder ()
                .post (requestBody)
                .url (Constant.URL_GET_FOOD_BY_ID + foodId)
                .build ();
        Call call = okHttpClient.newCall (request);
        call.enqueue (new Callback ( ) {
            @Override
            public void onFailure( Call call , IOException e ) {
                Log.e("错误信息",e.getMessage());
            }


            @Override
            public void onResponse( Call call , Response response ) throws IOException {
                String fooStr = response.body ().string ();
                try {
                    JSONObject object = new JSONObject(fooStr);
                    food.setFoodName(object.getString("foodName"));
                    food.setContent(object.getString("content"));
                    food.setDownload(object.getInt("download"));
                    food.setFabulous(object.getInt("fabulous"));
                    food.setId(object.getInt("id"));
                    food.setTitle(object.getString("title"));
                    food.setUploadDate(object.getString("uploadDate"));
                    food.setUserId(object.getInt("userId"));
                    food.setVegetablesId(object.getInt("vegetablesId"));
                    if (object.getString("video") == null){
                        JSONArray imageArray = new JSONArray(object.getString("images"));
                        Set<Image> imageSet = new HashSet<>();
                        for (int j = 0; j < imageArray.length(); j++) {
                            org.json.JSONObject imageObject = imageArray.getJSONObject(j);
                            Image image = new Image();
                            image.setId(imageObject.getInt("id"));
                            image.setImageName(imageObject.getString("imageName"));
                            imageSet.add(image);
                        }
                        food.setImages(imageSet);
                    }else{
                        food.setVideo(object.getString("video"));

                    }
                    DownloadActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bean = food;
                            mDownloadFoodName.setText (bean.getFoodName ( ));
                            /*try {
                                downloadFood ( );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return food;
    }

    private void downloadFood() throws Exception {
        downloadText ( );

        if (bean.getVideo() == null) {
            downloadImages ( );
        } else {
            downloadVideos ( );
        }

        uploadData (userId, bean.getId ( ));
    }

    private void downloadText() throws Exception {
        String FILE_NAME = "" + bean.getId ( ) + ".txt";
        String FILE_PATH = "/sdcard/downloadData/";
        String imageUrl = "";
        String text = "";
        if (bean.getType ( ) == 0) {
            text = getTUser (bean.getUserId ()) + "\n" + bean.getTitle ( ) + "\n" + bean.getVideo ( ) + "\n" + bean.getContent ( );
        } else {
            text = getTUser (bean.getUserId ()) + "\n" + bean.getTitle ( ) + "\n";
            for (Image image : bean.getImages ( )) {
                imageUrl = "|" + image.getImageName ( );
            }
            text = text + imageUrl + "\n" + bean.getContent ( );
        }
        //申请sd卡使用权限
        FileOperate.verifyStoragePermissions (DownloadActivity.this);
        //创建文件夹
        FileOperate.makeRootDirectory (FILE_PATH);
        if (!FileOperate.fileIsExists (FILE_PATH + FILE_NAME)) {
            FileOperate.writeData (FILE_PATH , FILE_NAME , text);
        }

    }


    private void downloadImages() throws Exception {
        String path = "/downloadImages/";
        Set <Image> images = bean.getImages ();
        Log.e ("images", String.valueOf (images.size ()));
        List <String> picList = new ArrayList <> ( );
        for (Image image : images) {
            picList.add (image.getImageName ( ));
        }

        String BASE_PATH = "http://39.97.3.111:8080/JianCanServerSystem/upload/images/";
        int i = 1;
        for (String imageName : picList) {
            i++;
            String FILE_PATH = BASE_PATH + imageName;
            final int finalI = i;
            DownloadUtil.get ( ).download (FILE_PATH , path , new DownloadUtil.OnDownloadListener ( ) {
                @Override
                public void onDownloadSuccess() {
                    runOnUiThread (new Runnable ( ) {
                        @Override
                        public void run() {
                            if (finalI >= 3){
                                Toast.makeText (DownloadActivity.this , "下载完成" , Toast.LENGTH_SHORT).show ( );
                            }
                            mCountItem.setText (String.valueOf (finalI)+"个");
                        }
                    });
                }

                @Override
                public void onDownloading( final int progress ) {
                    runOnUiThread (new Runnable ( ) {
                        @Override
                        public void run() {
                            mDownloadBar.setProgress (progress);
                            mDownloadPercentage.setText (progress + "%");
                        }
                    });
                }

                @Override
                public void onDownloadFailed() {
                    runOnUiThread (new Runnable ( ) {
                        @Override
                        public void run() {
                            Toast.makeText (DownloadActivity.this , "下载失败" , Toast.LENGTH_SHORT).show ( );
                        }
                    });
                }
            });
        }

    }

    private void downloadVideos() {
        String url = "http://39.97.3.111:8080/JianCanServerSystem/upload/videos/" + bean.getVideo ( );
        String path = "/downloadVideos/";
        DownloadUtil.get ( ).download (url , path , new DownloadUtil.OnDownloadListener ( ) {
            @Override
            public void onDownloadSuccess() {
                runOnUiThread (new Runnable ( ) {
                    @Override
                    public void run() {
                        Toast.makeText (DownloadActivity.this , "下载完成" , Toast.LENGTH_SHORT).show ( );
                    }
                });
            }

            @Override
            public void onDownloading( final int progress ) {
                runOnUiThread (new Runnable ( ) {
                    @Override
                    public void run() {
                        mDownloadBar.setProgress (progress);
                        mDownloadPercentage.setText (progress + "%");
                    }
                });
            }

            @Override
            public void onDownloadFailed() {
                runOnUiThread (new Runnable ( ) {
                    @Override
                    public void run() {
                        Toast.makeText (DownloadActivity.this , "下载失败" , Toast.LENGTH_SHORT).show ( );
                    }
                });
            }
        });
    }

    private void uploadData( int userId , int foodId ) {
        okHttpClient = new OkHttpClient ( );
        Request request = new Request.Builder ( )
                .url (Constant.URL_UPLOAD_DOWNLOAD_DATA + userId + "/" + foodId)
                .build ( );
        okHttpClient.newCall (request).enqueue (new Callback ( ) {
            @Override
            public void onFailure( Call call , IOException e ) {
                System.out.println (e.getMessage ( ));
            }

            @Override
            public void onResponse( Call call , Response response ) throws IOException {
                System.out.println ("上传成功");
            }
        });
    }


    private String getTUser (int id){
        okHttpClient = new OkHttpClient ();
        final Request request = new Request.Builder ()
                .url (Constant.URL_USER_INFO+id)
                .build ();
        okHttpClient.newCall (request).enqueue (new Callback ( ) {
            @Override
            public void onFailure( Call call , IOException e ) {
                System.out.println (e.getMessage () );
            }

            @Override
            public void onResponse( Call call , Response response ) throws IOException {
                String str = response.body ().string ();
                TUser user = new Gson ().fromJson (str,TUser.class);
                nickName = user.getNickname ();
            }
        });
        return nickName;
    }
}

class FileOperate {

    /**
     * Created by june on 2020/5/16.
     */


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE" , "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static void verifyStoragePermissions( Activity activity ) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission (activity , "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions (activity , PERMISSIONS_STORAGE , REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace ( );
        }
    }


    public static void writeData( String url , String name , String content ) {
        writeTxtToFile (content , url , name);
    }

    // 将字符串写入到文本文件中
    private static void writeTxtToFile( String strcontent , String filePath , String fileName ) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath (filePath , fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File (strFilePath);
            if (!file.exists ( )) {
                Log.d ("TestFile" , "Create the file:" + strFilePath);
                file.getParentFile ( ).mkdirs ( );
                file.createNewFile ( );
            }
            RandomAccessFile raf = new RandomAccessFile (file , "rwd");
            raf.seek (file.length ( ));
            raf.write (strContent.getBytes ( ));
            raf.close ( );
        } catch (Exception e) {
            Log.e ("TestFile" , "Error on write File:" + e);
        }
    }

    //生成文件
    private static File makeFilePath( String filePath , String fileName ) {
        File file = null;
        makeRootDirectory (filePath);
        try {
            file = new File (filePath + fileName);
            if (!file.exists ( )) {
                file.createNewFile ( );
            }
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        return file;
    }


    //判断文件是否存在
    public static boolean fileIsExists( String strFile ) {
        try {
            File f = new File (strFile);
            if (!f.exists ( )) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    //生成文件夹
    public static void makeRootDirectory( String filePath ) {
        File file = null;
        try {
            file = new File (filePath);
            //不存在就新建
            if (!file.exists ( )) {
                file.mkdir ( );
            }
        } catch (Exception e) {
            Log.i ("error:" , e + "");
        }


    }
}

/**
 * @author june
 */
class DownloadUtil {
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil ( );
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient ( );
    }

    /**
     * @param url      下载连接
     * @param saveDir  储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download( final String url , final String saveDir , final OnDownloadListener listener ) {
        Request request = new Request.Builder ( ).url (url).build ( );
        okHttpClient.newCall (request).enqueue (new Callback ( ) {
            @Override
            public void onFailure( Call call , IOException e ) {
                // 下载失败
                listener.onDownloadFailed ( );
            }

            @Override
            public void onResponse( Call call , Response response ) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir (saveDir);
                try {
                    is = response.body ( ).byteStream ( );
                    long total = response.body ( ).contentLength ( );
                    File file = new File (savePath , getNameFromUrl (url));
                    fos = new FileOutputStream (file);
                    long sum = 0;
                    while ((len = is.read (buf)) != -1) {
                        fos.write (buf , 0 , len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading (progress);
                    }
                    fos.flush ( );
                    // 下载完成
                    listener.onDownloadSuccess ( );
                } catch (Exception e) {
                    listener.onDownloadFailed ( );
                } finally {
                    try {
                        if (is != null) {
                            is.close ( );
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) {
                            fos.close ( );
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir( String saveDir ) throws IOException {
        // 下载位置
        File downloadFile = new File (Environment.getExternalStorageDirectory ( ) , saveDir);
        if (!downloadFile.mkdirs ( )) {
            downloadFile.createNewFile ( );
        }
        String savePath = downloadFile.getAbsolutePath ( );
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl( String url ) {
        return url.substring (url.lastIndexOf ("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading( int progress );

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}

