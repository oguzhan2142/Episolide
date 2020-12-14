package com.oguzhan.episolide.details.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.ui.tabbed_activity.PlaceholderFragment;
import com.oguzhan.episolide.utils.ExpandableTextView;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity
{

    public static final String WEB_ACTIVITY_INTENT_TAG = "URL";

    private ImageView posterImageView;
    private TextView releaseDate;
    private TextView genres;
    private TextView budged;
    private TextView voteAverage;

    private ExpandableTextView overview;

    private ListView productionCountries;
    private ListView productionCompanies;
    private TextView homepageUrl;



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

        posterImageView = findViewById(R.id.poster_image);
        releaseDate = findViewById(R.id.release_date);
        genres = findViewById(R.id.genres);
        budged = findViewById(R.id.budged);
        voteAverage = findViewById(R.id.vote_average);
        productionCountries = findViewById(R.id.production_countries);
        overview = findViewById(R.id.overview_text);
        productionCompanies = findViewById(R.id.production_companies);
        homepageUrl = findViewById(R.id.homepage_url);


        String data = getIntent().getStringExtra(PlaceholderFragment.DETAIL_ACTIVITY_DATA_TAG);
        try
        {
            JSONObject jsonData = new JSONObject(data);

            String originalTitle = jsonData.getString("original_title");
            toolBarLayout.setTitle(originalTitle);

            String backdropPath = jsonData.getString("backdrop_path");
            String backdropURL = Statics.BASE_IMAGE_URL + Statics.BACKDROP_SIZES[1] + backdropPath;
            ImageView backdropImageView = findViewById(R.id.expandedImage);
            Picasso.get().load(backdropURL).into(backdropImageView);

            int id = jsonData.getInt("id");
            new MovieDetailTask(this).execute(id);





            homepageUrl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(MovieDetailActivity.this, WebActivity.class);
                    intent.putExtra(WEB_ACTIVITY_INTENT_TAG, homepageUrl.getText().toString());
                    startActivity(intent);
                }
            });
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    public ImageView getPosterImageView()
    {
        return posterImageView;
    }


    public TextView getReleaseDate()
    {
        return releaseDate;
    }

    public TextView getGenres()
    {
        return genres;
    }

    public TextView getBudged()
    {
        return budged;
    }

    public TextView getVoteAverage()
    {
        return voteAverage;
    }

    public ListView getProductionCountries()
    {
        return productionCountries;
    }

    public ExpandableTextView getOverview()
    {
        return overview;
    }

    public ListView getProductionCompanies()
    {
        return productionCompanies;
    }

    public TextView getHomepageUrl()
    {
        return homepageUrl;
    }
}