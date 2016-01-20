package com.stoyan.favouritemovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stoyan.favouritemovies.R;
import com.stoyan.favouritemovies.object.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends BaseAdapter {

    public Context mContext;
    public ArrayList <Trailer> mData;

    public TrailerAdapter(Context c, ArrayList<Trailer> d){
        mContext = c;
        mData = d;
    }

    @Override
    public int getCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Trailer trailer = (Trailer) mData.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_trailer, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.trailer_title);

        tvName.setText(trailer.mName);

        return convertView;
    }

}
