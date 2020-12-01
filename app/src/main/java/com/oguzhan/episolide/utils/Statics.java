package com.oguzhan.episolide.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Statics
{
    public static final String ENGLISH = "en-US";

    public static String BASE_IMAGE_URL;




    public static String[] BACKDROP_SIZES;
    public static String[] POSTER_SIZES;
    public static String[] PROFILE_SIZES;


    public static void initilizeArrays() throws JSONException
    {

        String url = "https://api.themoviedb.org/3/configuration?api_key=ee637fe7f604d38049e71cb513a8a04d";
        JSONObject jsonObject = JsonReader.readJsonFromUrl(url);

        // images
        JSONObject images = Objects.requireNonNull(jsonObject).getJSONObject("images");

        // base_url
        BASE_IMAGE_URL = Objects.requireNonNull(images).get("secure_base_url").toString();

        // backdrop_sizes
        JSONArray backdropSizes = Objects.requireNonNull(images).getJSONArray("backdrop_sizes");
        BACKDROP_SIZES = new String[backdropSizes.length()];
        for (int i = 0; i < BACKDROP_SIZES.length; i++)
        {
            BACKDROP_SIZES[i] = backdropSizes.get(i).toString();
        }

        // poster_sizes
        JSONArray posterSizes = Objects.requireNonNull(images).getJSONArray("poster_sizes");
        POSTER_SIZES = new String[posterSizes.length()];
        for (int i = 0; i < BACKDROP_SIZES.length; i++)
        {
            POSTER_SIZES[i] = posterSizes.get(i).toString();
        }

        // profile_sizes
        JSONArray profileSizes = Objects.requireNonNull(images).getJSONArray("profile_sizes");
        PROFILE_SIZES = new String[profileSizes.length()];
        for (int i = 0; i < PROFILE_SIZES.length; i++)
        {
            PROFILE_SIZES[i] = profileSizes.get(i).toString();
        }
    }
}

