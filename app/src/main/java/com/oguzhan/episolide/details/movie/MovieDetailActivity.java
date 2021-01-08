package com.oguzhan.episolide.details.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.navigation_fragments.search_fragment.tabbed_activity.PlaceholderFragment;
import com.oguzhan.episolide.utils.ExpandableTextView;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String WEB_ACTIVITY_INTENT_TAG = "URL";


    private ExpandableTextView overview;

    private LinearLayout productionCountries;
    private LinearLayout productionCompanies;


    private ImageView collectionPoster;
    private TextView collectionOverview;

    private LinearLayout collectionScrolviewContainer;
    private LinearLayout castContainer;

    private TextView movieTitleTextView;
    private TextView yaerTextview;
    private TextView taglineTextview;
    private TextView runtimeTextview;
    private TextView imdbTextview;
    private TextView exploreMoreTextview;
    private TextView revenueTextview;
    private TextView budgetTextview;
    private TextView statusTextview;
    private TextView voteAverageTextview;
    private TextView spokenLanguagesTextview;
    private TextView homepageTextview;
    private TextView collectionNameTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_movie_activity);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        productionCountries = findViewById(R.id.production_countries);
//        productionCompanies = findViewById(R.id.production_companies);
        productionCompanies = findViewById(R.id.production_com);
        overview = findViewById(R.id.overview_text);


        castContainer = findViewById(R.id.cast_linearlayout);


        collectionOverview = findViewById(R.id.collection_overview);
        collectionPoster = findViewById(R.id.collection_poster);

        collectionScrolviewContainer = findViewById(R.id.collection_scrollview_container);
        movieTitleTextView = findViewById(R.id.movie_name_text);
        yaerTextview = findViewById(R.id.year_text);
        taglineTextview = findViewById(R.id.tagline_text);
        runtimeTextview = findViewById(R.id.runtime_textview);
        imdbTextview = findViewById(R.id.imdb_textview);
        exploreMoreTextview = findViewById(R.id.explore_more_textview);
        budgetTextview = findViewById(R.id.budget_textview);
        revenueTextview = findViewById(R.id.revenue_textview);
        statusTextview = findViewById(R.id.status_textview);
        voteAverageTextview = findViewById(R.id.vote_avarage_textview);
        spokenLanguagesTextview = findViewById(R.id.spoken_language_textview);
        homepageTextview = findViewById(R.id.homepage_textview);
        collectionNameTextview = findViewById(R.id.collection_name_textview);

        String data = getIntent().getStringExtra(PlaceholderFragment.DETAIL_ACTIVITY_DATA_TAG);

        homepageTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(homepageTextview.getText().toString()));
                startActivity(i);
            }
        });

        exploreMoreTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Explore More");
            }
        });


        try {
            JSONObject jsonData = new JSONObject(data);


            String originalTitle = jsonData.getString("title");

            movieTitleTextView.setText(originalTitle);

            String backdropPath = jsonData.getString("backdrop_path");
            String backdropURL = Statics.BASE_IMAGE_URL + Statics.BACKDROP_SIZES[1] + backdropPath;
            ImageView backdropImageView = findViewById(R.id.expandedImage);
            Picasso.get().load(backdropURL).into(backdropImageView);

            int id = jsonData.getInt("id");
            new MovieDetailTask(this).execute(id);
            new MovieCreditsTask(this).execute(id);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public LinearLayout createCollectionItemLayout(String year, String imageUrl) {
        LinearLayout content = (LinearLayout) getLayoutInflater().inflate(R.layout.collection_item_layout, null);
        TextView title = content.findViewById(R.id.title_textview);
        ImageView posterImage = content.findViewById(R.id.collection_poster);

        title.setText(year);
        Picasso.get().load(imageUrl).into(posterImage);
        return content;
    }


    public LinearLayout createPersonItemLayout(String name, String subtitle, String imageURL) {

        LinearLayout content = (LinearLayout) getLayoutInflater().inflate(R.layout.person_item_layout, null);
        TextView nameText = content.findViewById(R.id.person_item_name);
        TextView subtitleText = content.findViewById(R.id.person_item_character_name);
        ImageView posterImage = content.findViewById(R.id.person_item_image);

        nameText.setText(name);
        subtitleText.setText(subtitle);
        Picasso.get().load(imageURL).into(posterImage);
        return content;

    }

    public LinearLayout createListItemLayout(String name, String imageURL) {
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

    public LinearLayout createListItemLayout(String name, String imageURL, int width, int height) {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.production_listitem, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 10);

        TextView nameview = linearLayout.findViewById(R.id.production_name);
        ImageView imageview = linearLayout.findViewById(R.id.production_image);
        imageview.getLayoutParams().height = height;
        imageview.getLayoutParams().width = width;
        nameview.setText(name);
        Picasso.get().load(imageURL).into(imageview);


        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    public void addContentToCollectionContainer(LinearLayout content) {
        collectionScrolviewContainer.addView(content);
    }

    public void makeHomePageLayoutGone() {
        findViewById(R.id.homepage_layout).setVisibility(View.GONE);
    }

    public void makeCollectionLayoutGone() {
        findViewById(R.id.belongs_to_collection_layout).setVisibility(View.GONE);
    }

    public LinearLayout createScrollviewContent(String posterURL, String nameOfCollection, String releaseDate) {
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public LinearLayout getProductionCountries() {
        return productionCountries;
    }

    public ExpandableTextView getOverview() {
        return overview;
    }


    public ImageView getCollectionPoster() {
        return collectionPoster;
    }

    public TextView getCollectionOverview() {
        return collectionOverview;
    }


    public LinearLayout getProductionCompanies() {
        return productionCompanies;
    }

    public LinearLayout getCastContainer() {
        return castContainer;
    }


    public TextView getRuntimeTextview() {
        return runtimeTextview;
    }

    public TextView getYaerTextview() {
        return yaerTextview;
    }

    public TextView getTaglineTextview() {
        return taglineTextview;
    }

    public TextView getImdbTextview() {
        return imdbTextview;
    }

    public TextView getRevenueTextview() {
        return revenueTextview;
    }

    public TextView getBudgetTextview() {
        return budgetTextview;
    }

    public TextView getStatusTextview() {
        return statusTextview;
    }

    public TextView getVoteAverageTextview() {
        return voteAverageTextview;
    }

    public TextView getSpokenLanguagesTextview() {
        return spokenLanguagesTextview;
    }

    public TextView getHomepageTextview() {
        return homepageTextview;
    }

    public TextView getCollectionNameTextview() {
        return collectionNameTextview;
    }
}