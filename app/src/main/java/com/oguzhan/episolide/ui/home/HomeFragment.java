package com.oguzhan.episolide.ui.home;

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
import com.oguzhan.episolide.SearchResults;
import com.oguzhan.episolide.utils.Keyboard;
import com.oguzhan.episolide.utils.Statics;

public class HomeFragment extends Fragment
{

    public static final String SEARCH_RESULTS_TAG = "SEARCH_RESULTS";

    private String searchUrl = "https://api.themoviedb.org/3/search/multi?api_key=ee637fe7f604d38049e71cb513a8a04d&language=%s&query=%s&include_adult=true";
    private EditText editText;
    private ImageButton search_btn;

    private boolean searching = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        editText = getView().findViewById(R.id.search_edit_text);
        search_btn = getView().findViewById(R.id.search_btn);
        editText.setOnKeyListener(new View.OnKeyListener()
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
        });


        editText.addTextChangedListener(new TextWatcher()
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
                if (s.length() == 0)
                {
                    RecyclerView recyclerView = view.findViewById(R.id.resycle_view);
                    recyclerView.setAdapter(null);
                    return;
                }

                String url = String.format(searchUrl, Statics.ENGLISH, s);
                new SearchTask(getView()).execute(url);
            }
        });


        search_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doSearch();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void doSearch()
    {
        if (searching)
            return;
        searching = true;
        String query = editText.getText().toString();
        String url = String.format(searchUrl, Statics.ENGLISH, query);
        // Burada search task calisiyordu.
        new SearchTask(getView()).execute(url);
        Keyboard.hideKeyboard(getActivity());
    }

    private void goSearchResultsActivity(String jsonDataToTransfer)
    {
        Intent intent = new Intent(getActivity(), SearchResults.class);
        intent.putExtra(SEARCH_RESULTS_TAG, jsonDataToTransfer);
        startActivity(intent);
    }


}