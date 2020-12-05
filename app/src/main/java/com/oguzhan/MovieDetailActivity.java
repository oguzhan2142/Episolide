package com.oguzhan;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.ImageView;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.ui.tabbed_activity.PlaceholderFragment;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity
{



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_movie_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        String data = getIntent().getStringExtra(PlaceholderFragment.DETAIL_ACTIVITY_DATA_TAG);
        try
        {
            JSONObject jsonData = new JSONObject(data);
            String backdropPath = jsonData.getString("backdrop_path");
            String backdropURL = Statics.BASE_IMAGE_URL + Statics.BACKDROP_SIZES[1] + backdropPath;
            ImageView backdropImageView = findViewById(R.id.expandedImage);
            Picasso.get().load(backdropURL).into(backdropImageView);

            int id = jsonData.getInt("id");



        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        Log.d("MESAJ",data );

    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}