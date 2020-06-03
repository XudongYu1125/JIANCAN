package com.example.user.jiancan.personal.activityAndFragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PeraonalChangePasswordActivity extends AppCompatActivity {
    private EditText password;
    private EditText newPassword;
    private EditText confirmPassword;
    private Button btnChange;
    private User user;
    private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences;

    private static Activity ac;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(ac,"修改信息失败！",Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ac = this;
        findViews();
        sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        user = new Gson().fromJson(sharedPreferences.getString("user",null),User.class);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.getPassword().equals(password.getText().toString())){
                    Toast.makeText(PeraonalChangePasswordActivity.this, "您输入的原始密码有误！", Toast.LENGTH_SHORT).show();
                }else if (!(newPassword.getText().toString()).equals(confirmPassword.getText().toString())){
                    Toast.makeText(PeraonalChangePasswordActivity.this, "您修改后的密码不一致！", Toast.LENGTH_SHORT).show();
                }else if (!isContainAll(confirmPassword.getText().toString())){
                    Toast.makeText(PeraonalChangePasswordActivity.this, "密码中必须包含数字、英文（大和小写）！", Toast.LENGTH_SHORT).show();
                }else if (newPassword.getText().toString().length()>12 || newPassword.getText().toString().length()<6){
                    Toast.makeText(PeraonalChangePasswordActivity.this, "密码必须为6-12位", Toast.LENGTH_SHORT).show();
                }else{
                    user.setPassword(newPassword.getText().toString());
                    okHttpClient = new OkHttpClient();
                    requestData(user);
                }
            }
        });
    }

    private void requestData(final User user) {Request request = new Request.Builder().url(Constant.URL_CHANGE_USER + new Gson().toJson(user)).build();
        final Call call = okHttpClient.newCall(request);
        //发送请求
        call.enqueue(new Callback() {
            @Override
            //请求失败时回调
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            //请求成功后回调
            public void onResponse(Call call, Response response) throws IOException {
                //不直接更新界面
                String result = response.body().string();
                if (result.equals("0")){
                    Message message = new Message();
                    message.obj = "";
                    myHandler.sendMessage(message);
                }else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user",new Gson().toJson(user));
                    editor.commit();
                    Log.e("222","成功");
                    finish();
                }
            }

        });

    }
    private void findViews() {
        password = findViewById(R.id.et_password);
        newPassword = findViewById(R.id.et_new_password);
        confirmPassword = findViewById(R.id.et_confirm_password);
        btnChange = findViewById(R.id.btn_change_password);
    }

    //必须同时包含大小写字母及数字
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase && isUpperCase && str.matches(regex);
        return isRight;
    }

}
