package com.oguzhan.episolide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.oguzhan.episolide.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SearchResults extends AppCompatActivity
{
    public final String PERSON_TAG = "person"; // key : media_type
    public final String TV_SHOW_TAG = "tv"; // key : media_type
    public final String MOVIE_TAG = "movie"; // key : media_type

    public List<JSONObject> persons = new ArrayList<>();
    public List<JSONObject> tvShows = new ArrayList<>();
    public List<JSONObject> movies = new ArrayList<>();

    private ExpandableListView expandlist_view;
    private ExpandableListAdapter expand_adapter;


    public List<String> list_parent;
    public HashMap<String, List<String>> list_child;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_result);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        String results = i.getStringExtra(HomeFragment.SEARCH_RESULTS_TAG);

        try
        {
            JSONObject resultsJson = new JSONObject(results);
            seperateJsonObjects(resultsJson);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        expandlist_view = (ExpandableListView) findViewById(R.id.expand_listview);
        initilizeLists(); // expandablelistview içeriğini hazırlamak için

        // Adapter sınıfımızı oluşturmak için başlıklardan oluşan listimizi ve onlara bağlı olan elemanlarımızı oluşturmak için HaspMap türünü yolluyoruz
        expand_adapter = new ExpandableListAdapter(getApplicationContext(), list_parent, list_child);
        expandlist_view.setAdapter(expand_adapter);  // oluşturduğumuz adapter sınıfını set ediyoruz
        expandlist_view.setClickable(true);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    public void initilizeLists()
    {
        list_parent = new ArrayList<String>();  // başlıklarımızı listemelek için oluşturduk
        list_child = new HashMap<String, List<String>>(); // başlıklara bağlı elemenları tutmak için oluşturduk

        list_parent.add("Movies");  // ilk başlığı giriyoruz
        list_parent.add("Tv Shows");   // ikinci başlığı giriyoruz
        list_parent.add("Persons");


        list_child.put(list_parent.get(0), getItemNames(movies)); // ilk başlığımızı ve onların elemanlarını HashMap sınıfında tutuyoruz
        list_child.put(list_parent.get(1), getItemNames(tvShows)); // ikinci başlığımızı ve onların elemanlarını HashMap sınıfında tutuyoruz
        list_child.put(list_parent.get(2), getItemNames(persons));

    }


    private List<String> getItemNames(List<JSONObject> jsonObjects)
    {
        // movie title
        // person ve tv'de name

        List<String> list = new ArrayList<>();
        for (JSONObject jsonObject : jsonObjects)
        {
            try
            {
                if (jsonObjects.equals(movies))
                {
                    list.add(jsonObject.get("title").toString());
                } else if (jsonObjects.equals(tvShows) || jsonObjects.equals(persons))
                {
                    list.add(jsonObject.get("name").toString());
                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return list;
    }

    private void seperateJsonObjects(JSONObject searchResultRoot)
    {


        try
        {
            JSONArray results = searchResultRoot.getJSONArray("results");


            for (int i = 0; i < results.length(); i++)
            {
                try
                {
                    JSONObject result = results.getJSONObject(i);

                    if (result.get("media_type").toString().trim().equals(PERSON_TAG))
                        persons.add(result);
                    else if (result.get("media_type").toString().trim().equals(TV_SHOW_TAG))
                        tvShows.add(result);
                    else if (result.get("media_type").toString().trim().equals(MOVIE_TAG))
                        movies.add(result);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}