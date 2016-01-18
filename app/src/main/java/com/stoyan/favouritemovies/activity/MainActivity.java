package com.stoyan.favouritemovies.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.stoyan.favouritemovies.R;
import com.stoyan.favouritemovies.ShakeDetector;
import com.stoyan.favouritemovies.adapter.ViewPagerAdapter;
import com.stoyan.favouritemovies.fragment.DetailActivityFragment;
import com.stoyan.favouritemovies.fragment.MoviesFragment;
import com.stoyan.favouritemovies.object.Movie;
import com.stoyan.favouritemovies.widget.SlidingTabLayout;


public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {
    private static final CharSequence MOST_POPULAR = "Popular";
    private static final CharSequence HIGHEST_RATED = "Highest Rated";
    private static final CharSequence FAVORITES = "Favorites";
    private static final int SECOND_PAGER_TAB_INDEX = 1;
    private static final CharSequence Titles[] = {MOST_POPULAR, HIGHEST_RATED, FAVORITES};
    private static final int NUMBOFTABS = 3;

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    Toolbar mToolbar;
    ViewPager mPager;
    ViewPagerAdapter mPagerAdapter;
    SlidingTabLayout mTabs;

    private boolean mTwoPane;

    private static final String DETAILFRAGMENT_TAG = "DFTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                handleShakeEvent(count);
            }
        });

        if (!Movie.musicCreated){
            Movie.mediaPlayer = MediaPlayer.create(this, R.raw.sandra);
            Movie.mediaPlayer.start();
            Movie.musicCreated = true;
        }

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, NUMBOFTABS);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);

        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.icons);
            }
        });

        mTabs.setViewPager(mPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void handleShakeEvent(int count) {

        if (Movie.mediaPlayer.isPlaying()) {

            Movie.mediaPlayer.pause();
            Toast.makeText(getApplicationContext(), "Intro Music Off!", Toast.LENGTH_SHORT).show();
        }
        else {
            Movie.mediaPlayer.start();
            Toast.makeText(getApplicationContext(),"Intro Music On!", Toast.LENGTH_SHORT).show();
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.woosh);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if(id == R.id.action_about){
            Intent aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        loadMoviesInCurrentTab();
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    private void loadMoviesInCurrentTab(){

        if(!(mPagerAdapter.getItem(mPager.getCurrentItem()) instanceof MoviesFragment) ) return;

        String sortBy = getString(R.string.most_popular_value);

        if(mPager.getCurrentItem() == SECOND_PAGER_TAB_INDEX ) sortBy = getString(R.string.highest_rated_value);

        MoviesFragment currentMovieFragment = (MoviesFragment) mPagerAdapter.getItem(mPager.getCurrentItem());
        if (null != currentMovieFragment) currentMovieFragment.onSortChanged(sortBy);
    }

    @Override
    public void onItemSelected(String movieData) {
        if (mTwoPane) {

            Bundle args = new Bundle();
            args.putString(Intent.EXTRA_TEXT, movieData);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movieData);
            startActivity(intent);
        }
    }

    // Not working yet
    @Override
    public void onItemLongClick(String movieData){
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putString(Intent.EXTRA_TEXT, movieData);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movieData);
            startActivity(intent);
        }
    }
}
