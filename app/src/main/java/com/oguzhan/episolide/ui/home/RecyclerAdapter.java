package com.oguzhan.episolide.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.oguzhan.episolide.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>
{

    List<String> strings;
    LayoutInflater inflater;

    public RecyclerAdapter(Context context, List<String> strings)
    {
        inflater = LayoutInflater.from(context);
        this.strings = strings;
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
        String selectedProduct = strings.get(position);
        holder.setData(selectedProduct, position);

    }

    @Override
    public int getItemCount()
    {
        return strings.size();
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
            String text = ((TextView) v.findViewById(R.id.suggestion_item_text)).getText().toString();
            Log.d("onclick", text);
        }


    }
}
