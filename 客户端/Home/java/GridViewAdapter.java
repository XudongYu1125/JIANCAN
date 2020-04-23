package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private List <Taobao> taobaoList;
    private LayoutInflater inflater;

    public GridViewAdapter(){

    }

    public GridViewAdapter(List<Taobao> stuList, Context context) {
        this.taobaoList = stuList;
        this.inflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return taobaoList == null?0:taobaoList.size ();
    }

    @Override
    public Object getItem( int position ) {
        return taobaoList.get (position);
    }

    @Override
    public long getItemId( int position ) {
        return position;
    }

    @Override
    public View getView( int position , View convertView , ViewGroup parent ) {
        final View view = inflater.inflate (R.layout.activity_griditem,null);
        ViewHolder viewHolder = new ViewHolder ( );



        Taobao taobao= (Taobao) getItem(position);
        viewHolder.t1=view.findViewById(R.id.t1);
        viewHolder.t1.setText(taobao.getName());
        viewHolder.t2=view.findViewById(R.id.t2);
        viewHolder.t2.setText(taobao.getInformation());
        return view;

    }

    static class ViewHolder{
        TextView t1;
        TextView t2;
    }
}
