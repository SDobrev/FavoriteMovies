package com.stoyan.favouritemovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.stoyan.favouritemovies.object.Movie;
import com.stoyan.favouritemovies.task.FetchMoviesTask;

import java.util.Arrays;

public class Utils {

    public static final String API_KEY = "a5ce1fb40df99b0a467438d51390c418";

    private static final String POSTER_NOT_FOUND_IMAGE = "https://d3a8mw37cqal2z.cloudfront.net/assets/f996aa2014d2ffddfda8463c479898a3/images/no-poster-w185.jpg";
    private static final String POSTER_BASE_PATH = "https://image.tmdb.org/t/p/w185/";

    // calculate the Aspect ratio of the image (From a sample image on the moviesDB api)
    private static final double MOVIE_IMAGE_ASPECT_RATIO = 278.00/184.00;


    private static final String YOUTUBE_BASE_HTTP = "http://www.youtube.com/watch?v=";
    private static final String VND_YOUTUBE = "vnd.youtube:";

    public static final String MOVIE_ID_FIELD = "id";
    public static final String MOVIE_POSTER_FIELD = "poster";
    public static final String MOVIE_TITLE_FIELD = "title";
    public static final String MOVIE_YEAR_FIELD = "year";
    public static final String MOVIE_RATING_FIELD = "rating";
    public static final String MOVIE_DESCRIPTTION_FIELD = "description";

    private static final String[] MOVIE_INFO_FIELDS = new String[]{
            MOVIE_ID_FIELD,
            MOVIE_POSTER_FIELD,
            MOVIE_TITLE_FIELD,
            MOVIE_YEAR_FIELD,
            MOVIE_RATING_FIELD,
            MOVIE_DESCRIPTTION_FIELD
    };

    public static String buildPosterImageUrl(final String posterImageName){
        String imageUrl = (
                posterImageName != null
                && !posterImageName.isEmpty()
                && !posterImageName.equals("null"))
                ? POSTER_BASE_PATH + posterImageName
                : POSTER_NOT_FOUND_IMAGE;
        return imageUrl;
    }

    public static void setPosterImageSizeParams(Context context, ImageView imageView){

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        // Get the number of rows from resources
        int numRows = context.getResources().getInteger(R.integer.grid_num_rows);
        int numPanes = context.getResources().getInteger(R.integer.num_panes);
        int margin = (int) (2 * context.getResources().getDisplayMetrics().density);

        // Use the display metrics and the number of columns to calculate the size of the images to use
        int movieThumbnailWidth = displayMetrics.widthPixels / numRows / numPanes - 5 ;
        int movieThumbnailHeight = (int) (movieThumbnailWidth * MOVIE_IMAGE_ASPECT_RATIO);

        GridView.LayoutParams layoutParams = new GridView.LayoutParams(movieThumbnailWidth, movieThumbnailHeight);

        // if it's not recycled, initialize some attributes
        // imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView.setLayoutParams(layoutParams);
    }

    public static String extractValueFromMovieInfo(final String infoToExtract, final Movie movieInfo){
        String[] movieInfoArray = movieInfo.dataString.split(FetchMoviesTask.SEPERATOR);
        int position = Arrays.asList(MOVIE_INFO_FIELDS).indexOf(infoToExtract);
        return (movieInfoArray.length > position && !movieInfoArray[position].equals("null"))?movieInfoArray[position]:"";
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void watchYoutubeVideo(String id, Context context){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(VND_YOUTUBE + id));
            context.startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse(YOUTUBE_BASE_HTTP +id));
            context.startActivity(intent);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {

            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
