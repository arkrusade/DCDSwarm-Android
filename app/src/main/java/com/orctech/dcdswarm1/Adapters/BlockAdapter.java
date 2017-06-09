package com.orctech.dcdswarm1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.orctech.dcdswarm1.R;

import com.orctech.dcdswarm1.Models.Block;

import java.util.ArrayList;

/**
 * Created by Justin Lee on 6/2/2017.
 */

public class BlockAdapter extends BaseAdapter {
    private Context          mContext;
    private LayoutInflater   mInflater;
    private ArrayList<Block> mDataSource;
    
    public BlockAdapter(Context mContext, ArrayList<Block> mDataSource) {
        this.mContext = mContext;
        this.mDataSource = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }
    
    @Override
    public int getCount() {
        return mDataSource.size();
    }
    
    public Object getItem(int position) {
        return mDataSource.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView != null) {
            rowView = convertView;
        } else {
            rowView = mInflater.inflate(R.layout.list_item_block, parent, false);
        }
        
        TextView nameTextView =
                (TextView) rowView.findViewById(R.id.list_block_name);
        
        TextView timesTextView =
                (TextView) rowView.findViewById(R.id.list_block_times);
        
        Block a = (Block) getItem(position);
        
        nameTextView.setText(a.getName());
        timesTextView.setText(a.getTimes());
        
        return rowView;
    }
}
