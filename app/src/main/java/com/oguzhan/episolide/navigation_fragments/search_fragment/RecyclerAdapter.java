package com.oguzhan.episolide.navigation_fragments.search_fragment;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.oguzhan.episolide.R;

import org.json.JSONArray;
import org.json.JSONException;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>
{

    JSONArray results;
    LayoutInflater inflater;

    public RecyclerAdapter(Context context, JSONArray results)
    {
        inflater = LayoutInflater.from(context);
        this.results = results;
    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.search_suggestion_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        /*
         * KEYS from movieDb
         * movie title
         * tv name
         * person name
         */

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                /*
                *
                * BURADAN ILGILI SAYFAYA GIDECEGIZ
                *
                * */
            }
        });

        try
        {
            String mediaType = results.getJSONObject(position).get("media_type").toString();
            String name = "";
            if (mediaType.equals("person"))
            {
                name = results.getJSONObject(position).get("name").toString();
                int gender = results.getJSONObject(position).getInt("gender");

                int drawable = gender == 1 ? R.drawable.woman : R.drawable.male;
                holder.setDrawable(drawable, position);

            } else if (mediaType.equals("tv"))
            {
                name = results.getJSONObject(position).get("name").toString();
                holder.setDrawable(R.drawable.tv_show, position);
            } else if (mediaType.equals("movie"))
            {
                name = results.getJSONObject(position).get("title").toString();
                holder.setDrawable(R.drawable.movie, position);
            }

            holder.setData(name, position);


        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void setResults(JSONArray results)
    {
        this.results = results;
    }

    @Override
    public int getItemCount()
    {
        return results.length();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView textView;


        public MyViewHolder(View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.suggestion_item_text);
        }

        public void setData(String name, int position)
        {
            this.textView.setText(name);

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void setDrawable(@DrawableRes int drawable, int posiiton)
        {
            this.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, 0, 0, 0);
        }


        @Override
        public void onClick(View v)
        {
            String text = ((TextView) v.findViewById(R.id.suggestion_item_text)).getText().toString();
            Log.d("onclick", text);
        }


    }
}
