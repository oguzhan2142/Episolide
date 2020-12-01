package com.oguzhan.episolide.ui.home;

import android.os.AsyncTask;

import com.oguzhan.episolide.utils.JsonReader;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Objects;

public class SearchButtonTask extends AsyncTask<String, Void, Void>
{


    private final WeakReference<HomeFragment> homeFragmentWeakReference;

    private final WeakReference<HashMap<String, String>> datasWeakReferance;

    public SearchButtonTask(HomeFragment homeFragment)
    {
        homeFragmentWeakReference = new WeakReference<>(homeFragment);
        datasWeakReferance = new WeakReference<>(new HashMap<>());
    }

    @Override
    protected Void doInBackground(String... strings)
    {

        String movieUrl = strings[0];
        String tvShowUrl = strings[1];
        String personUrl = strings[2];

        String movies = Objects.requireNonNull(JsonReader.readJsonFromUrl(movieUrl)).toString();
        String tvShows = Objects.requireNonNull(JsonReader.readJsonFromUrl(tvShowUrl)).toString();
        String persons = Objects.requireNonNull(JsonReader.readJsonFromUrl(personUrl)).toString();

        datasWeakReferance.get().put("movies", movies);
        datasWeakReferance.get().put("tvShows", tvShows);
        datasWeakReferance.get().put("persons", persons);


        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid)
    {
        homeFragmentWeakReference.get().goSearchResultsActivity(datasWeakReferance.get());
        super.onPostExecute(aVoid);
    }
}
