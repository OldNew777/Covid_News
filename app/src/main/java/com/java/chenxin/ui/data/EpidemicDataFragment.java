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
    private NiceSpinner spinnerProvince;
    private NiceSpinner spinnerCity;

    private List<String> spinnerCountryList;
    private List<String> spinnerProvinceList;
    private List<String> spinnerCityList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_epidemic_data, container, false);

        System.out.println("China");
        // 找组件
        spinnerCountry = (NiceSpinner) root.findViewById(R.id.spinner_country);
        spinnerProvince = (NiceSpinner) root.findViewById(R.id.spinner_province);
        spinnerCity = (NiceSpinner) root.findViewById(R.id.spinner_city);

        // 设置下拉栏数据
        spinnerCountryList = new ArrayList<>();
        spinnerProvinceList = new ArrayList<>();
        spinnerCityList = new ArrayList<>();
        // DEBUG
        spinnerCountryList.add("UK");

        // 设置adapter

        // 构建下拉选择栏的适配器
        spinnerCountry.attachDataSource(spinnerCountryList);
        spinnerProvince.attachDataSource(spinnerProvinceList);
        spinnerCity.attachDataSource(spinnerCityList);

        spinnerCountry.setHint("请选择国家");
        spinnerProvince.setHint("请选择省份");
        spinnerCity.setHint("请选择城市");

        spinnerCountryList.add("USA");

        return root;
    }
}
