package com.oguzhan.episolide.details.movie;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.oguzhan.episolide.utils.JsonReader;
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
//            String nameOfCollection = belongs_to_collection.getString("name");
//            String posterPathOfCollection = belongs_to_collection.getString("poster_path");
            int idOfCollection = belongs_to_collection.getInt("id");

            new CollectionTask(movieDetailActivity.get()).execute(idOfCollection);


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
            List<ProductionInfo> productionCompanies = new ArrayList<>();
            for (int i = 0; i < production_companies.length(); i++)
            {
                JSONObject company = production_companies.getJSONObject(i);
                String productionName = company.getString("name");
                String imagePath = company.getString("logo_path");
                String imageURL = Statics.BASE_IMAGE_URL + Statics.LOGO_SIZES[1] + imagePath;

                productionCompanies.add(new ProductionInfo(productionName, imageURL));
            }

            JSONArray production_countries = root.getJSONArray("production_countries");
            List<ProductionInfo> productionCountries = new ArrayList<>();


            for (int i = 0; i < production_countries.length(); i++)
            {
                JSONObject country = production_countries.getJSONObject(i);
                String countryName = country.getString("name");
                String countryCode = country.getString("iso_3166_1");
                String imageURL = String.format(Locale.US, Statics.COUNTRY_IMAGES_TEMPLATE, countryCode, 32);

                ProductionInfo info = new ProductionInfo(countryName, imageURL);
                productionCountries.add(info);
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


                    for (int i = 0; i < productionCountries.size(); i++)
                    {
                        LinearLayout content = movieDetailActivity.get().createProductionItemLayout(productionCountries.get(i).name, productionCountries.get(i).imageURL);
                        movieDetailActivity.get().getProductionCountries().addView(content);
                    }

                    for (int i = 0; i < productionCompanies.size(); i++)
                    {
                        LinearLayout content = movieDetailActivity.get().createProductionItemLayout(productionCompanies.get(i).name, productionCompanies.get(i).imageURL);
                        movieDetailActivity.get().getProductionCompanies().addView(content);
                    }

                }
            });
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {


        super.onPostExecute(aVoid);
    }
}
