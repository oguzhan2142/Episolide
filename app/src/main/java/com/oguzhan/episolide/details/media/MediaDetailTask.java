package com.oguzhan.episolide.details.media;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.Statics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MediaDetailTask extends AsyncTask<Integer, Void, Void>
{

    private final String MOVIE_DETAIL_URL_TEMPLATE = "https://api.themoviedb.org/3/movie/%d?api_key=ee637fe7f604d38049e71cb513a8a04d";
    private final String TV_DETAIL_URL_TEMPLATE = "https://api.themoviedb.org/3/tv/%d?api_key=ee637fe7f604d38049e71cb513a8a04d";

    private static final int COUNTRY_IMAGE_SIZE = 64;
    final String DETAIL_TYPE;

    // Common Properties
    private WeakReference<MediaDetailActivity> movieDetailActivity;
    private String homePageUrl;
    private String overview;
    private String status;
    private String originalLanguage;
    private double voteAverage;
    private int voteCount;
    private String tagline;
    private String genre;
    private String spokenLanguages;
    private ProductionInfo[] productionCompanies;
    private ProductionInfo[] productionCountries;
    private String releaseDate;

    // Movie Only Properties
    private int collectionID = -1;
    private int budged;
    private int revenue;
    private int runtime; // yari-ortak

    //Tv Only Properties

    private String episodeRuntimeText; // yari-ortak
    private boolean inProduction;
    private int numberOfEpisodes;
    private int numberOfSeasons;
    private String originCountries;
    private HashMap<String, String>[] networks;

    public MediaDetailTask(MediaDetailActivity mediaDetailActivity, String detailType)
    {
        this.movieDetailActivity = new WeakReference<>(mediaDetailActivity);
        this.DETAIL_TYPE = detailType;
    }


    private void initCommonProperties(JSONObject root) throws JSONException
    {
        homePageUrl = root.getString("homepage");
        overview = root.getString("overview");
        status = root.getString("status");
        originalLanguage = root.getString("original_language");
        voteAverage = root.getDouble("vote_average");
        voteCount = root.getInt("vote_count");
        tagline = root.getString("tagline");

        JSONArray genresJsonArray = root.getJSONArray("genres");
        StringBuilder genreStringBuilder = new StringBuilder();
        for (int i = 0; i < genresJsonArray.length(); i++)
        {
            String nameOfGenre = genresJsonArray.getJSONObject(i).getString("name");
            genreStringBuilder.append(nameOfGenre).append(", ");
        }
        genre = genreStringBuilder.toString();

        StringBuilder spokenLanguagesBuilder = new StringBuilder();
        JSONArray spokenLanguagesArray = root.getJSONArray("spoken_languages");
        for (int i = 0; i < spokenLanguagesArray.length(); i++)
        {
            JSONObject languageObject = spokenLanguagesArray.getJSONObject(i);
            String name = languageObject.getString("english_name");
            spokenLanguagesBuilder.append(name);
            if (i != spokenLanguagesArray.length() - 1)
                spokenLanguagesBuilder.append(", ");
        }
        spokenLanguages = spokenLanguagesBuilder.toString();

        JSONArray production_companies = root.getJSONArray("production_companies");
        List<ProductionInfo> productionCompanies = new ArrayList<>();
        this.productionCompanies = new ProductionInfo[production_companies.length()];

        for (int i = 0; i < production_companies.length(); i++)
        {
            JSONObject company = production_companies.getJSONObject(i);
            String productionName = company.getString("name");
            String imagePath = company.getString("logo_path");
            String imageURL = Statics.BASE_IMAGE_URL + Statics.LOGO_SIZES[1] + imagePath;
            this.productionCompanies[i] = new ProductionInfo(productionName, imageURL);
        }


        JSONArray production_countries = root.getJSONArray("production_countries");

        this.productionCountries = new ProductionInfo[production_countries.length()];
        for (int i = 0; i < production_countries.length(); i++)
        {
            JSONObject country = production_countries.getJSONObject(i);
            String countryName = country.getString("name");
            String countryCode = country.getString("iso_3166_1");
            String imageURL = String.format(Locale.US, Statics.COUNTRY_IMAGES_TEMPLATE, countryCode, COUNTRY_IMAGE_SIZE);
            ProductionInfo info = new ProductionInfo(countryName, imageURL);
            this.productionCountries[i] = info;
        }

    }


    private void setCommonTextviews()
    {

        movieDetailActivity.get().getStatusTextview().setText(status);
        movieDetailActivity.get().getOverview().setText(overview);
        movieDetailActivity.get().getOriginalLanguageTextview().setText(originalLanguage);
        movieDetailActivity.get().getVoteAverageTextview().setText(
                String.format(Locale.ENGLISH, "%.1f from %d votes", voteAverage, voteCount));
        // IMDB puanini vote average olarak varsaydik
        movieDetailActivity.get().getImdbTextview().setText(String.valueOf(voteAverage));
        movieDetailActivity.get().getGenreTextview().setText(genre);
        movieDetailActivity.get().getTaglineTextview().setText(tagline);
        movieDetailActivity.get().getSpokenLanguagesTextview().setText(spokenLanguages);

        for (ProductionInfo productionCountry : productionCountries)
        {
            LinearLayout content = movieDetailActivity.get().createListItemLayout(
                    productionCountry.name, productionCountry.imageURL);
            movieDetailActivity.get().getProductionCountries().addView(content);
        }

        for (ProductionInfo productionCompany : productionCompanies)
        {
            LinearLayout content = movieDetailActivity.get().createListItemLayout(
                    productionCompany.name, productionCompany.imageURL, 50, 50);
            movieDetailActivity.get().getProductionCompanies().addView(content);
        }

        if (productionCompanies.length == 0)
            movieDetailActivity.get().makeGoneProductionCompaniesLayout();
    }


    private void initMovieProperties(JSONObject root) throws JSONException
    {
        Object belongsToObject = root.get("belongs_to_collection");
        if (!belongsToObject.toString().equals("null"))
            collectionID = ((JSONObject) belongsToObject).getInt("id");

        releaseDate = root.getString("release_date");
        budged = root.getInt("budget");
        revenue = root.getInt("revenue");
        runtime = root.getInt("runtime");
    }

    private void setMovieTextviews()
    {
        NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
        moneyFormatter.setMaximumFractionDigits(0);

        movieDetailActivity.get().getYaerTextview().setText(releaseDate.substring(0, 4));
        movieDetailActivity.get().getRuntimeTextview().setText(String.format(Locale.ENGLISH, "%d min", runtime));
        movieDetailActivity.get().getBudgetTextview().setText(moneyFormatter.format(budged));
        movieDetailActivity.get().getRevenueTextview().setText(moneyFormatter.format(revenue));

        new Handler(Looper.getMainLooper()).post(() ->
        {

            if (collectionID != -1)
                new CollectionTask(movieDetailActivity.get()).execute(collectionID);
            else
                movieDetailActivity.get().makeCollectionLayoutGone();
        });
    }

    private void initTvProperties(JSONObject root) throws JSONException
    {
        String firstAirDate = root.getString("first_air_date");
        String lastAirDate = root.getString("last_air_date");
        releaseDate = String.format(Locale.ENGLISH, "%s - %s",
                firstAirDate.substring(0, 4), lastAirDate.substring(0, 4));

        JSONArray runtimes = root.getJSONArray("episode_run_time");
        float totalRuntime = 0;
        for (int i = 0; i < runtimes.length(); i++)
        {
            totalRuntime += runtimes.getInt(i);
        }
        int episodeRuntime = (int) (totalRuntime / runtimes.length());
        if (runtimes.length() != 1)
            this.episodeRuntimeText = String.format(Locale.ENGLISH, "avg. %d min", episodeRuntime);
        else
            this.episodeRuntimeText = String.format(Locale.ENGLISH, "%d min", episodeRuntime);


        this.inProduction = root.getBoolean("in_production");
        this.numberOfEpisodes = root.getInt("number_of_episodes");
        this.numberOfSeasons = root.getInt("number_of_seasons");

        JSONArray originCountriesJSONArray = root.getJSONArray("origin_country");
        this.originCountries = originCountriesJSONArray.toString()
                .replace("[", "")
                .replace("]", "");

        if (originCountries.endsWith(","))
            originCountries = originCountries.substring(0, originCountries.length() - 2);

        JSONArray networkArray = root.getJSONArray("networks");
        this.networks = new HashMap[networkArray.length()];
        for (int i = 0; i < networkArray.length(); i++)
        {
            JSONObject networkObject = networkArray.getJSONObject(i);
            networks[i] = new HashMap<>();
            networks[i].put("name", networkObject.getString("name"));
            networks[i].put("logo_path", networkObject.getString("logo_path"));
        }

    }

    private void setTvTextviews()
    {

        movieDetailActivity.get().getYaerTextview().setText(releaseDate);
        movieDetailActivity.get().getNumberOfEpisodeTextview().setText(String.valueOf(numberOfEpisodes));
        movieDetailActivity.get().getNumberOfSeasonTextview().setText(String.valueOf(numberOfSeasons));
        movieDetailActivity.get().getOriginCountriesTextview().setText(originCountries);
        movieDetailActivity.get().getRuntimeTextview().setText(episodeRuntimeText);
    }

    @Override
    protected Void doInBackground(Integer... integers)
    {

        int id = integers[0];
        String url = null;
        if (DETAIL_TYPE.equals(MediaDetailActivity.MOVIE_TYPE_TAG))
            url = String.format(Locale.US, MOVIE_DETAIL_URL_TEMPLATE, id);
        else if (DETAIL_TYPE.equals(MediaDetailActivity.TV_TYPE_TAG))
            url = String.format(Locale.US, TV_DETAIL_URL_TEMPLATE, id);

        if (url == null) return null;

        JSONObject root = JsonReader.readJsonFromUrl(url);

        if (root == null) return null;

        new Handler(Looper.getMainLooper()).post(() ->
        {
            try
            {
                if (homePageUrl != null)
                    movieDetailActivity.get().getHomepageTextview().setText(homePageUrl);
                else
                    movieDetailActivity.get().makeHomePageLayoutGone();

                initCommonProperties(root);
                setCommonTextviews();

                if (DETAIL_TYPE.equals(MediaDetailActivity.MOVIE_TYPE_TAG))
                {
                    initMovieProperties(root);
                    setMovieTextviews();
                    movieDetailActivity.get().makeGoneTvOnlyProperties();
                }
                if (DETAIL_TYPE.equals(MediaDetailActivity.TV_TYPE_TAG))
                {
                    initTvProperties(root);
                    setTvTextviews();
                    movieDetailActivity.get().makeGoneMoviesOnlyProperties();
                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        });


        return null;
    }
}
