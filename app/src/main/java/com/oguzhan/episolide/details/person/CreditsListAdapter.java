package com.oguzhan.episolide.details.person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.oguzhan.episolide.R;

import java.util.List;

public class CreditsListAdapter extends ArrayAdapter<PersonCreditDetails>
{
    public CreditsListAdapter(@NonNull Context context, List<PersonCreditDetails> personCreditDetails)
    {
        super(context, 0, personCreditDetails);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        // Get the data item for this position

        PersonCreditDetails personCreditDetails = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null)
        {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_credits_item, parent, false);

        }

        // Lookup view for data population
        TextView nameTextView = (TextView) convertView.findViewById(R.id.item_name_text);


        // Populate the data into the template view using the data object
        nameTextView.setText(personCreditDetails.name);

        // Return the completed view to render on screen
        return convertView;
    }
}
