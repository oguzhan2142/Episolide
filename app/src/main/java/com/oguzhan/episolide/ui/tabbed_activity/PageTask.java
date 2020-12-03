package com.oguzhan.episolide.ui.tabbed_activity;

import android.os.AsyncTask;

import com.oguzhan.episolide.ui.SearchResultsAdapter;
import com.oguzhan.episolide.utils.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class PageTask extends AsyncTask<String, Void, JSONObject>
{


    private final WeakReference<SearchResultsAdapter> adapterReferance;

    public PageTask(SearchResultsAdapter adapter)
    {
        this.adapterReferance = new WeakReference<>(adapter);
    }

    @Override
    protected JSONObject doInBackground(String... strings)
    {
        String url = strings[0];

        return JsonReader.readJsonFromUrl(url);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject)
    {


        try
        {
            JSONArray results = jsonObject.getJSONArray("results");
            adapterReferance.get().setJsonArray(results);
            adapterReferance.get().notifyDataSetChanged();


        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        super.onPostExecute(jsonObject);
    }
}