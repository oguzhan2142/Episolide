package com.oguzhan.episolide.details.person;

import android.app.Person;
import android.media.tv.TvRecordingClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.ListviewHeightCalculator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MediaCreditsTask extends AsyncTask<Integer, Void, Void>
{

    private final String CREDITS_URL_TEMPLATE = "https://api.themoviedb.org/3/person/%d/combined_credits?api_key=ee637fe7f604d38049e71cb513a8a04d";

    private WeakReference<PersonDetailActivity> personDetailActivity;

    // 10 movies as cast
    private final String LISTVIEWS_HEADER_TEMPLATE = "%d %s as %s";


    List<PersonCreditDetails> moviesCastList = new ArrayList<>();
    List<PersonCreditDetails> tvShowsCastList = new ArrayList<>();
    List<PersonCreditDetails> moviesCrewList = new ArrayList<>();
    List<PersonCreditDetails> tvShowsCrewList = new ArrayList<>();

    public MediaCreditsTask(PersonDetailActivity personDetailActivity)
    {
        this.personDetailActivity = new WeakReference<>(personDetailActivity);
    }

    @Override
    protected Void doInBackground(Integer... integers)
    {
        int id = integers[0];
        String url = String.format(Locale.US, CREDITS_URL_TEMPLATE, id);
        Log.d("MESAJ", url);
        JSONObject jsonObject = JsonReader.readJsonFromUrl(url);


        JSONArray castJsonArray = null;
        JSONArray crewJsonArray = null;
        try
        {
            castJsonArray = jsonObject.getJSONArray("cast");
            crewJsonArray = jsonObject.getJSONArray("crew");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        for (int i = 0; i < castJsonArray.length(); i++)
        {

            try
            {
                JSONObject credit = castJsonArray.getJSONObject(i);

                if (credit.getString("media_type").equals("tv"))
                {
                    String name = credit.getString("name");
                    String roleName = credit.getString("character");
                    String firstAirDate = credit.getString("first_air_date");

                    PersonCreditDetails personCredit = PersonCreditDetails.CastInstance(name, roleName, firstAirDate);

                    tvShowsCastList.add(personCredit);

                } else if (credit.getString("media_type").equals("movie"))
                {
                    String name = credit.getString("title");
                    String roleName = credit.getString("character");
                    String firstAirDate = credit.getString("release_date");

                    PersonCreditDetails personCredit = PersonCreditDetails.CastInstance(name, roleName, firstAirDate);

                    moviesCastList.add(personCredit);
                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }


        for (int i = 0; i < crewJsonArray.length(); i++)
        {
            try
            {
                JSONObject credit = crewJsonArray.getJSONObject(i);
                if (credit.getString("media_type").equals("tv"))
                {
                    String name = credit.getString("name");
                    String job = credit.getString("job");
                    String firstAirDate = credit.getString("first_air_date");

                    PersonCreditDetails personCredit = PersonCreditDetails.CrewInstance(name, job, firstAirDate);
                    tvShowsCrewList.add(personCredit);


                } else if (credit.getString("media_type").equals("movie"))
                {
                    String name = credit.getString("title");
                    String job = credit.getString("job");
                    String firstAirDate = credit.getString("release_date");

                    PersonCreditDetails personCredit = PersonCreditDetails.CrewInstance(name, job, firstAirDate);
                    moviesCrewList.add(personCredit);
                }
            } catch (JSONException e)
            {

                e.printStackTrace();
            }
        }


        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);


        setLIstsHeaders();
        sortListsByDate();
        makeInvisibleEmptyLists();

        CreditsListAdapter moviesCastAdapter = new CreditsListAdapter(
                personDetailActivity.get().getBaseContext(), moviesCastList);

        CreditsListAdapter tvShowsCastAdapter = new CreditsListAdapter(
                personDetailActivity.get().getBaseContext(), tvShowsCastList
        );

        CreditsListAdapter moviesCrewAdapter = new CreditsListAdapter(
                personDetailActivity.get().getBaseContext(), moviesCrewList);

        CreditsListAdapter tvShowsCrewAdapter = new CreditsListAdapter(
                personDetailActivity.get().getBaseContext(), tvShowsCrewList);


        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {

                personDetailActivity.get().getMoviesCastListview().setAdapter(moviesCastAdapter);
                ListviewHeightCalculator.setListViewHeightBasedOnItems(personDetailActivity.get().getMoviesCastListview());

                personDetailActivity.get().getTvShowsCastListView().setAdapter(tvShowsCastAdapter);
                ListviewHeightCalculator.setListViewHeightBasedOnItems(personDetailActivity.get().getTvShowsCastListView());

                personDetailActivity.get().getTvShowsCrewListView().setAdapter(tvShowsCrewAdapter);
                ListviewHeightCalculator.setListViewHeightBasedOnItems(personDetailActivity.get().getTvShowsCrewListView());


                personDetailActivity.get().getMoviesCrewListview().setAdapter(moviesCrewAdapter);
                ListviewHeightCalculator.setListViewHeightBasedOnItems(personDetailActivity.get().getMoviesCrewListview());


            }
        });
    }

    private void makeInvisibleEmptyLists()
    {
        if (moviesCastList.size() == 0)
        {
            personDetailActivity.get().getMoviesCastListviewParent().setVisibility(View.GONE);
        }
        if (tvShowsCastList.size() == 0)
        {
            personDetailActivity.get().getTvShowsCastListviewParent().setVisibility(View.GONE);
        }
        if (moviesCrewList.size() == 0)
        {
            personDetailActivity.get().getMoviesCrewListviewParent().setVisibility(View.GONE);
        }
        if (tvShowsCrewList.size() == 0)
        {
            personDetailActivity.get().getTvShowsCrewListviewParent().setVisibility(View.GONE);
        }
    }

    private void sortListsByDate()
    {
        Collections.sort(moviesCastList);
        Collections.sort(tvShowsCastList);
        Collections.sort(moviesCrewList);
        Collections.sort(tvShowsCrewList);
    }

    private void setLIstsHeaders()
    {
        personDetailActivity.get().getMoviesCastListTextview().setText(
                String.format(Locale.US, LISTVIEWS_HEADER_TEMPLATE, moviesCastList.size(), "Movies", "Cast"));
        personDetailActivity.get().getTvShowsCastListTextview().setText(
                String.format(Locale.US, LISTVIEWS_HEADER_TEMPLATE, tvShowsCastList.size(), "Tv Shows", "Cast"));


        personDetailActivity.get().getMoviesCrewListTextview().setText(
                String.format(Locale.US, LISTVIEWS_HEADER_TEMPLATE, moviesCrewList.size(), "Movies", "Crew"));

        personDetailActivity.get().getTvShowsCrewListTextview().setText(
                String.format(Locale.US, LISTVIEWS_HEADER_TEMPLATE, tvShowsCrewList.size(), "Tv Shows", "Crew"));
    }
}
