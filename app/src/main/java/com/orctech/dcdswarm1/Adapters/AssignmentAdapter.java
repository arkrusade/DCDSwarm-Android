package com.orctech.dcdswarm1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.orctech.dcdswarm1.R;
import com.orctech.dcdswarm1.Models.Assignment;

import java.util.ArrayList;

/**
 * Created by justinjlee99 on 5/11/2017.
 */

public class AssignmentAdapter extends BaseAdapter {
    private Context               mContext;
    private LayoutInflater        mInflater;
    private ArrayList<Assignment> mDataSource;

    public AssignmentAdapter(Context context, ArrayList<Assignment> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_assignment, parent, false);

        // Get title element
        TextView nameTextView =
                (TextView) rowView.findViewById(R.id.recipe_list_name);

// Get description element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.recipe_list_title);

// Get detail element
        TextView descTextView =
                (TextView) rowView.findViewById(R.id.recipe_list_desc);

        Assignment a = (Assignment) getItem(position);

        nameTextView.setText(a.getClassName());
        titleTextView.setText(a.getTitle());
        descTextView.setText(a.getDescription());

        return rowView;
    }
}
