package com.oguzhan.episolide.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.SearchResults;
import com.oguzhan.episolide.utils.JsonReader;
import com.oguzhan.episolide.utils.Keyboard;
import com.oguzhan.episolide.utils.Statics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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
                String url = String.format(searchUrl, Statics.ENGLISH, s);
                new KeyboardTask().execute(url);
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
        new SearchTask().execute(url);
        Keyboard.hideKeyboard(getActivity());
    }

    private void goSearchResultsActivity(String jsonDataToTransfer)
    {
        Intent intent = new Intent(getActivity(), SearchResults.class);
        intent.putExtra(SEARCH_RESULTS_TAG, jsonDataToTransfer);
        startActivity(intent);
    }


    private class KeyboardTask extends AsyncTask<String, String, JSONObject>
    {
        private final int MAX_TAKABLE_RESULTS = 10;
        private WeakReference<List<String>> movies;
        private WeakReference<List<String>> tvShows;
        private WeakReference<List<String>> persons;


        public KeyboardTask()
        {
            movies = new WeakReference<>(new ArrayList<>());
            tvShows = new WeakReference<>(new ArrayList<>());
            persons = new WeakReference<>(new ArrayList<>());
        }

        @Override
        protected JSONObject doInBackground(String... strings)
        {

            return JsonReader.readJsonFromUrl(strings[0]);
        }


        @Override
        protected void onPostExecute(JSONObject s)
        {
            try
            {
                /*
                 * KEYS from movieDb
                 * movie title
                 * tv name
                 * person name
                 */

                JSONArray results = s.getJSONArray("results");

                for (int i = 0; i < results.length(); i++)
                {
                    if (i == MAX_TAKABLE_RESULTS)
                        break;

                    JSONObject result = results.getJSONObject(i);

                    String mediaType = result.get("media_type").toString();

                    if (mediaType.equals("person"))
                    {
                        String personName = result.get("name").toString();
                        if (!persons.get().contains(personName))
                            persons.get().add(personName);
                    } else if (mediaType.equals("tv"))
                    {
                        String tvShowName = result.get("name").toString();
                        if (!tvShows.get().contains(tvShowName))
                            tvShows.get().add(tvShowName);
                    } else if (mediaType.equals("movie"))
                    {
                        String movieName = result.get("title").toString();
                        if (!movies.get().contains(movieName))
                            movies.get().add(movieName);
                    }
                }

                Log.d("films", movies.get().toString());
                Log.d("tvShows", tvShows.get().toString());
                Log.d("persons", persons.get().toString());

                RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.resycle_view);

                myAdapter productAdapter = new myAdapter(getContext(), movies.get());
                recyclerView.setAdapter(productAdapter);

//                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }

    }

    private class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder>
    {

        List<String> mProductList;
        LayoutInflater inflater;

        public myAdapter(Context context, List<String> products)
        {
            inflater = LayoutInflater.from(context);
            this.mProductList = products;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = inflater.inflate(R.layout.search_suggestion_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            String selectedProduct = mProductList.get(position);
            holder.setData(selectedProduct, position);

        }

        @Override
        public int getItemCount()
        {
            return mProductList.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {

            TextView textView;

            public MyViewHolder(View itemView)
            {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.suggestion_item_text);


            }

            public void setData(String selectedProduct, int position)
            {
                this.textView.setText(selectedProduct);

            }


            @Override
            public void onClick(View v)
            {


            }


        }
    }

    private class SearchTask extends AsyncTask<String, Void, JSONObject>
    {

        @Override
        protected JSONObject doInBackground(String... strings)
        {
            return JsonReader.readJsonFromUrl(strings[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {
            searching = false;
            if (jsonObject == null)
                return;
            try
            {
                JSONArray results = jsonObject.getJSONArray("results");
                if (results.length() == 0)
                {
                    Toast.makeText(getContext(), "Sonuc Bulunamadi", Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            goSearchResultsActivity(jsonObject.toString());
            super.onPostExecute(jsonObject);
        }
    }


}