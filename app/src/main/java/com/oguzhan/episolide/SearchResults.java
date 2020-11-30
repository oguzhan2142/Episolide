package com.oguzhan.episolide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.oguzhan.episolide.ui.home.HomeFragment;
import com.oguzhan.episolide.ui.home.Results;

public class SearchResults extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        Intent i = getIntent();
        Results results = (Results) i.getSerializableExtra(HomeFragment.SEARCH_RESULTS_TAG);


        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < results.movies.size(); j++)
        {
            builder.append(results.movies.get(j));
        }

        StringBuilder b = new StringBuilder();
        for (int j = 0; j < results.tvShows.size(); j++)
        {
            b.append(results.tvShows.get(j));
        }

        Log.d("TAGTAG", "movies" + builder.toString());
        Log.d("TAGTAG", "tv" + b.toString());

        ((TextView) findViewById(R.id.tv)).setText(builder.toString());
        ((TextView) findViewById(R.id.movies)).setText(b.toString());
    }
}