package com.example.user.jiancan.message.activityAndFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.jiancan.R;
import com.example.user.jiancan.User;
import com.example.user.jiancan.message.util.MessageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageFragment extends Fragment {

    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private ListView listView;
    private MessageAdapter messageAdapter = null;
    private User user;
    /// private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences ;

    int i = 0;

    int[] img = {R.drawable.back};
    String[] names = {"郜三"};
    String[] date = {"下午 20:02"};
    String[] content = {"11"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        Log.e("tip", "进入消息页面");

        /*
        okHttpClient = new OkHttpClient();
        sharedPreferences = getActivity().getSharedPreferences("loginInfo", MODE_PRIVATE);
        Gson gson = new GsonBuilder().serializeNulls() .create();
        user = gson.fromJson(sharedPreferences.getString("user", null), User.class);

         */

        initDatalv1();

        listView = view.findViewById(R.id.lv_message);
        messageAdapter = new MessageAdapter(getActivity(), dataSource, R.layout.message_list);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChatRecordActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initDatalv(){

    }

    private void initDatalv1() {

        dataSource = new ArrayList<>();
        for (i = 0; i < img.length; ++i) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", img[i]);
            map.put("name", names[i]);
            map.put("date", date[i]);
            map.put("content", content[i]);
            dataSource.add(map);
        }

    }

}
