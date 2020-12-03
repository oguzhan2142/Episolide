package com.oguzhan.episolide.ui.SearchNav;

import android.os.AsyncTask;
import android.os.Bundle;

import com.oguzhan.episolide.utils.JsonReader;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class SearchButtonTask extends AsyncTask<String, Void, Void>
{


    private final WeakReference<SearchNavFragment> homeFragmentWeakReference;
    private final WeakReference<Bundle> bundleWeakReference;

    public SearchButtonTask(SearchNavFragment homeFragment)
    {
        homeFragmentWeakReference = new WeakReference<>(homeFragment);

        bundleWeakReference = new WeakReference<>(new Bundle());
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


        String[] datas = new String[]{movies, tvShows, persons};
        String[] urls = new String[]{movieUrl, tvShowUrl, personUrl};

//        bundleWeakReference.get().putString("movie_url", movieUrl);
//        bundleWeakReference.get().putString("movie_results", movies);
//        bundleWeakReference.get().putString("tv_show_url", tvShowUrl);
//        bundleWeakReference.get().putString("tv_show_results", tvShows);
//        bundleWeakReference.get().putString("person_url", personUrl);
//        bundleWeakReference.get().putString("person_results", persons);

        bundleWeakReference.get().putStringArray("datas", datas);
        bundleWeakReference.get().putStringArray("urls", urls);

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid)
    {
        homeFragmentWeakReference.get().goSearchResultsCardsActivity(bundleWeakReference.get());
        homeFragmentWeakReference.get().setSearching(false);
        super.onPostExecute(aVoid);
    }
}
