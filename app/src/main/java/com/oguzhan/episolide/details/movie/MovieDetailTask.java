package com.oguzhan.episolide.details.movie;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.ListviewHeightCalculator;
import com.oguzhan.episolide.utils.Statics;
import com.oguzhan.episolide.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieDetailTask extends AsyncTask<Integer, Void, Void>
{

    private final String MOVIE_DETAIL_URL_TEMPLATE = "https://api.themoviedb.org/3/movie/%d?api_key=ee637fe7f604d38049e71cb513a8a04d";

    private WeakReference<MovieDetailActivity> movieDetailActivity;


    public MovieDetailTask(MovieDetailActivity movieDetailActivity)
    {
        this.movieDetailActivity = new WeakReference<>(movieDetailActivity);
    }

    @Override
    protected Void doInBackground(Integer... integers)
    {

        int id = integers[0];
        String url = String.format(Locale.US, MOVIE_DETAIL_URL_TEMPLATE, id);

        JSONObject root = JsonReader.readJsonFromUrl(url);
        try
        {
            //
            JSONObject belongs_to_collection = root.getJSONObject("belongs_to_collection");
            String nameOfCollection = belongs_to_collection.getString("name");
            String posterPathOfCollection = belongs_to_collection.getString("poster_path");
//            String backdropPathOfCollection = belongs_to_collection.getString("backdrop_path");
            //

            int budged = root.getInt("budget");
            String formattedBudged = Utils.ConvertBudgedAsFormatted(String.valueOf(budged));


            JSONArray genresJsonArray = root.getJSONArray("genres");

            StringBuilder genreStringBuilder = new StringBuilder();
            for (int i = 0; i < genresJsonArray.length(); i++)
            {
                String nameOfGenre = genresJsonArray.getJSONObject(i).getString("name");
                genreStringBuilder.append(nameOfGenre).append(", ");
            }


            String homePageUrl = root.getString("homepage");

            String overview = root.getString("overview");

            String posterPath = root.getString("poster_path");
            String posterURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + posterPath;


            JSONArray production_companies = root.getJSONArray("production_companies");
            List<ProductionCompany> productionCompanies = new ArrayList<>();
            for (int i = 0; i < production_companies.length(); i++)
            {
                JSONObject company = production_companies.getJSONObject(i);
                String productionName = company.getString("name");
                String productionLogoPath = company.getString("logo_path");
                productionCompanies.add(new ProductionCompany(productionName, productionLogoPath));
            }

            JSONArray production_countries = root.getJSONArray("production_countries");
            String[] countries = new String[production_countries.length()];
            for (int i = 0; i < production_countries.length(); i++)
            {
                JSONObject country = production_countries.getJSONObject(i);
                String countryName = country.getString("name");
                countries[i] = countryName;
            }


            String releaseDate = root.getString("release_date");

            double voteAvarage = root.getDouble("vote_average");


            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    Picasso.get().load(posterURL).into(movieDetailActivity.get().getPosterImageView());
                    movieDetailActivity.get().getOverview().setText(overview);
                    movieDetailActivity.get().getBudged().setText(formattedBudged);
                    movieDetailActivity.get().getGenres().setText(genreStringBuilder.toString());
                    movieDetailActivity.get().getReleaseDate().setText(Utils.ConvertDateAsFormatted(releaseDate));
                    movieDetailActivity.get().getVoteAverage().setText(String.valueOf(voteAvarage));
                    movieDetailActivity.get().getHomepageUrl().setText(homePageUrl);


                    ListAdapter adapter = new ArrayAdapter<String>(
                            movieDetailActivity.get().getBaseContext(), android.R.layout.simple_list_item_1, countries);
                    movieDetailActivity.get().getProductionCountries().setAdapter(adapter);
                    ListviewHeightCalculator.setListViewHeightBasedOnItems(movieDetailActivity.get().getProductionCountries());


                    ProductionCompaniesAdapter companiesAdapter = new ProductionCompaniesAdapter(
                            movieDetailActivity.get().getBaseContext(), productionCompanies);
                    movieDetailActivity.get().getProductionCompanies().setAdapter(companiesAdapter);
                    ListviewHeightCalculator.setListViewHeightBasedOnItems(movieDetailActivity.get().getProductionCompanies());
                }
            });
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        return null;
    }
}
