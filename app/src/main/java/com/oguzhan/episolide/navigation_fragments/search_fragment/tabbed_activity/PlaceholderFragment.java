package com.oguzhan.episolide.navigation_fragments.search_fragment.tabbed_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oguzhan.episolide.details.media.MediaDetailActivity;
import com.oguzhan.episolide.search_results.PageManager;
import com.oguzhan.episolide.details.person.PersonDetailActivity;
import com.oguzhan.episolide.R;
import com.oguzhan.episolide.utils.Statics;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class PlaceholderFragment extends Fragment
{

    public static final String DETAIL_ACTIVITY_DATA_TAG = "json_object";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private final String PAGE_TEMPLATE = "&page=%d";

    private PageManager pageManager;
    private String searchURL;
    private SearchResultsAdapter resultsAdapter;


    private TextView pageTextview;

    public static PlaceholderFragment newInstance(int index, Bundle bundle)
    {
        PlaceholderFragment fragment = new PlaceholderFragment();

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
        final String mediaType = getCurrentMediaType(pageIndex);

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
            resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    try
                    {
                        JSONObject objectToGo = results.getJSONObject(position);
                        if (mediaType.equals(Statics.PersonKeys.MEDIA_TYPE))
                        {
                            goPersonDetailsActivity(objectToGo);

                        } else if (mediaType.equals(Statics.MovieKeys.MEDIA_TYPE))
                        {
                            goMovieDetailsActivity(objectToGo);
                        } else
                        {
                            goTvShowsDetailsActivity(objectToGo);
                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        initilizePaginationButtonsListeners(view);
    }

    @NotNull
    private String getCurrentMediaType(int pageIndex)
    {
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
        return mediaType;
    }


    private void goPersonDetailsActivity(JSONObject jsonObject)
    {

        Intent intent = new Intent(getContext(), PersonDetailActivity.class);
        intent.putExtra(DETAIL_ACTIVITY_DATA_TAG, jsonObject.toString());
        startActivity(intent);

    }

    private void goMovieDetailsActivity(JSONObject jsonObject)
    {

        Intent intent = new Intent(getContext(), MediaDetailActivity.class);
        intent.putExtra(DETAIL_ACTIVITY_DATA_TAG, jsonObject.toString());
        intent.putExtra(MediaDetailActivity.DETAIL_TYPE_TAG, MediaDetailActivity.MOVIE_TYPE_TAG);
        startActivity(intent);
    }

    private void goTvShowsDetailsActivity(JSONObject jsonObject)
    {

        Intent intent = new Intent(getContext(), MediaDetailActivity.class);
        intent.putExtra(DETAIL_ACTIVITY_DATA_TAG, jsonObject.toString());
        intent.putExtra(MediaDetailActivity.DETAIL_TYPE_TAG, MediaDetailActivity.TV_TYPE_TAG);
        startActivity(intent);

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