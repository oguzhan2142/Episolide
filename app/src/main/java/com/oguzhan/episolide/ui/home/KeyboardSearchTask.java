package com.oguzhan.episolide.ui.home;

import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.utils.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class KeyboardSearchTask extends AsyncTask<String, String, JSONObject>
{
    private final int MAX_TAKABLE_RESULTS = 10;


    private WeakReference<View> viewWeakReference;


    public KeyboardSearchTask(View view)
    {
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


            RecyclerView recyclerView = viewWeakReference.get().findViewById(R.id.resycle_view);

            RecyclerAdapter adapter = new RecyclerAdapter(viewWeakReference.get().getContext(), results);
            recyclerView.setAdapter(adapter);


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }

}



