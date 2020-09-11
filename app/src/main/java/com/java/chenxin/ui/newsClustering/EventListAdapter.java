package com.java.chenxin.ui.newsClustering;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.java.chenxin.R;
import com.java.chenxin.data_struct.NewsPiece;

import java.util.List;

public class EventListAdapter extends ArrayAdapter<NewsPiece> {
    private int resourceId;

    public EventListAdapter(@NonNull Context context, int resource, @NonNull List<NewsPiece> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        NewsPiece newsPiece = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView title = (TextView)view.findViewById(R.id.event_list_title);
        TextView source = (TextView)view.findViewById(R.id.event_list_source);
        TextView data = (TextView)view.findViewById(R.id.event_list_date);

        title.setText(newsPiece.getTitle());
        source.setText(newsPiece.getSource());
        data.setText(newsPiece.getDate());

        return view;
    }
}
