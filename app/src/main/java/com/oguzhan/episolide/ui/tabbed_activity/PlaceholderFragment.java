package com.oguzhan.episolide.ui.tabbed_activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oguzhan.episolide.PageManager;
import com.oguzhan.episolide.R;
import com.oguzhan.episolide.ui.SearchResultsAdapter;
import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.Statics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment
{

    private static final String ARG_SECTION_NUMBER = "section_number";

    private final String PAGE_TEMPLATE = "&page=%d";

    private PageManager pageManager;
    private String searchURL;
    SearchResultsAdapter resultsAdapter;


    private TextView pageTextview;

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
        ListView resultsListView = getView().findViewById(R.id.search_results_listview);
        pageTextview = view.findViewById(R.id.page_text_search_result);



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

        searchURL = getArguments().getString("url");
        String data = getArguments().getString("data");


        try
        {
            JSONObject resultsJsonRoot = new JSONObject(data);
            pageManager = new PageManager(1, resultsJsonRoot.getInt("total_pages"));
            pageTextview.setText(pageManager.toString());
            JSONArray results = resultsJsonRoot.getJSONArray("results");
            resultsAdapter = new SearchResultsAdapter(getContext(), results, mediaType);
            resultsListView.setAdapter(resultsAdapter);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        initilizePaginationButtonsListeners(view);
    }

    private void initilizePaginationButtonsListeners(View view)
    {


        view.findViewById(R.id.search_result_next_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pageManager.nextPage();
                String nextPageSearchUrl = searchURL +
                        String.format(Locale.US, PAGE_TEMPLATE, pageManager.getCurrentPage());
                new PageTask(resultsAdapter).execute(nextPageSearchUrl);
                pageTextview.setText(pageManager.toString());
            }
        });

        view.findViewById(R.id.search_result_before_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pageManager.previousPage();
                String previousPageSearchUrl = searchURL +
                        String.format(Locale.US, PAGE_TEMPLATE, pageManager.getCurrentPage());
                new PageTask(resultsAdapter).execute(previousPageSearchUrl);
                pageTextview.setText(pageManager.toString());
            }
        });
    }



}