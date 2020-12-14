package com.oguzhan.episolide.ui.search_fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.oguzhan.episolide.utils.JsonReader;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class SearchButtonTask extends AsyncTask<String, Void, Void>
{


    private final WeakReference<SearchFragment> searchFragment;


    public SearchButtonTask(SearchFragment searchFragment)
    {

        this.searchFragment = new WeakReference<>(searchFragment);
        this.searchFragment.get().getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(String... strings)
    {


        String movieUrl = strings[0];
        String tvShowUrl = strings[1];
        String personUrl = strings[2];

        JSONObject movies = JsonReader.readJsonFromUrl(movieUrl);
        JSONObject tvShows = JsonReader.readJsonFromUrl(tvShowUrl);
        JSONObject persons = JsonReader.readJsonFromUrl(personUrl);

        if (movies == null || tvShows == null || persons == null)
        {
            Toast.makeText(searchFragment.get().getContext(), "Arama Hatasi", Toast.LENGTH_SHORT).show();
            return null;
        }

        String[] datas = new String[]{movies.toString(), tvShows.toString(), persons.toString()};
        String[] urls = new String[]{movieUrl, tvShowUrl, personUrl};

        Bundle bundle = new Bundle();


        bundle.putStringArray("datas", datas);
        bundle.putStringArray("urls", urls);

        this.searchFragment.get().getProgressBar().setVisibility(View.INVISIBLE);
        searchFragment.get().goSearchResultsCardsActivity(bundle);
        searchFragment.get().setSearching(false);
        return null;
    }



}
