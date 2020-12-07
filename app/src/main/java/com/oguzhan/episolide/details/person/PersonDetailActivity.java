package com.oguzhan.episolide.details.person;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.ui.tabbed_activity.PlaceholderFragment;
import com.oguzhan.episolide.utils.ExpandableTextView;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonDetailActivity extends AppCompatActivity
{

    private ImageView posterViev;
    private TextView nameView;
    private TextView departmentView;
    private TextView birthdayView;
    private TextView birthPlaceView;
    private ExpandableTextView biographyTextview;
    private LinearLayout profileImagesLinearLayout;
    private FrameLayout frameLayout;
    private ListView moviesListview;

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
        biographyTextview = findViewById(R.id.biography_expandabletext);
        profileImagesLinearLayout = findViewById(R.id.profile_images_linearlayout);
        frameLayout = findViewById(R.id.profile_image_containenr);
        moviesListview = findViewById(R.id.expandableListView);


        frameLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                frameLayout.setVisibility(FrameLayout.GONE);
            }
        });

        String data = getIntent().getStringExtra(PlaceholderFragment.DETAIL_ACTIVITY_DATA_TAG);
        try
        {
            JSONObject jsonData = new JSONObject(data);
            String profilePath = jsonData.getString(Statics.PersonKeys.POSTER_PATH);
            JSONArray knownForArray = jsonData.getJSONArray("known_for");
            LinearLayout knownLinearLayout = (LinearLayout) findViewById(R.id.known_for_linaerlayour);


            fiilKnownForImagesToLayout(knownForArray, knownLinearLayout);

            String profileImageURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + profilePath;
            Picasso.get().load(profileImageURL).into(posterViev);

            int id = jsonData.getInt("id");
            new DetailTask(this).execute(id);
            new ImagesTask(this).execute(id);


            new MediaCreditsTask(this).execute(id);


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void fiilKnownForImagesToLayout(JSONArray knownForArray, LinearLayout knownLinearLayout) throws JSONException
    {


        for (int i = 0; i < knownForArray.length(); i++)
        {

            JSONObject knownFor = knownForArray.getJSONObject(i);
            String posterPath = knownFor.getString("poster_path");
            String posterURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + posterPath;

            ImageView imageView = createCardImageView();
            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Drawable drawable = ((ImageView) v).getDrawable();
                    setDrawableForFullScreen(drawable);
                    frameLayout.setVisibility(FrameLayout.VISIBLE);
                }
            });
            Picasso.get().load(posterURL).into(imageView);
            knownLinearLayout.addView(imageView);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {

        boolean isFullScreenLayoutOpen = frameLayout.getVisibility() == View.VISIBLE;
        if (isFullScreenLayoutOpen)
            frameLayout.setVisibility(FrameLayout.GONE);
        else
            onBackPressed();


        return super.onSupportNavigateUp();
    }


    public void setDrawableForFullScreen(Drawable drawable)
    {
        ImageView imageview = findViewById(R.id.profile_image_view);
        imageview.setImageDrawable(drawable);
    }

    public ImageView createCardImageView()
    {
        ImageView imageView = new ImageView(this);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.card_width),
                (int) getResources().getDimension(R.dimen.card_height)

        );
        param.leftMargin = 1;
        param.rightMargin = 1;
        imageView.setLayoutParams(param);
        return imageView;
    }

    public FrameLayout getFrameLayout()
    {
        return frameLayout;
    }


    public ListView getMoviesListview()
    {
        return moviesListview;
    }

    public ImageView getPosterViev()
    {
        return posterViev;
    }

    public TextView getNameView()
    {
        return nameView;
    }

    public TextView getDepartmentView()
    {
        return departmentView;
    }

    public TextView getBirthdayView()
    {
        return birthdayView;
    }

    public TextView getBirthPlaceView()
    {
        return birthPlaceView;
    }

    public ExpandableTextView getBiographyTextview()
    {
        return biographyTextview;
    }

    public LinearLayout getProfileImagesLinearLayout()
    {
        return profileImagesLinearLayout;
    }
}