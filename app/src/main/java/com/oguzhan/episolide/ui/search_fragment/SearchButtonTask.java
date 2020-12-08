package com.oguzhan.episolide.ui.search_fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.oguzhan.episolide.utils.JsonReader;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class SearchButtonTask extends AsyncTask<String, Void, Void>
{


    private final WeakReference<SearchFragment> searchFragment;
    private final WeakReference<Bundle> bundleWeakReference;

    public SearchButtonTask(SearchFragment searchFragment)
    {
        bundleWeakReference = new WeakReference<>(new Bundle());
        this.searchFragment = new WeakReference<>(searchFragment);
        this.searchFragment.get().getProgressBar().setVisibility(View.VISIBLE);
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

        bundleWeakReference.get().putStringArray("datas", datas);
        bundleWeakReference.get().putStringArray("urls", urls);

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid)
    {
        this.searchFragment.get().getProgressBar().setVisibility(View.GONE);
        searchFragment.get().goSearchResultsCardsActivity(bundleWeakReference.get());
        searchFragment.get().setSearching(false);
        super.onPostExecute(aVoid);
    }
}
