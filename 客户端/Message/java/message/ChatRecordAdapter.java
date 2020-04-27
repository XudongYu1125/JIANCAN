package com.example.user.jiancan.message;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.jiancan.R;

import java.util.List;
import java.util.Map;

public class ChatRecordAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> dataSource;
    private int resource;

    public ChatRecordAdapter(Context context, List<Map<String, Object>> dataSource,
                             int resource) {
        this.context = context;
        this.dataSource = dataSource;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Map<String, Object> newData) {
        dataSource.add(newData);
        notifyDataSetChanged();
    }

    public void deleteItem(int pos) {
        dataSource.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ImageView img;
        TextView txt;

        if ((Integer) dataSource.get(position).get("who") == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_listi, parent, false);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_listu, parent, false);
        }


        if ((Integer) dataSource.get(position).get("who") == 0) {
            img = (ImageView) convertView.findViewById(R.id.con_imgi);
            txt = (TextView) convertView.findViewById(R.id.con_tvi);
            img.setImageResource((Integer) dataSource.get(position).get("img"));
            txt.setText(dataSource.get(position).get("content").toString());
        } else {
            img = (ImageView) convertView.findViewById(R.id.con_imgu);
            txt = (TextView) convertView.findViewById(R.id.con_tvu);
            img.setImageResource((Integer) dataSource.get(position).get("img"));
            txt.setText(dataSource.get(position).get("content").toString());
        }

        return convertView;
    }


}
