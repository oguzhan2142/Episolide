package com.oguzhan.episolide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.oguzhan.episolide.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SearchResults extends AppCompatActivity
{
    public final String PERSON_TAG = "person"; // key : media_type
    public final String TV_SHOW_TAG = "tv"; // key : media_type
    public final String MOVIE_TAG = "movie"; // key : media_type

    private JSONObject moviesResult;
    private JSONObject tvShowsResult;
    private JSONObject personsResult;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_result);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        CardView moviesCardView = findViewById(R.id.movies_card);
        moviesCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("TAG", "onClick: movies");
            }
        });


        CardView tvShowsCardView = findViewById(R.id.tv_shows_card);
        tvShowsCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("TAG", "onClick: tvshows");
            }
        });

        CardView peopleCardView = findViewById(R.id.people_card);
        peopleCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("TAG", "onClick: people");
            }
        });

        try
        {
            Intent i = getIntent();
            moviesResult = new JSONObject(i.getStringExtra(HomeFragment.MOVIE_TAG));
            tvShowsResult = new JSONObject(i.getStringExtra(HomeFragment.TV_SHOW_TAG));
            personsResult = new JSONObject(i.getStringExtra(HomeFragment.PERSON_TAG));

            setResultText(moviesResult, R.id.movies_results_amount_text);
            setResultText(tvShowsResult, R.id.tv_shows_results_amount_text);
            setResultText(personsResult, R.id.people_results_amount_text);
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


    private void setResultText(JSONObject data, int textCardId)
    {

        TextView textView = findViewById(textCardId);
        try
        {
            int totalResults = data.getInt("total_results");
            textView.setText(String.format("(%s)", totalResults));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }


}