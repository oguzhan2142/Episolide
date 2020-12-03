package com.oguzhan.episolide.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.PageManager;
import com.oguzhan.episolide.ui.SearchResultsAdapter;
import com.oguzhan.episolide.utils.Statics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment
{

    private static final String ARG_SECTION_NUMBER = "section_number";

    private final String PAGE_TEMPLATE = "&page=%d";

    private PageManager pageManager;
    private String searchURL;
    private ListView resultsListView;
    private JSONArray results;
    SearchResultsAdapter resultsAdapter;

    public static PlaceholderFragment newInstance(int index, Bundle bundle)
    {
        PlaceholderFragment fragment = new PlaceholderFragment();
//        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_search_results_tabbed, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        resultsListView = getView().findViewById(R.id.search_results_listview);

        int pageIndex = 1;

//        SearchResultsAdapter.MediaType mediaType = SearchResultsAdapter.MediaType.MOVIE;
        String mediaType = Statics.MovieKeys.MEDIA_TYPE;
        if (getArguments() != null)
        {
            pageIndex = getArguments().getInt(ARG_SECTION_NUMBER);
            // hangi tabdan geliyoruz indexi
        }
        switch (pageIndex)
        {
            case 1:
                mediaType = Statics.MovieKeys.MEDIA_TYPE;
                break;
            case 2:
                mediaType = Statics.TvShowKeys.MEDIA_TYPE;
                break;
            case 3:
                mediaType = Statics.PersonKeys.MEDIA_TYPE;
        }

        String url = getArguments().getString("url");
        String data = getArguments().getString("data");


        try
        {
            JSONObject resultsJsonRoot = new JSONObject(data);
            pageManager = new PageManager(1, resultsJsonRoot.getInt("total_results"));
            results = resultsJsonRoot.getJSONArray("results");
            resultsAdapter = new SearchResultsAdapter(getContext(), results, mediaType);
            resultsListView.setAdapter(resultsAdapter);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }




    }

}