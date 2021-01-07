package com.oguzhan.episolide.details.movie;

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
import java.util.Locale;

public class MovieCreditsTask extends AsyncTask<Integer, Void, Void> {

    private WeakReference<MovieDetailActivity> movieDetailActivity;
    Info[] crewInfos;
    Info[] castInfos;
    private final int MAX_TOP_CAST_AMOUNT = 9;

    public MovieCreditsTask(MovieDetailActivity movieDetailActivity) {
        this.movieDetailActivity = new WeakReference<>(movieDetailActivity);

    }

    @Override
    protected Void doInBackground(Integer... integers) {

        int id = integers[0];
        String url = String.format(Locale.US, Statics.MOVIE_CREDITS_TEMPLATE, id);

        JSONObject root = JsonReader.readJsonFromUrl(url);

        if (root == null)
            return null;


        try {

            JSONArray castArray = root.getJSONArray("cast");
            JSONArray crewArray = root.getJSONArray("crew");


            crewInfos = new Info[crewArray.length()];
            castInfos = new Info[castArray.length()];

            for (int i = 0; i < crewArray.length(); i++) {
                JSONObject crewJson = crewArray.getJSONObject(i);

                String name = crewJson.getString("name");
                String profilePath = crewJson.getString("profile_path");
                String job = crewJson.getString("job");

                String imageURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + profilePath;

                crewInfos[i] = new Info(name, job, imageURL);


            }

            for (int i = 0; i < castArray.length(); i++) {

                JSONObject cast = castArray.getJSONObject(i);

                String name = cast.getString("name");
                String characterName = cast.getString("character");
                String profilePath = cast.getString("profile_path");

                String imageURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + profilePath;
                castInfos[i] = new Info(name, characterName, imageURL);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                int topCastAmount = Math.min(castInfos.length, MAX_TOP_CAST_AMOUNT);
                for (int i = 0; i < topCastAmount; i++) {
                    Info info = castInfos[i];
                    LinearLayout content = movieDetailActivity.get()
                            .createPersonItemLayout(info.name, info.subtext, info.posterURL);
                    movieDetailActivity.get().getCastContainer().addView(content);
                }

            }
        });

    }

    private static class Info {
        public String name;
        public String subtext;
        public String posterURL;

        public Info(String name, String subtext, String posterURL) {
            this.name = name;
            this.subtext = subtext;
            this.posterURL = posterURL;
        }
    }
}
