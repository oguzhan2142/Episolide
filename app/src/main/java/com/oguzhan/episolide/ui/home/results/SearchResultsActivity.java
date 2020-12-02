package com.oguzhan.episolide.ui.home.results;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.oguzhan.episolide.MainActivity;
import com.oguzhan.episolide.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SearchResultsActivity extends AppCompatActivity
{

    private final String PAGE_TEMPLATE = "&page=%d";

    private PageManager pageManager;
    private String searchURL;
    private ListView resultsListView;
    private JSONArray results;
    SearchResultsAdapter resultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Toolbar toolbar = findViewById(R.id.toolbar_search_results);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        resultsListView = findViewById(R.id.search_results_listview);


        Intent i = getIntent();
        searchURL = i.getExtras().getString(SearchResultsCardsActivity.TAG_URL);
        String resultsData = i.getExtras().getString(SearchResultsCardsActivity.TAG_RESULTS);

        SearchResultsAdapter.MediaType mediaType = (SearchResultsAdapter.MediaType) i.getExtras()
                .getSerializable(SearchResultsCardsActivity.TAG_MEDIA_TYPE);
        try
        {
            JSONObject resultsJsonRoot = new JSONObject(resultsData);
            pageManager = new PageManager(1, resultsJsonRoot.getInt("total_results"));
            results = resultsJsonRoot.getJSONArray("results");

            resultsAdapter = new SearchResultsAdapter(this, results, mediaType);
            resultsListView.setAdapter(resultsAdapter);


        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        ((ImageButton) findViewById(R.id.search_result_next_btn)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });


        ((ImageButton) findViewById(R.id.search_result_before_btn)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_search_results_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        if (item.getItemId() == R.id.home_btn)
        {
            Intent intent = new Intent(SearchResultsActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private class PageTask extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... strings)
        {
            return null;
        }
    }
}