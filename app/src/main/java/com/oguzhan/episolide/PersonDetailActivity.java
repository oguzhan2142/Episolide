package com.oguzhan.episolide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oguzhan.episolide.ui.tabbed_activity.PlaceholderFragment;
import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

public class PersonDetailActivity extends AppCompatActivity
{

    private ImageView posterViev;
    private TextView nameView;
    private TextView departmentView;
    private TextView birthdayView;
    private TextView birthPlaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_person_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        posterViev = findViewById(R.id.profile_imageview);
        nameView = findViewById(R.id.name_textview);
        departmentView = findViewById(R.id.department_textview);
        birthdayView = findViewById(R.id.birthday_textview);
        birthPlaceView = findViewById(R.id.birth_place_textview);

        String data = getIntent().getStringExtra(PlaceholderFragment.DETAIL_ACTIVITY_DATA_TAG);
        try
        {
            JSONObject jsonData = new JSONObject(data);
            String profilePath = jsonData.getString(Statics.PersonKeys.POSTER_PATH);
            JSONArray knownForArray = jsonData.getJSONArray("known_for");


            LinearLayout layout = (LinearLayout) findViewById(R.id.known_for_linaerlayour);

            for (int i = 0; i < knownForArray.length() ; i++)
            {

                JSONObject knownFor = knownForArray.getJSONObject(i);
                String posterPath = knownFor.getString("poster_path");
                String posterURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + posterPath;

                ImageView imageView = new ImageView(this);
                imageView.setId(i);
                imageView.setPadding(2, 2, 2, 2);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                param.leftMargin = 4;
                param.rightMargin = 4;

                imageView.setLayoutParams(param);

                Picasso.get().load(posterURL).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.addView(imageView);
            }


            String profileImageURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + profilePath;

            Picasso.get().load(profileImageURL).into(posterViev);

            int id = jsonData.getInt("id");
            new DetailTask().execute(id);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



    private class DetailTask extends AsyncTask<Integer, Void, JSONObject>
    {

        private final String PERSON_DETAIL_TEMPLATE = "https://api.themoviedb.org/3/person/%d?api_key=ee637fe7f604d38049e71cb513a8a04d";


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


                nameView.setText(name);
                birthdayView.setText(birthday);
                birthPlaceView.setText(placeOfBirth);
                departmentView.setText(department);


            } catch (JSONException e)
            {
                e.printStackTrace();
            }


        }
    }
}