package com.stoyan.favoritemovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class MoviesAdapter extends BaseAdapter {

    Context mContext;
    String[] mData;

    MoviesAdapter(Context c, String[] d){
        mContext = c;
        mData = d;
    }

    @Override
    public int getCount() {
        if(mData == null) return 0;
        return mData.length;
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

        // Create the ImageView to return
        ImageView imageItemView;
        if (convertView == null) {
            // if it's not recycled, create and set the attributes
            imageItemView = new ImageView(mContext);
            Utils.setPosterImageSizeParams(mContext,imageItemView);
        } else {
            // this image should have the properties already set
            imageItemView = (ImageView) convertView;
        }

         // set the image url
        String imageName = Utils.extractValueFromMovieInfo(Utils.MOVIE_POSTER_FIELD,mData[position]);
        Picasso.with(mContext).load(Utils.buildPosterImageUrl(imageName)).into(imageItemView);

        return imageItemView;
    }

}
