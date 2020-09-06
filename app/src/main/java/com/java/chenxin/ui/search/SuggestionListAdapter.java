package com.java.chenxin.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.java.chenxin.R;

import java.util.List;

public class SuggestionListAdapter extends ArrayAdapter<String> {
    private int resourceId;

    public SuggestionListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        String suggestion = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView suggestionView = (TextView)view.findViewById(R.id.suggestion_view);
        suggestionView.setText(suggestion);

        return view;
    }


}
