package com.oguzhan.episolide.details.media;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class CollectionTask extends AsyncTask<Integer, Void, Void> {

    private WeakReference<MediaDetailActivity> movieDetailActivity;

    public CollectionTask(MediaDetailActivity mediaDetailActivity) {
        this.movieDetailActivity = new WeakReference<>(mediaDetailActivity);
    }

    @Override
    protected Void doInBackground(Integer... integers) {

        int id = integers[0];


        String url = String.format(Locale.US, Statics.COLLECTION_URL_TEMPLATE, id);

        JSONObject root = JsonReader.readJsonFromUrl(url);

        try {
            String nameOfCollection = root.getString("name");
            String overview = root.getString("overview");
            String posterPath = root.getString("poster_path");
//            String backdropPath = root.getString("backdrop_path");

            JSONArray parts = root.getJSONArray("parts");

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    LinearLayout[] contents = new LinearLayout[parts.length()];
                    for (int i = 0; i < parts.length(); i++) {
                        try {
                            JSONObject part = parts.getJSONObject(i);

                            String partPosterPath = part.getString("poster_path");
                            String releaseDate = part.getString("release_date");
                            String year = releaseDate.substring(0, 4);
                            String URL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + partPosterPath;
                            LinearLayout content = movieDetailActivity.get()
                                    .createCollectionItemLayout(year, URL);
                            contents[i] = content;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    movieDetailActivity.get().getCollectionOverview().setText(overview);
                    movieDetailActivity.get().getCollectionNameTextview().setText(nameOfCollection);
                    String posterURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + posterPath;
                    Picasso.get().load(posterURL).into(movieDetailActivity.get().getCollectionPoster());

                    for (int i = 0; i < contents.length; i++) {
                        movieDetailActivity.get().addContentToCollectionContainer(contents[i]);
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
