package com.oguzhan.episolide.details.person;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class ImagesTask extends AsyncTask<Integer, Void, Void>
{
    private final String URL_TEMPLATE = "https://api.themoviedb.org/3/person/%d/images?api_key=ee637fe7f604d38049e71cb513a8a04d";
    private WeakReference<PersonDetailActivity> personDetailActivity;


    public ImagesTask(PersonDetailActivity personDetailActivity)
    {
        this.personDetailActivity = new WeakReference<>(personDetailActivity);
    }

    @Override
    protected Void doInBackground(Integer... integers)
    {
        int id = integers[0];

        String url = String.format(Locale.US, URL_TEMPLATE, id);

        JSONObject jsonObject = JsonReader.readJsonFromUrl(url);
        try
        {
            JSONArray profiles = jsonObject.getJSONArray("profiles");
            for (int i = 0; i < profiles.length(); i++)
            {

                String path = profiles.getJSONObject(i).getString("file_path");
                String profileImageURL = Statics.BASE_IMAGE_URL + Statics.PROFILE_SIZES[0] + path;
                ImageView view = personDetailActivity.get().createCardImageView();
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Picasso.get().load(profileImageURL).into(view);
                        personDetailActivity.get().getProfileImagesLinearLayout().addView(view);
                    }
                });



            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }


}
