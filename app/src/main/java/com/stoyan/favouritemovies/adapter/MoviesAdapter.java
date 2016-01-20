package com.stoyan.favouritemovies.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.stoyan.favouritemovies.object.Movie;
import com.stoyan.favouritemovies.Utils;

import java.util.ArrayList;

public class MoviesAdapter extends BaseAdapter {

    public Context mContext;
    public ArrayList <Movie> mData;

    public MoviesAdapter(Context c, ArrayList<Movie> d){
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

        ImageView imageItemView;
        if (convertView == null) {
            imageItemView = new ImageView(mContext);
            Utils.setPosterImageSizeParams(mContext, imageItemView);
        } else {
            imageItemView = (ImageView) convertView;
        }

        String imageName = Utils.extractValueFromMovieInfo(Utils.MOVIE_POSTER_FIELD,mData.get(position));
        Glide.with(mContext).load(Utils.buildPosterImageUrl(imageName)).into(imageItemView);

        return imageItemView;
    }

}
