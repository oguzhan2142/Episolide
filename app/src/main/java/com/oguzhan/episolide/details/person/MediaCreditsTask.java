package com.oguzhan.episolide.details.person;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.oguzhan.episolide.R;
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

    List<PersonCreditDetails> movies = new ArrayList<>();
    List<PersonCreditDetails> tvShows = new ArrayList<>();

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


        try
        {
            JSONArray jsonArray = jsonObject.getJSONArray("cast");

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject credit = jsonArray.getJSONObject(i);

                if (credit.getString("media_type").equals("tv"))
                {

                    PersonCreditDetails personCreditDetails = new PersonCreditDetails();
                    String name = credit.getString("name");
                    String roleName = credit.getString("character");
                    String firstAirDate = credit.getString("first_air_date");

                    personCreditDetails.name = name;
                    personCreditDetails.roleName = roleName;
                    personCreditDetails.firstAirDate = firstAirDate;

                    tvShows.add(personCreditDetails);


                } else if (credit.getString("media_type").equals("movie"))
                {
                    PersonCreditDetails personCreditDetails = new PersonCreditDetails();
                    String name = credit.getString("title");
                    String roleName = credit.getString("character");
                    String firstAirDate = credit.getString("release_date");

                    personCreditDetails.name = name;
                    personCreditDetails.roleName = roleName;
                    personCreditDetails.firstAirDate = firstAirDate;

                    movies.add(personCreditDetails);

                }

            }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);


        CreditsListAdapter moviesAdapter = new CreditsListAdapter(
                personDetailActivity.get().getBaseContext(), movies);

        CreditsListAdapter tvShowsAdapter = new CreditsListAdapter(
                personDetailActivity.get().getBaseContext(), tvShows
        );


        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {

                personDetailActivity.get().getMoviesListview().setAdapter(moviesAdapter);
                ListviewHeightCalculator.setListViewHeightBasedOnItems(personDetailActivity.get().getMoviesListview());

                personDetailActivity.get().getTvShowsListView().setAdapter(tvShowsAdapter);
                ListviewHeightCalculator.setListViewHeightBasedOnItems(personDetailActivity.get().getTvShowsListView());
            }
        });
    }
}
