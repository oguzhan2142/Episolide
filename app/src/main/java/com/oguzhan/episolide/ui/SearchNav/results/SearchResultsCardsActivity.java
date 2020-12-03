package com.oguzhan.episolide.ui.SearchNav.results;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.ui.SearchResultsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SearchResultsCardsActivity extends AppCompatActivity
{
    public final String PERSON_TAG = "person"; // key : media_type
    public final String TV_SHOW_TAG = "tv"; // key : media_type
    public final String MOVIE_TAG = "movie"; // key : media_type

    public static final String TAG_MEDIA_TYPE = "MEDIA_TYPE";
    public static final String TAG_RESULTS = "RESULTS";
    public static final String TAG_URL = "URL";

    private String moviesResult;
    private String tvShowsResult;
    private String personResult;

    private String movieUrl;
    private String tvShowUrl;
    private String personUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_cards);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_result_card_activity);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        unpackBundle();
        CardView moviesCardView = findViewById(R.id.movies_card);
        moviesCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString(TAG_URL, movieUrl);
                bundle.putString(TAG_RESULTS, moviesResult);
                bundle.putSerializable(TAG_MEDIA_TYPE, SearchResultsAdapter.MediaType.MOVIE);
                goSearchResultActivity(bundle);
            }
        });


        CardView tvShowsCardView = findViewById(R.id.tv_shows_card);
        tvShowsCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString(TAG_URL, tvShowUrl);
                bundle.putString(TAG_RESULTS, tvShowsResult);
                bundle.putSerializable(TAG_MEDIA_TYPE, SearchResultsAdapter.MediaType.TV_SHOW);
                goSearchResultActivity(bundle);
            }
        });

        CardView peopleCardView = findViewById(R.id.people_card);
        peopleCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString(TAG_URL, personUrl);
                bundle.putString(TAG_RESULTS, personResult);
                bundle.putSerializable(TAG_MEDIA_TYPE, SearchResultsAdapter.MediaType.PERSON);
                goSearchResultActivity(bundle);
            }
        });




        setResultText(moviesResult, R.id.movies_results_amount_text);
        setResultText(tvShowsResult, R.id.tv_shows_results_amount_text);
        setResultText(personResult, R.id.people_results_amount_text);


    }

    private void unpackBundle()
    {
        Intent i = getIntent();

        movieUrl = i.getExtras().getString("movie_url");
        moviesResult = i.getExtras().getString("movie_results");
        tvShowUrl = i.getExtras().getString("tv_show_url");
        tvShowsResult = i.getExtras().getString("tv_show_results");
        personUrl = i.getExtras().getString("person_url");
        personResult = i.getExtras().getString("person_results");
    }

    private void goSearchResultActivity(Bundle bundleToTransfer)
    {
        Intent intent = new Intent(SearchResultsCardsActivity.this, SearchResultsActivity.class);
        intent.putExtras(bundleToTransfer);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }


    private void setResultText(String data, int textCardId)
    {

        TextView textView = findViewById(textCardId);
        try
        {
            JSONObject dataJson = new JSONObject(data);
            int totalResults = dataJson.getInt("total_results");
            textView.setText(String.format("(%s)", totalResults));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }


}