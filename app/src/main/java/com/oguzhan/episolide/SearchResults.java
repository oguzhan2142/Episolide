package com.oguzhan.episolide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.oguzhan.episolide.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity
{
    public final String PERSON_TAG = "person"; // key : media_type
    public final String TV_SHOW_TAG = "tv"; // key : media_type
    public final String MOVIE_TAG = "movie"; // key : media_type

    public List<JSONObject> persons = new ArrayList<>();
    public List<JSONObject> tvShows = new ArrayList<>();
    public List<JSONObject> movies = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        Intent i = getIntent();
        String results = i.getStringExtra(HomeFragment.SEARCH_RESULTS_TAG);

        try
        {
            JSONObject resultsJson = new JSONObject(results);
            seperateJsonObjects(resultsJson);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }



        ((TextView) findViewById(R.id.tv)).setText(persons.toString());
        ((TextView) findViewById(R.id.movies)).setText(tvShows.toString());
    }

    private void seperateJsonObjects(JSONObject searchResultRoot)
    {

        JSONArray results = null;
        try
        {
            results = searchResultRoot.getJSONArray("results");
            Log.d("TAG", "seperateJsonObjects: " + results.toString());

            for (int i = 0; i < results.length(); i++)
            {
                try
                {
                    JSONObject result = results.getJSONObject(i);
                    Log.d("media_type", result.get("media_type").toString());
                    Log.d("result", (result.get("media_type") == TV_SHOW_TAG) + "");
                    if (result.get("media_type").toString().trim().equals(PERSON_TAG))
                        persons.add(result);
                    else if (result.get("media_type").toString().trim().equals(TV_SHOW_TAG))
                        tvShows.add(result);
                    else if (result.get("media_type").toString().trim().equals(MOVIE_TAG))
                        movies.add(result);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}