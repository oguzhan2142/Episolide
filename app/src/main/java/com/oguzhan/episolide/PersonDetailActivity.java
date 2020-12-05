package com.oguzhan.episolide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;

import com.oguzhan.episolide.ui.tabbed_activity.PlaceholderFragment;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonDetailActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_person_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        String data = getIntent().getStringExtra(PlaceholderFragment.DETAIL_ACTIVITY_DATA_TAG);
        try
        {
            JSONObject jsonData = new JSONObject(data);

            String profilePath = jsonData.getString("profile_path");
            int id = jsonData.getInt("id");







        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onNavigateUp()
    {
        onBackPressed();
        return true;
    }
}