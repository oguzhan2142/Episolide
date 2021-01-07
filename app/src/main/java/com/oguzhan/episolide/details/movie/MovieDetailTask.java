package com.oguzhan.episolide.details.movie;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.Statics;
import com.oguzhan.episolide.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieDetailTask extends AsyncTask<Integer, Void, Void> {

    private final String MOVIE_DETAIL_URL_TEMPLATE = "https://api.themoviedb.org/3/movie/%d?api_key=ee637fe7f604d38049e71cb513a8a04d";
    private static final int COUNTRY_IMAGE_SIZE = 64;

    private WeakReference<MovieDetailActivity> movieDetailActivity;


    public MovieDetailTask(MovieDetailActivity movieDetailActivity) {
        this.movieDetailActivity = new WeakReference<>(movieDetailActivity);
    }

    @Override
    protected Void doInBackground(Integer... integers) {

        int id = integers[0];
        String url = String.format(Locale.US, MOVIE_DETAIL_URL_TEMPLATE, id);

        JSONObject root = JsonReader.readJsonFromUrl(url);
        if (root == null) {
            return null;
        }
        try {


            JSONArray genresJsonArray = root.getJSONArray("genres");
            StringBuilder genreStringBuilder = new StringBuilder();
            for (int i = 0; i < genresJsonArray.length(); i++) {
                String nameOfGenre = genresJsonArray.getJSONObject(i).getString("name");
                genreStringBuilder.append(nameOfGenre).append(", ");
            }

            int runtime = root.getInt("runtime");
            String homePageUrl = root.getString("homepage");
            String overview = root.getString("overview");
            String status = root.getString("status");
            String releaseDate = root.getString("release_date");
            double voteAvarage = root.getDouble("vote_average");
            int voteCount = root.getInt("vote_count");
            String tagline = root.getString("tagline");
            int budged = root.getInt("budget");
            int revenue = root.getInt("revenue");

            StringBuilder spokenLanguagesBuilder = new StringBuilder();
            JSONArray spokenLanguagesArray = root.getJSONArray("spoken_languages");
            for (int i = 0; i < spokenLanguagesArray.length(); i++) {
                JSONObject languageObject = spokenLanguagesArray.getJSONObject(i);
                String name = languageObject.getString("english_name");
                spokenLanguagesBuilder.append(name);
                if (i != spokenLanguagesArray.length() - 1)
                    spokenLanguagesBuilder.append(", ");
            }

            JSONArray production_companies = root.getJSONArray("production_companies");
            List<ProductionInfo> productionCompanies = new ArrayList<>();
            for (int i = 0; i < production_companies.length(); i++) {
                JSONObject company = production_companies.getJSONObject(i);
                String productionName = company.getString("name");
                String imagePath = company.getString("logo_path");
                String imageURL = Statics.BASE_IMAGE_URL + Statics.LOGO_SIZES[1] + imagePath;

                productionCompanies.add(new ProductionInfo(productionName, imageURL));
            }

            JSONArray production_countries = root.getJSONArray("production_countries");
            List<ProductionInfo> productionCountries = new ArrayList<>();


            for (int i = 0; i < production_countries.length(); i++) {
                JSONObject country = production_countries.getJSONObject(i);
                String countryName = country.getString("name");
                String countryCode = country.getString("iso_3166_1");
                String imageURL = String.format(Locale.US, Statics.COUNTRY_IMAGES_TEMPLATE, countryCode, COUNTRY_IMAGE_SIZE);

                ProductionInfo info = new ProductionInfo(countryName, imageURL);
                productionCountries.add(info);
            }


            JSONObject belongsToObject = root.getJSONObject("belongs_to_collection");
            String collectionName = belongsToObject.getString("name");
            int collectionID = belongsToObject.getInt("id");


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new CollectionTask(movieDetailActivity.get()).execute(collectionID);
                    NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
                    moneyFormatter.setMaximumFractionDigits(0);

                    movieDetailActivity.get().getOverview().setText(overview);
                    movieDetailActivity.get().getYaerTextview().setText(releaseDate.substring(0, 4));
                    movieDetailActivity.get().getTaglineTextview().setText(tagline);
                    movieDetailActivity.get().getImdbTextview().setText(String.valueOf(voteAvarage));
                    movieDetailActivity.get().getRuntimeTextview().setText(String.format(Locale.ENGLISH, "%d min", runtime));
                    movieDetailActivity.get().getBudgetTextview().setText(moneyFormatter.format(budged));
                    movieDetailActivity.get().getRevenueTextview().setText(moneyFormatter.format(revenue));
                    movieDetailActivity.get().getStatusTextview().setText(
                            String.format(Locale.ENGLISH, "%s (%s)", status, Utils.ConvertDateAsFormatted(releaseDate)));
                    movieDetailActivity.get().getVoteAverageTextview().setText(
                            String.format(Locale.ENGLISH, "%.1f from %d votes", voteAvarage, voteCount));
                    movieDetailActivity.get().getSpokenLanguagesTextview().setText(spokenLanguagesBuilder.toString());
                    if (!homePageUrl.equals(""))
                        movieDetailActivity.get().getHomepageTextview().setText(homePageUrl);
                    else
                        movieDetailActivity.get().makeHomePageLayoutGone();

                    if(belongsToObject != null)
                        movieDetailActivity.get().getCollectionNameTextview().setText(collectionName);
                    else
                        movieDetailActivity.get().makeCollectionLayoutGone();

                    for (int i = 0; i < productionCountries.size(); i++) {
                        LinearLayout content = movieDetailActivity.get().createListItemLayout(
                                productionCountries.get(i).name, productionCountries.get(i).imageURL);
                        movieDetailActivity.get().getProductionCountries().addView(content);
                    }

                    for (int i = 0; i < productionCompanies.size(); i++) {
                        LinearLayout content = movieDetailActivity.get().createListItemLayout(
                                productionCompanies.get(i).name, productionCompanies.get(i).imageURL, 50, 50);
                        movieDetailActivity.get().getProductionCompanies().addView(content);
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
