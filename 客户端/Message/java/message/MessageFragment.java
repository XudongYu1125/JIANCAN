package com.example.user.jiancan.message;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageFragment extends Fragment {

    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private ListView listView;
    private MessageAdapter messageAdapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        Log.e("tip", "进入消息页面");
        initDatalv();

        listView = view.findViewById(R.id.lv_message);
        messageAdapter = new MessageAdapter(getActivity(), dataSource, R.layout.message_list);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "第" + position + "个item", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChatRecordActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initDatalv() {

        int i = 0;

        int[] img = {R.drawable.back, R.drawable.back};
        String[] names = {"加人", "二号"};
        String[] date = {"下午 20：02", "早上 2：30"};
        String[] content = {"歪暗示啊", "阿双方各接引人"};

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
