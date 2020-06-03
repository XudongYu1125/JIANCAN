package com.example.user.jiancan.personal.activityAndFragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PersonalChangeMessageActivity extends AppCompatActivity {
    private LinearLayout llChangeName;
    private LinearLayout llChangeAvatar;
    private LinearLayout llChangeSex;
    private TextView tvName;
    private ImageView ivAvatar;
    private TextView tvId;
    private TextView tvSex;
    private SharedPreferences sharedPreferences;
    private User user;
    private Intent intent;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_message);
        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        user = new Gson().fromJson(sharedPreferences.getString("user", null), User.class);
        findViews();
        setOnclicked();
        setContents();
        myIntent = getIntent();
        setResult(101,myIntent);
    }

    private void setContents() {
        String time = sharedPreferences.getString("time",null);
        if (time == null){
            RequestOptions options = new RequestOptions().error(R.drawable.default_vatar);
            Glide.with(this)
                    .load(Constant.BASE_URL + "/upload/avatarimgs/" + user.getImageUrl())
                    .apply(options)
                    .into(ivAvatar);
        }else {
            RequestOptions options = new RequestOptions().signature(new ObjectKey(time)).error(R.drawable.default_vatar);
            Glide.with(this)
                    .load(Constant.BASE_URL + "/upload/avatarimgs/" + user.getImageUrl())
                    .apply(options)
                    .into(ivAvatar);
        }

        tvName.setText(user.getNickname());
        tvId.setText(user.getId() + "");
        tvSex.setText(user.getSex());
    }


    private void setOnclicked() {
        llChangeAvatar.setOnClickListener(new onclicked());
        llChangeName.setOnClickListener(new onclicked());
        llChangeSex.setOnClickListener(new onclicked());
    }

    private void findViews() {
        llChangeName = findViewById(R.id.ll_change_name);
        llChangeAvatar = findViewById(R.id.ll_change_avatar);
        llChangeSex = findViewById(R.id.ll_change_sex);
        tvName = findViewById(R.id.tv_change_name);
        ivAvatar = findViewById(R.id.iv_change_avatar);
        tvSex = findViewById(R.id.tv_change_sex);
        tvId = findViewById(R.id.tv_my_userid);
    }

    private class onclicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_change_name:
                    intent = new Intent();
                    intent.setClass(PersonalChangeMessageActivity.this, PersonalChangeNameActivity.class);
                    intent.putExtra("name", tvName.toString());
                    startActivityForResult(intent, 100);
                    break;
                case R.id.ll_change_sex:
                    intent = new Intent();
                    intent.setClass(PersonalChangeMessageActivity.this, PersonalChangeSexActivity.class);
                    startActivityForResult(intent, 200);
                    break;
                case R.id.ll_change_avatar:
                    Log.e("11", "22");
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("");
                    ActivityCompat.requestPermissions(PersonalChangeMessageActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    break;
            }

        }
    }

    //权限提示框，用户点击允许时回调这个方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //打开手机相册
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case 101:
                String name = data.getStringExtra("changename");
                user = new Gson().fromJson(sharedPreferences.getString("user", null), User.class);
                tvName.setText(user.getNickname());
                Toast.makeText(PersonalChangeMessageActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                break;
            case 201:
                String sex = data.getStringExtra("changesex");
                user = new Gson().fromJson(sharedPreferences.getString("user", null), User.class);
                tvSex.setText(user.getSex());
                Toast.makeText(PersonalChangeMessageActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                break;
            case RESULT_OK:
                Uri uri = data.getData();//图片的uri对象
                Log.e("uri", uri.toString());
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    //获取图片的路径
                    String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                    Glide.with(this).load(imagePath).into(ivAvatar);
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute(imagePath);
                }
                break;
            default:
                break;
        }
    }

    public class MyAsyncTask extends AsyncTask<String, Object, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                String imgName = user.getId()+"";
                url = new URL(Constant.URL_UPLOAD_PIC +imgName);
                Log.e("url",Constant.URL_UPLOAD_PIC +imgName);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.e("21", "99");
                File file = new File(strings[0]);
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setChunkedStreamingMode(1024 * 1024);
                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("Charsert", "utf-8");
                conn.setConnectTimeout(50000);
                conn.setRequestProperty("Content-Type", "image/png");
                conn.connect();
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                DataInputStream in = new DataInputStream(new FileInputStream(file));
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) != -1) {
                    Log.e("123", "123");
                    out.write(buf, 0, len);
                }
                InputStream is = conn.getInputStream();

                in.close();
                out.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String userStr = sharedPreferences.getString("user",null);
            user.setImageUrl(user.getId()+"");
            String userFinalStr = new Gson().toJson(user);
            editor.putString("user",userFinalStr);
            editor.putString("time",String.valueOf(System.currentTimeMillis()));
            editor.commit();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
