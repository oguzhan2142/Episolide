package com.oguzhan.episolide.ui.tabbed_activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.oguzhan.episolide.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{

    private final int TAB_AMOUNT = 3;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.movies_text, R.string.tvshows_text, R.string.people_text};
    private final Context mContext;

    private String[] urls;
    private String[] datas;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Bundle bundle)
    {
        super(fm);
        mContext = context;
        urls = bundle.getStringArray("urls");
        datas = bundle.getStringArray("datas");

    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Bundle bundle = new Bundle();
        bundle.putString("url", urls[position]);
        bundle.putString("data", datas[position]);

        return PlaceholderFragment.newInstance(position + 1, bundle);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {

        String pureTitle = mContext.getResources().getString(TAB_TITLES[position]);
        String title = pureTitle;

        try
        {
            JSONObject data = new JSONObject(datas[position]);
            int totalResults = data.getInt("total_results");
            title = String.format(Locale.US, "%s (%d)", pureTitle, totalResults);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return title;
    }

    @Override
    public int getCount()
    {
        // Show 2 total pages.
        return TAB_AMOUNT;
    }
}