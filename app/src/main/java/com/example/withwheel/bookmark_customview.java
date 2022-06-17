package com.example.withwheel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class bookmark_customview extends BaseAdapter {

    LayoutInflater layoutInflater = null;
    private ArrayList<LocationData> mArrayList = null;
    private int count = 0;

    public bookmark_customview(ArrayList<LocationData> listData, int cnt)
    {
        mArrayList = listData;
        count = cnt;
    }

    @Override
    public int getCount()
    {
        return count;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (layoutInflater == null)
            {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = layoutInflater.inflate(R.layout.bookmark_customview, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView address = convertView.findViewById(R.id.address);

        name.setText(mArrayList.get(position).getName());
        address.setText(mArrayList.get(position).getAddress());

        return convertView;
    }
}