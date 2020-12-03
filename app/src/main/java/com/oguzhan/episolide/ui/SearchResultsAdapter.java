package com.oguzhan.episolide.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oguzhan.episolide.R;
import com.oguzhan.episolide.utils.Statics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchResultsAdapter extends BaseAdapter
{

    private JSONArray jsonArray;
    private final Context context;

    private final String mediaType;

    public SearchResultsAdapter(Context context, JSONArray jsonArray, String mediaType)
    {

        this.context = context;
        this.jsonArray = jsonArray;
        this.mediaType = mediaType;
    }

    @Override
    public int getCount()
    {
        return jsonArray.length();
    }

    @Override
    public JSONObject getItem(int position)
    {
        try
        {
            return (JSONObject) jsonArray.get(position);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }


    public void setJsonArray(JSONArray jsonArray)
    {
        this.jsonArray = jsonArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder;
        if (convertView == null)
        {

            if (mediaType.equals(Statics.MovieKeys.MEDIA_TYPE) || mediaType.equals(Statics.TvShowKeys.MEDIA_TYPE))
            {
                convertView = View.inflate(context, R.layout.list_item_movie_tvshow, null);

                holder = new MovieOrTvViewHolder();
                ((MovieOrTvViewHolder) holder).posterImageView = convertView.findViewById(R.id.movieandtv_card_image);
                ((MovieOrTvViewHolder) holder).nameTextview = convertView.findViewById(R.id.movieandtv_card_name_text);
                ((MovieOrTvViewHolder) holder).genreTextview = convertView.findViewById(R.id.movieandtv_card_genre_text);
                ((MovieOrTvViewHolder) holder).releaseDateTextview = convertView.findViewById(R.id.movienandtv_release_date_text);
                ((MovieOrTvViewHolder) holder).voteTextview = convertView.findViewById(R.id.movieandtv_card_vote_text);

            } else
            {
                convertView = View.inflate(context, R.layout.list_item_person, null);

                holder = new PersonViewHolder();
                ((PersonViewHolder) holder).posterImageView = convertView.findViewById(R.id.person_card_image);
                ((PersonViewHolder) holder).nameTextview = convertView.findViewById(R.id.person_card_name_text);
                ((PersonViewHolder) holder).departmentTextview = convertView.findViewById(R.id.person_card_department);

            }


            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        try
        {


            JSONObject item = jsonArray.getJSONObject(position);


            if (mediaType.equals(Statics.TvShowKeys.MEDIA_TYPE))
            {
                setViewHolderValuesForTvShow((MovieOrTvViewHolder) holder, item);

            } else if (mediaType.equals(Statics.MovieKeys.MEDIA_TYPE))
            {
                setViewHolderValuesForMovie((MovieOrTvViewHolder) holder, item);
            } else if (mediaType.equals(Statics.PersonKeys.MEDIA_TYPE))
            {
                setViewHolderValuesForPerson((PersonViewHolder) holder, item);
            }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        return convertView;
    }


    private void setViewHolderValuesForMovie(MovieOrTvViewHolder holder, JSONObject item) throws JSONException
    {

        String posterPath = item.getString(Statics.MovieKeys.POSTER_PATH);

        String posterURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + posterPath;
        Picasso.get().load(posterURL).into(holder.posterImageView);

        holder.nameTextview.setText(item.getString(Statics.MovieKeys.NAME));
        holder.releaseDateTextview.setText(item.getString(Statics.MovieKeys.RELEASE_DATE));
        holder.genreTextview.setText(item.get(Statics.MovieKeys.GENRE_IDS).toString());
        holder.voteTextview.setText(item.getString(Statics.MovieKeys.VOTE_AVERAGE));

    }

    private void setViewHolderValuesForTvShow(MovieOrTvViewHolder holder, JSONObject item) throws JSONException
    {
        String posterPath = item.getString(Statics.TvShowKeys.POSTER_PATH);

        String posterURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + posterPath;
        Picasso.get().load(posterURL).into(holder.posterImageView);

        holder.nameTextview.setText(item.getString(Statics.TvShowKeys.NAME));
        holder.releaseDateTextview.setText(item.getString(Statics.TvShowKeys.RELEASE_DATE));
        holder.genreTextview.setText(item.get(Statics.TvShowKeys.GENRE_IDS).toString());
        holder.voteTextview.setText(item.getString(Statics.TvShowKeys.VOTE_AVERAGE));

    }

    private void setViewHolderValuesForPerson(PersonViewHolder holder, JSONObject item) throws JSONException
    {
        String posterPath = item.getString(Statics.PersonKeys.POSTER_PATH);

        String posterURL = Statics.BASE_IMAGE_URL + Statics.POSTER_SIZES[1] + posterPath;
        Picasso.get().load(posterURL).into(holder.posterImageView);

        holder.nameTextview.setText(item.getString(Statics.PersonKeys.NAME));
        holder.departmentTextview.setText(item.getString(Statics.PersonKeys.DEPARTMENT));

    }


    private static class PersonViewHolder extends ViewHolder
    {
        ImageView posterImageView;
        TextView nameTextview;
        TextView departmentTextview;

    }

    private static class MovieOrTvViewHolder extends ViewHolder
    {
        ImageView posterImageView;
        TextView nameTextview;
        TextView genreTextview;
        TextView releaseDateTextview;
        TextView voteTextview;

    }


    private abstract static class ViewHolder
    {

    }


}
