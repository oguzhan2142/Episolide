package com.oguzhan.episolide.ui.home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Results implements Serializable
{

    public final String PERSON_TAG = "person"; // key : media_type
    public final String TV_SHOW_TAG = "tv"; // key : media_type
    public final String MOVIE_TAG = "movie"; // key : media_type

    public List<JSONObject> persons;
    public List<JSONObject> tvShows;
    public List<JSONObject> movies;

    public Results(JSONObject searchResultRoot)
    {
        persons = new ArrayList<>();
        tvShows = new ArrayList<>();
        movies = new ArrayList<>();

        seperateJsonObjects(searchResultRoot);

    }

    private void seperateJsonObjects(JSONObject searchResultRoot)
    {

        JSONArray results = null;
        try
        {
            results = searchResultRoot.getJSONArray("results");


            for (int i = 0; i < results.length(); i++)
            {
                try
                {
                    JSONObject result = results.getJSONObject(i);

                    if (result.get("media_type") == PERSON_TAG)
                        persons.add(result);
                    else if (result.get("media_type") == TV_SHOW_TAG)
                        tvShows.add(result);
                    else if (result.get("media_type") == MOVIE_TAG)
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
