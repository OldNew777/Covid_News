package com.java.chenxin.ui.data.epidemicData;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.java.chenxin.R;
import com.java.chenxin.background.DataServer;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EpidemicDataFragment extends Fragment  {
    // 地区三级分级的Map
    Map<String, Map<String, List<String>>> districtMap;

    // 下拉栏的数据
    private List<String> spinnerCountryList;
    private List<String> spinnerProvinceList;
    private List<String> spinnerCityList;
    private List<String> emptyListCity;

    // 下拉栏
    private NiceSpinner spinnerCountry;
    private NiceSpinner spinnerProvince;
    private NiceSpinner spinnerCity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_epidemic_data, container, false);

        // 获取地区分级信息
        districtMap = DataServer.readNameListJSON();

        // 找组件
        spinnerCountry = (NiceSpinner) root.findViewById(R.id.spinner_country);
        spinnerProvince = (NiceSpinner) root.findViewById(R.id.spinner_province);
        spinnerCity = (NiceSpinner) root.findViewById(R.id.spinner_city);
        Button checkButton = (Button) root.findViewById(R.id.button_check);

        // 设置下拉栏数据
        spinnerCountryList = new ArrayList<>(districtMap.keySet());
        Collections.sort(spinnerCountryList);
        spinnerCountryList.add(0, getResources().getString(R.string.input_country));
        emptyListCity = new ArrayList<>();
        emptyListCity.add(getResources().getString(R.string.input_city));


        // 构建下拉选择栏的适配器
        spinnerCountry.attachDataSource(spinnerCountryList);
        spinnerCountry.setSelectedIndex(0);
        spinnerProvince.attachDataSource(emptyListCity);
        spinnerCity.attachDataSource(emptyListCity);

        // 监听
        spinnerCountry.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                System.out.println("spinnerCountry.onItemSelected");
                if (parent.getSelectedIndex() == 0)
                    return;
                spinnerProvinceList = new ArrayList<>(districtMap.get(parent.getSelectedItem()).keySet());
                Collections.sort(spinnerProvinceList);
                spinnerProvinceList.add(0, getResources().getString(R.string.input_province));
                spinnerProvince.attachDataSource(spinnerProvinceList);
                spinnerCity.attachDataSource(emptyListCity);
                spinnerProvince.setSelectedIndex(0);
            }
        });
        spinnerProvince.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                if (parent.getSelectedIndex() == 0)
                    return;
                System.out.println("spinnerCountry.getSelectedItem() = " + spinnerCountry.getSelectedItem());
                spinnerCityList = new ArrayList<>(
                        districtMap.get(spinnerCountry.getSelectedItem()).get(parent.getSelectedItem()));
                Collections.sort(spinnerCityList);
                spinnerCityList.add(0, getResources().getString(R.string.input_city));
                spinnerCity.attachDataSource(spinnerCityList);
            }
        });

        // 查询按钮的监听
        checkButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                String districtName, token = (String) spinnerCountry.getSelectedItem();
                if (token.equals(getResources().getString(R.string.input_country)) || token.equals(""))
                    return;
                districtName = token;
                token = (String) spinnerProvince.getSelectedItem();
                if (!token.equals(getResources().getString(R.string.input_province)) && !token.equals("")){
                    districtName += "|" + token;
                    token = (String) spinnerCity.getSelectedItem();
                    if (!token.equals(getResources().getString(R.string.input_city)) && !token.equals(""))
                        districtName += "|" + token;
                }

                Intent intent = new Intent(getContext(), EpidemicDataActivity.class);
                intent.putExtra("District", districtName);
                startActivity(intent);
            }
        });

        return root;
    }

}
