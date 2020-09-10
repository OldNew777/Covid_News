package com.java.chenxin.ui.scholar;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.java.chenxin.R;
import com.java.chenxin.data_struct.NewsPiece;
import com.java.chenxin.data_struct.Scholar;

import java.util.List;

public class ScholarAdapter extends ArrayAdapter<Scholar> {
    private int resourceId;

    public  ScholarAdapter(Context context, int textViewResourceId, List<Scholar> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        Scholar scholar = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView name = (TextView)view.findViewById(R.id.scholar_list_name);
        TextView nameZh = (TextView)view.findViewById(R.id.scholar_list_name_zh);
        TextView scholarPostion = (TextView)view.findViewById(R.id.scholar_list_position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_viewer);
        Glide.with(view).load(scholar.getImgUrl()).into(imageView);
        name.setText(scholar.getNameZh());
        nameZh.setText(scholar.getName());
        scholarPostion.setText(scholar.getPosition());


        return view;
    }
}
