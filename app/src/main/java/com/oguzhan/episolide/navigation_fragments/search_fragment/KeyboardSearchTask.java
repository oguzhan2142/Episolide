package com.oguzhan.episolide.navigation_fragments.search_fragment;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.oguzhan.episolide.utils.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class KeyboardSearchTask extends AsyncTask<String, String, JSONObject>
{
    private final int MAX_TAKABLE_RESULTS = 8;


    private WeakReference<RecyclerView> recyclerView;


    public KeyboardSearchTask(RecyclerView recyclerView)
    {
        this.recyclerView = new WeakReference<>(recyclerView);

    }

    @Override
    protected JSONObject doInBackground(String... strings)
    {

        return JsonReader.readJsonFromUrl(strings[0]);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

            JSONArray results;

            if (s == null)
                results = new JSONArray();
            else
                results = s.getJSONArray("results");

            while (results.length() > MAX_TAKABLE_RESULTS)
            {
                results.remove(results.length() - 1);
            }


            RecyclerAdapter adapter = (RecyclerAdapter) recyclerView.get().getAdapter();

            if (adapter != null)
            {
                adapter.setResults(results);
                adapter.notifyDataSetChanged();
            }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }

}



