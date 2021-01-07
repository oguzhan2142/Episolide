package com.oguzhan.episolide.details.person;

import android.os.AsyncTask;
import android.view.View;

import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class DetailTask extends AsyncTask<Integer, Void, JSONObject>
{

    private final String PERSON_DETAIL_TEMPLATE = "https://api.themoviedb.org/3/person/%d?api_key=ee637fe7f604d38049e71cb513a8a04d";
    private WeakReference<PersonDetailActivity> personDetailActivity;

    public DetailTask(PersonDetailActivity personDetailActivity)
    {
        this.personDetailActivity = new WeakReference<>(personDetailActivity);
    }

    @Override
    protected JSONObject doInBackground(Integer... integers)
    {
        int id = integers[0];
        String url = String.format(Locale.US, PERSON_DETAIL_TEMPLATE, id);
        return JsonReader.readJsonFromUrl(url);

    }

    @Override
    protected void onPostExecute(JSONObject jsonObject)
    {
        super.onPostExecute(jsonObject);


        try
        {
            String name = jsonObject.getString("name");
            String birthday = jsonObject.getString("birthday");
            String deadDay = jsonObject.getString("deathday");
            String placeOfBirth = jsonObject.getString("place_of_birth");
            String department = jsonObject.getString("known_for_department");
            String biography = jsonObject.getString("biography");


            if (biography.equals(""))
                personDetailActivity.get().getBiographyTextview().setVisibility(View.GONE);

            personDetailActivity.get().getNameView().setText(name);
            personDetailActivity.get().getBirthdayView().setText(Utils.ConvertDateAsFormatted(birthday));
            personDetailActivity.get().getBirthPlaceView().setText(placeOfBirth);
            personDetailActivity.get().getDepartmentView().setText(department);
            personDetailActivity.get().getBiographyTextview().setText(biography);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }
}
