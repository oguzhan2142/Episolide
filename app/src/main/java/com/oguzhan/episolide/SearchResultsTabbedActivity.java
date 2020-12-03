package com.oguzhan.episolide;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.oguzhan.episolide.ui.SearchNav.results.SearchResultsAdapter;
import com.oguzhan.episolide.ui.SearchNav.results.SearchResultsCardsActivity;
import com.oguzhan.episolide.ui.main.SectionsPagerAdapter;

public class SearchResultsTabbedActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_tabbed);


        Intent intent = getIntent();


//        String url = intent.getExtras().getString("movie_url");
//        String results = intent.getExtras().getString("movie_results");

        Bundle bundle = intent.getExtras();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), bundle);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }
}