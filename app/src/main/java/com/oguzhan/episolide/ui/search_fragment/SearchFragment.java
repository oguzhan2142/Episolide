package com.oguzhan.episolide.ui.search_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.SearchResultsTabbedActivity;
import com.oguzhan.episolide.utils.Keyboard;
import com.oguzhan.episolide.utils.Statics;

import org.json.JSONArray;

public class SearchFragment extends Fragment
{

    public static final String PERSON_TAG = "PERSON";
    public static final String TV_SHOW_TAG = "TV_SHOW";
    public static final String MOVIE_TAG = "MOVIE";

    private final String MULTI_SEARCH_TEMPLATE = "https://api.themoviedb.org/3/search/multi?api_key=ee637fe7f604d38049e71cb513a8a04d&language=%s&query=%s&include_adult=true";
    private final String PEOPLE_SEARCH_TEMPLATE = "https://api.themoviedb.org/3/search/person?api_key=ee637fe7f604d38049e71cb513a8a04d&language=%s&query=%s";
    private final String TV_SHOW_SEARCH_TEMPLATE = "https://api.themoviedb.org/3/search/tv?api_key=ee637fe7f604d38049e71cb513a8a04d&language=%s&query=%s";
    private final String MOVIE_SEARCH_TEMPLATE = "https://api.themoviedb.org/3/search/movie?api_key=ee637fe7f604d38049e71cb513a8a04d&language=%s&query=%s";

    private EditText editText;
    private ImageButton search_btn;

    private boolean searching = false;
    private RecyclerView recyclerView;
    private JSONArray results;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {


        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        editText = getView().findViewById(R.id.search_edit_text);
        search_btn = getView().findViewById(R.id.search_btn);

        results = new JSONArray();

        recyclerView = view.findViewById(R.id.resycle_view);
        recyclerView.setAdapter(new RecyclerAdapter(getContext(), results));


        editText.setOnKeyListener(createSearchButtonEnterListener());
        editText.addTextChangedListener(createTextChangedListener());
        search_btn.setOnClickListener(createSearchButtonOnClickListener());


        super.onViewCreated(view, savedInstanceState);
    }

    private void doSearch()
    {
        if (searching)
            return;
        searching = true;
        String query = editText.getText().toString();

        // Burada search task calisiyordu.

        String movieUrl = String.format(MOVIE_SEARCH_TEMPLATE, Statics.ENGLISH, query);
        String tvShowUrl = String.format(TV_SHOW_SEARCH_TEMPLATE, Statics.ENGLISH, query);
        String personUrl = String.format(PEOPLE_SEARCH_TEMPLATE, Statics.ENGLISH, query);

        new SearchButtonTask(this).execute(movieUrl, tvShowUrl, personUrl);

        Keyboard.hideKeyboard(getActivity());
    }

    private View.OnClickListener createSearchButtonOnClickListener()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doSearch();
            }
        };
    }


    private View.OnKeyListener createSearchButtonEnterListener()
    {
        return new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    doSearch();
                    return true;
                }

                return false;
            }
        };
    }

    private TextWatcher createTextChangedListener()
    {
        return new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {


            }

            @Override
            public void afterTextChanged(Editable s)
            {


                String url = String.format(MULTI_SEARCH_TEMPLATE, Statics.ENGLISH, s);
                new KeyboardSearchTask(recyclerView).execute(url);
            }
        };
    }

    public void goSearchResultsCardsActivity(Bundle dataBundle)
    {
//        Intent intent = new Intent(getActivity(), SearchResultsCardsActivity.class);
        Intent intent = new Intent(getActivity(), SearchResultsTabbedActivity.class);

        intent.putExtras(dataBundle);
        startActivity(intent);
    }

    public void setSearching(boolean searching)
    {
        this.searching = searching;
    }

}