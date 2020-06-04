package com.example.user.jiancan.message.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.jiancan.R;

import java.util.List;
import java.util.Map;

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> dataSource;
    private int resource;

    public MessageAdapter(Context context, List dataSource,
                          int resource) {
        this.context = context;
        this.dataSource = dataSource;
        this.resource = resource;

    }

    @Override
    public int getCount() {
        if (dataSource == null) {
            return 0;
        }
        return dataSource.size();
    }

    public Object getItem(int position) {
        return dataSource.get(position);
    }

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

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(resource, null);
        }
        ImageView img_head = convertView.findViewById(R.id.ms_head);
        TextView txt_name = convertView.findViewById(R.id.ms_name);
        TextView txt_date = convertView.findViewById(R.id.ms_date);
        TextView txt_content = convertView.findViewById(R.id.ms_content);
        // final Map<String, Object> mItemData = dataSource.get(position);
        // img_head.setImageResource((int) mItemData.get("cakeimg"));
        img_head.setImageResource(R.drawable.back);
        txt_name.setText(dataSource.get(position).get("name").toString());
        txt_date.setText(dataSource.get(position).get("date").toString());
        txt_content.setText(dataSource.get(position).get("content").toString());
        return convertView;
    }
}
