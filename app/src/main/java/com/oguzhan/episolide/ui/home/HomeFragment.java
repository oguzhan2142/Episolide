package com.oguzhan.episolide.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.SearchResults;
import com.oguzhan.episolide.utils.Statics;
import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.Keyboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment
{

    public static final String SEARCH_RESULTS_TAG = "SEARCH_RESULTS";

    private String searchUrl = "https://api.themoviedb.org/3/search/multi?api_key=ee637fe7f604d38049e71cb513a8a04d&language=%s&query=%s&include_adult=true";
    private EditText editText;
    private ImageButton search_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        editText = getView().findViewById(R.id.search_edit_text);
        search_btn = getView().findViewById(R.id.search_btn);
        editText.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    doSearch();
                    return true;
                }

                return false;
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doSearch();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void doSearch()
    {
        String query = editText.getText().toString();
        String url = String.format(searchUrl, Statics.ENGLISH, query);
        new SearchTask().execute(url);
        Keyboard.hideKeyboard(getActivity());
    }

    private void go(JSONObject searchResultRoot)
    {
        Intent intent = new Intent(getActivity(), SearchResults.class);
        intent.putExtra(SEARCH_RESULTS_TAG, new Results(searchResultRoot));
        startActivity(intent);
    }


    private class SearchTask extends AsyncTask<String, Void, JSONObject>
    {

        @Override
        protected JSONObject doInBackground(String... strings)
        {
            return JsonReader.readJsonFromUrl(strings[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {
            Log.d("d", jsonObject.toString());

            go(jsonObject);
            super.onPostExecute(jsonObject);
        }
    }



}