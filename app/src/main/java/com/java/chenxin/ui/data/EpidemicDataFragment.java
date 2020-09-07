package com.java.chenxin.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.java.chenxin.R;
import com.java.chenxin.universal.StringListAdapter;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

public class EpidemicDataFragment extends Fragment  {
    private NiceSpinner spinnerCountry;

    private List<String> spinnerCountryList = new ArrayList<>(100);

    private StringListAdapter spinnerCountryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_epidemic_data, container, false);

        System.out.println("China");
        // 找组件
        spinnerCountry = (NiceSpinner) root.findViewById(R.id.spinner_country);

        // DEBUG
        spinnerCountryList.add("China");
        spinnerCountryList.add("America");

        // 构建下拉选择栏的适配器
//        spinnerCountryAdapter = new StringListAdapter(getContext(), R.layout.item_spinnerlist, spinnerCountryList);
//        spinnerCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.attachDataSource(spinnerCountryList);

        return root;
    }
}
