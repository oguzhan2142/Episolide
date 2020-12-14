package com.oguzhan.episolide.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Statics
{

    // API KEY ee637fe7f604d38049e71cb513a8a04d

    public static final String ENGLISH = "en-US";

    public static String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";


    public static String[] BACKDROP_SIZES = {"w300", "w780", "w1280", "original"};
    public static String[] POSTER_SIZES = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};
    public static String[] PROFILE_SIZES = {"w45", "w185", "h632", "original"};
    public static String[] LOGO_SIZES = {"w45", "w92", "w154", "w185", "w300", "w500", "original"};


    public static class SearchResultKeys
    {
        public static final String MEDIA_TYPE = "media_type";
    }

    public static class MovieKeys
    {
        public static final String NAME = "title";
        public static final String RELEASE_DATE = "release_date";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String MEDIA_TYPE = "movie";
        public static final String GENRE_IDS = "genre_ids";
        public static final String POSTER_PATH = "poster_path";
        public static final String BACKDROP_PATH = "backdrop_path";

    }

    public static class TvShowKeys
    {
        public static final String NAME = "name";
        public static final String RELEASE_DATE = "first_air_date";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String MEDIA_TYPE = "tv";
        public static final String GENRE_IDS = "genre_ids";
        public static final String POSTER_PATH = "poster_path";
        public static final String BACKDROP_PATH = "backdrop_path";
    }

    public static class PersonKeys
    {
        public static final String NAME = "name";
        public static final String MEDIA_TYPE = "person";
        public static final String POSTER_PATH = "profile_path";
        public static final String DEPARTMENT = "known_for_department";

    }


}

