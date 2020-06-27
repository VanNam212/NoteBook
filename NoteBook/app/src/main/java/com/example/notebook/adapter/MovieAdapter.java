package com.example.notebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notebook.R;
import com.example.notebook.model.Movie;

import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Movie> movies;
    private ArrayList<Movie> filterList;
    private CustomFilter filter;

    public MovieAdapter(Context context, ArrayList<Movie> personArrayList) {
        this.context = context;
        this.movies = personArrayList;
        this.filterList = personArrayList;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_video, null);
        }
        Movie movie = (Movie) getItem(position);
        if (movie != null) {
            ImageView image = (ImageView) view.findViewById(R.id.image);
            TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
            TextView textViewTime = (TextView) view.findViewById(R.id.textViewTime);
            image.setImageResource(movie.getImage());
            textViewName.setText(movie.getName_film());
            textViewTime.setText(movie.getTime());
            //scroll text
            textViewName.setSelected(true);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
//                constraint = constraint.toString();
                ArrayList<Movie> filters = new ArrayList<>();
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getTime().contains(constraint)) {
                        Movie movie = new Movie(filterList.get(i).getImage(), filterList.get(i).getName_file(), filterList.get(i).getName_film(), filterList.get(i).getTime());
                        filters.add(movie);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movies = (ArrayList<Movie>) results.values;
            notifyDataSetChanged();
        }
    }
}
