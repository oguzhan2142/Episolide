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
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductionCompaniesAdapter extends ArrayAdapter<ProductionCompany>
{


    public ProductionCompaniesAdapter(@NonNull Context context, List<ProductionCompany> companies)
    {
        super(context, R.layout.production_company_listitem, companies);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.production_company_listitem, parent, false);

        }
        TextView textView = convertView.findViewById(R.id.company_name);
        ImageView imageView = convertView.findViewById(R.id.company_logo);

        ProductionCompany productionCompany = getItem(position);

        textView.setText(productionCompany.name);
        String URL = Statics.BASE_IMAGE_URL + Statics.LOGO_SIZES[1] + productionCompany.imagePath;



        Picasso.get().load(URL).into(imageView);

        return convertView;
    }
}
