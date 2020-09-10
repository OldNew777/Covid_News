package com.java.chenxin.ui.data.epidemicMap;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.java.chenxin.R;
import com.java.chenxin.data_struct.Relation;

import java.util.List;

public class RelationAdapter extends ArrayAdapter {
    private int resourceId;

    public RelationAdapter(@NonNull Context context, int resource, @NonNull List<Relation> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        Relation relation = (Relation) getItem(position);

        TextView entity_name = (TextView)view.findViewById(R.id.entity_name);
        TextView entity_relation = (TextView)view.findViewById(R.id.entity_relation);

        entity_name.setText(relation.getLabel());
        entity_relation.setText(relation.getIsFather() ? "属于" : "父类");

        return view;
    }
}
