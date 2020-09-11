package com.java.chenxin.ui.scholar;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.java.chenxin.R;
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
        Resources res = getContext().getResources();
        CardView cardView = (CardView)view.findViewById(R.id.scholar_list_card);
        // 找组件
        TextView name_zh_textView = (TextView)view.findViewById(R.id.scholar_list_name_zh);
        TextView name_en_textView = (TextView)view.findViewById(R.id.scholar_list_name_en);
        TextView scholarPostion = (TextView)view.findViewById(R.id.scholar_list_position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_viewer);
        if(scholar.getIsPassaway()){
            cardView.setCardBackgroundColor(res.getColor(R.color.light_gray));
            name_zh_textView.setTextColor(res.getColor(R.color.white));
            name_en_textView.setTextColor(res.getColor(R.color.white));
            scholarPostion.setTextColor(res.getColor(R.color.white));
        }
        // 名字处理
        String name_zh = scholar.getNameZh();
        String name_en = scholar.getName();
        if (name_zh.equals("")){
            String tmp = name_en;
            name_en = name_zh;
            name_zh = tmp;
        }
        if(scholar.getIsPassaway()){
            name_zh += " (已故）";
        }

        Glide.with(view).load(scholar.getImgUrl()).into(imageView);
        name_zh_textView.setText(name_zh);
        name_en_textView.setText(name_en);
        scholarPostion.setText(scholar.getPosition());

        return view;
    }
}
