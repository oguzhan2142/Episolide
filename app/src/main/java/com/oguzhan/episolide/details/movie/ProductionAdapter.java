package com.oguzhan.episolide.details.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.oguzhan.episolide.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductionAdapter extends ArrayAdapter<ProductionInfo>
{


    public ProductionAdapter(@NonNull Context context, List<ProductionInfo> companies)
    {
        super(context, R.layout.production_listitem, companies);
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.production_listitem, parent, false);

        }
        TextView textView = convertView.findViewById(R.id.production_name);
        ImageView imageView = convertView.findViewById(R.id.production_image);

        ProductionInfo productionInfo = getItem(position);

        textView.setText(productionInfo.name);



        Picasso.get().load(productionInfo.imageURL).into(imageView);

        return convertView;
    }
}
