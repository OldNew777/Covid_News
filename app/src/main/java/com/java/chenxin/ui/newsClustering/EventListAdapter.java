package com.java.chenxin.ui.newsClustering;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

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
        Resources res = getContext().getResources();
        CardView cardView = (CardView)view.findViewById(R.id.event_list_card);
        if(position % 3 == 1){
            cardView.setCardBackgroundColor(res.getColor(R.color.light_aqua));
        }
        else if(position % 3 == 2){
            cardView.setCardBackgroundColor(res.getColor(R.color.light_wheat));
        }
        else{
            cardView.setCardBackgroundColor(res.getColor(R.color.light_rose));
        }
        TextView title = (TextView)view.findViewById(R.id.event_list_title);
        TextView source = (TextView)view.findViewById(R.id.event_list_source);
        TextView data = (TextView)view.findViewById(R.id.event_list_date);

        title.setText(newsPiece.getTitle());
        source.setText(newsPiece.getSource());
        data.setText(newsPiece.getDate());

        return view;
    }
}
