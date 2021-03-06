package com.example.withwheel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class search_list_customview extends BaseAdapter {

    LayoutInflater layoutInflater = null;
    private ArrayList<LocationData> mArrayList = null;
    private int count = 0, click;

    public search_list_customview(ArrayList<LocationData> listData, int clickCnt)
    {
        mArrayList = listData;
        click = clickCnt;
        if((listData.size() - clickCnt) < 20){
            count = listData.size() - clickCnt;
        }
        else{
            count = 20;
        }
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
            convertView = layoutInflater.inflate(R.layout.search_list_customview, parent, false);
        }

        if(mArrayList.size() != 0){
            TextView name = convertView.findViewById(R.id.name);
            TextView address = convertView.findViewById(R.id.address);

            name.setText(mArrayList.get(position + click).getName());
            address.setText(mArrayList.get(position + click).getAddress());
        }

        return convertView;
    }
}