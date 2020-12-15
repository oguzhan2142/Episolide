package com.oguzhan.episolide.details.movie;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.oguzhan.episolide.R;
import com.oguzhan.episolide.ui.tabbed_activity.PlaceholderFragment;
import com.oguzhan.episolide.utils.ExpandableTextView;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity
{

    public static final String WEB_ACTIVITY_INTENT_TAG = "URL";

    private ImageView posterImageView;
    private TextView releaseDate;
    private TextView genres;
    private TextView budged;
    private TextView voteAverage;

    private ExpandableTextView overview;

    private LinearLayout productionCountries;
    private LinearLayout productionCompanies;

    private TextView homepageUrl;

    private TextView collectionHeader;
    private ImageView collectionPoster;
    private TextView collectionOverview;

    private LinearLayout collectionScrolviewContainer;


    private LinearLayout castContainer;
    private LinearLayout crewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_movie_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        posterImageView = findViewById(R.id.poster_image);
        releaseDate = findViewById(R.id.release_date);
        genres = findViewById(R.id.genres);
        budged = findViewById(R.id.budged);
        voteAverage = findViewById(R.id.vote_average);
        productionCountries = findViewById(R.id.production_countries);
//        productionCompanies = findViewById(R.id.production_companies);
        productionCompanies = findViewById(R.id.production_com);
        overview = findViewById(R.id.overview_text);
        homepageUrl = findViewById(R.id.homepage_url);

        crewContainer = findViewById(R.id.crew_linearlayout);
        castContainer = findViewById(R.id.cast_linearlayout);

        collectionHeader = findViewById(R.id.collection_header);
        collectionOverview = findViewById(R.id.collection_overview);
        collectionPoster = findViewById(R.id.collection_poster);

        collectionScrolviewContainer = findViewById(R.id.collection_scrollview_container);



        String data = getIntent().getStringExtra(PlaceholderFragment.DETAIL_ACTIVITY_DATA_TAG);

        try
        {
            JSONObject jsonData = new JSONObject(data);

            String originalTitle = jsonData.getString("original_title");
            toolBarLayout.setTitle(originalTitle);

            String backdropPath = jsonData.getString("backdrop_path");
            String backdropURL = Statics.BASE_IMAGE_URL + Statics.BACKDROP_SIZES[1] + backdropPath;
            ImageView backdropImageView = findViewById(R.id.expandedImage);
            Picasso.get().load(backdropURL).into(backdropImageView);

            int id = jsonData.getInt("id");
            new MovieDetailTask(this).execute(id);
            new MovieCreditsTask(this).execute(id);


            homepageUrl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(MovieDetailActivity.this, WebActivity.class);
                    intent.putExtra(WEB_ACTIVITY_INTENT_TAG, homepageUrl.getText().toString());
                    startActivity(intent);
                }
            });


        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }


    public LinearLayout createPersonItemLayout(String name, String subtitle, String imageURL)
    {
        LinearLayout content = (LinearLayout) getLayoutInflater().inflate(R.layout.person_item_layout, null);

        TextView nameText = content.findViewById(R.id.person_item_name);
        TextView subtitleText = content.findViewById(R.id.person_item_character_name);
        ImageView posterImage = content.findViewById(R.id.person_item_image);
        nameText.setText(name);
        subtitleText.setText(subtitle);
        Picasso.get().load(imageURL).into(posterImage);
        return content;

    }

    public LinearLayout createProductionItemLayout(String name, String imageURL)
    {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.production_listitem, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 10);

        TextView nameview = linearLayout.findViewById(R.id.production_name);
        ImageView imageview = linearLayout.findViewById(R.id.production_image);

        nameview.setText(name);
        Picasso.get().load(imageURL).into(imageview);

        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    public void addContentToCollectionContainer(LinearLayout content)
    {
        collectionScrolviewContainer.addView(content);
    }

    public LinearLayout createScrollviewContent(String posterURL, String nameOfCollection, String releaseDate)
    {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT

        );

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(layoutParams);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.card_width),
                (int) getResources().getDimension(R.dimen.card_height)

        );
        imageView.setLayoutParams(param);
        Picasso.get().load(posterURL).into(imageView);

        TextView dateText = new TextView(this);
        dateText.setText(releaseDate);
        dateText.setGravity(Gravity.CENTER_HORIZONTAL);
        dateText.setTextSize(12);

        TextView nameText = new TextView(this);
        nameText.setText(nameOfCollection);
        nameText.setGravity(Gravity.CENTER_HORIZONTAL);
        nameText.setTextSize(14);


        linearLayout.addView(dateText);
        linearLayout.addView(imageView);
        linearLayout.addView(nameText);


        return linearLayout;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    public ImageView getPosterImageView()
    {
        return posterImageView;
    }


    public TextView getReleaseDate()
    {
        return releaseDate;
    }

    public TextView getGenres()
    {
        return genres;
    }

    public TextView getBudged()
    {
        return budged;
    }

    public TextView getVoteAverage()
    {
        return voteAverage;
    }

    public LinearLayout getProductionCountries()
    {
        return productionCountries;
    }

    public ExpandableTextView getOverview()
    {
        return overview;
    }


    public TextView getHomepageUrl()
    {
        return homepageUrl;
    }

    public TextView getCollectionHeader()
    {
        return collectionHeader;
    }

    public ImageView getCollectionPoster()
    {
        return collectionPoster;
    }

    public TextView getCollectionOverview()
    {
        return collectionOverview;
    }


    public LinearLayout getProductionCompanies()
    {
        return productionCompanies;
    }

    public LinearLayout getCastContainer()
    {
        return castContainer;
    }

    public LinearLayout getCrewContainer()
    {
        return crewContainer;
    }
}