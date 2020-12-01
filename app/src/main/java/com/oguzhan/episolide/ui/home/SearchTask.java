package com.oguzhan.episolide.ui.home;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.utils.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SearchTask extends AsyncTask<String, String, JSONObject>
{
    private final int MAX_TAKABLE_RESULTS = 10;
    private WeakReference<List<String>> movies;
    private WeakReference<List<String>> tvShows;
    private WeakReference<List<String>> persons;


    private WeakReference<View> viewWeakReference;


    public SearchTask(View view)
    {
        movies = new WeakReference<>(new ArrayList<>());
        tvShows = new WeakReference<>(new ArrayList<>());
        persons = new WeakReference<>(new ArrayList<>());
        this.viewWeakReference = new WeakReference<>(view);

    }

    @Override
    protected JSONObject doInBackground(String... strings)
    {

        return JsonReader.readJsonFromUrl(strings[0]);
    }


    @Override
    protected void onPostExecute(JSONObject s)
    {
        try
        {
            /*
             * KEYS from movieDb
             * movie title
             * tv name
             * person name
             */

            JSONArray results = s.getJSONArray("results");

            for (int i = 0; i < results.length(); i++)
            {
                if (i == MAX_TAKABLE_RESULTS)
                    break;

                JSONObject result = results.getJSONObject(i);

                String mediaType = result.get("media_type").toString();

                if (mediaType.equals("person"))
                {
                    String personName = result.get("name").toString();
                    if (!persons.get().contains(personName))
                        persons.get().add(personName);
                } else if (mediaType.equals("tv"))
                {
                    String tvShowName = result.get("name").toString();
                    if (!tvShows.get().contains(tvShowName))
                        tvShows.get().add(tvShowName);
                } else if (mediaType.equals("movie"))
                {
                    String movieName = result.get("title").toString();
                    if (!movies.get().contains(movieName))
                        movies.get().add(movieName);
                }
            }

            Log.d("films", movies.get().toString());
            Log.d("tvShows", tvShows.get().toString());
            Log.d("persons", persons.get().toString());

            RecyclerView recyclerView = (RecyclerView) viewWeakReference.get().findViewById(R.id.resycle_view);

            RecyclerAdapter productAdapter = new RecyclerAdapter(viewWeakReference.get().getContext(), movies.get());
            recyclerView.setAdapter(productAdapter);

//                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }

}



