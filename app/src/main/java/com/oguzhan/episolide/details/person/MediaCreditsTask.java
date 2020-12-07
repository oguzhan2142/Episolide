package com.oguzhan.episolide.details.person;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.ListviewHeightCalculator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MediaCreditsTask extends AsyncTask<Integer, Void, Void>
{

    private final String CREDITS_URL_TEMPLATE = "https://api.themoviedb.org/3/person/%d/combined_credits?api_key=ee637fe7f604d38049e71cb513a8a04d";

    private WeakReference<PersonDetailActivity> personDetailActivity;

    public MediaCreditsTask(PersonDetailActivity personDetailActivity)
    {
        this.personDetailActivity = new WeakReference<>(personDetailActivity);
    }

    @Override
    protected Void doInBackground(Integer... integers)
    {
        int id = integers[0];
        String url = String.format(Locale.US, CREDITS_URL_TEMPLATE, id);

        JSONObject jsonObject = JsonReader.readJsonFromUrl(url);


        List<PersonDetailListItem> movies = new ArrayList<>();
        List<PersonDetailListItem> tvShows = new ArrayList<>();

        try
        {
            JSONArray jsonArray = jsonObject.getJSONArray("cast");

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject credit = jsonArray.getJSONObject(i);

                if (credit.getString("media_type").equals("tv"))
                {

                    PersonDetailListItem personDetailListItem = new PersonDetailListItem();
                    String name = credit.getString("name");
                    personDetailListItem.name = name;
                    tvShows.add(personDetailListItem);


                } else if (credit.getString("media_type").equals("movie"))
                {
                    String name = credit.getString("title");
                    PersonDetailListItem personDetailListItem = new PersonDetailListItem();
                    personDetailListItem.name = name;
                    movies.add(personDetailListItem);

                }

            }




            CreditsListAdapter adapter = new CreditsListAdapter(
                    personDetailActivity.get().getBaseContext(), movies);

            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    personDetailActivity.get().getMoviesListview().setAdapter(adapter);
                    ListviewHeightCalculator.calculate(personDetailActivity.get().getMoviesListview());
                }
            });


        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        return null;
    }
}
