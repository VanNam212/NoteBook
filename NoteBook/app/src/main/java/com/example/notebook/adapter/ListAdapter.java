package com.example.notebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.notebook.R;
import com.example.notebook.model.Word;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Word> personArrayList;
    private ArrayList<Word> filterList;
    private CustomFilter filter;

    public ListAdapter(Context context, ArrayList<Word> personArrayList) {
        this.context = context;
        this.personArrayList = personArrayList;
        this.filterList = personArrayList;
    }

    @Override
    public int getCount() {
        return personArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return personArrayList.get(position);
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
            view = inflater.inflate(R.layout.activity_form_main, null);
        }
        Word person = (Word) getItem(position);
        if (person != null) {
            TextView textView = (TextView) view.findViewById(R.id.OriginText);
            TextView textView2 = (TextView) view.findViewById(R.id.SubText);
            textView.setText(person.getOriginal_Text());
            textView2.setText(person.getSub_Text());
            //scroll text
            textView.setSelected(true);
            textView2.setSelected(true);
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
                constraint = constraint.toString().toUpperCase();
                ArrayList<Word> filters = new ArrayList<>();
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getOriginal_Text().toUpperCase().contains(constraint) ||
                            filterList.get(i).getSub_Text().toUpperCase().contains(constraint)) {
                        Word person = new Word(filterList.get(i).getOriginal_Text(), filterList.get(i).getSub_Text());
                        filters.add(person);
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
            personArrayList = (ArrayList<Word>) results.values;
            notifyDataSetChanged();
        }
    }
}
