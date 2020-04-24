package com.example.test12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author june
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List <String> mDataList;
    private int mItemLayout;

    public MyRecyclerViewAdapter( Context context, int itemLayout, List<String> datalist) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemLayout = itemLayout;
        mDataList = datalist;
    }

    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(mItemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder( MyRecyclerViewAdapter.MyViewHolder holder, final int position) {
        holder.mTextView.setText(mDataList.get(position));
        holder.mTextView.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick( View v ) {
                Toast.makeText (v.getContext (), "点的是:" + position , Toast.LENGTH_SHORT).show ( );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public MyViewHolder( View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
