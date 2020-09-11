package com.java.chenxin.ui.data.epidemicData;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.java.chenxin.R;
import com.java.chenxin.background.DataServer;
import com.java.chenxin.data_struct.DataPerDay;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

public class EpidemicDataActivity extends AppCompatActivity {
    // 组件
    private LineChartView previewLineChartView;

    // 数据
    private List<DataPerDay> dataPerDayList;

    // observer
    private Observer<List<DataPerDay>> dataObserver;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:     // return
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epidemic_data_chart);

        // actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String districtName = (String) getIntent().getSerializableExtra("District");
        getSupportActionBar().setTitle(getSupportActionBar().getTitle() + " : " + districtName);

        // 找组件
        previewLineChartView = findViewById(R.id.preview_line_chartview);

        // 设置属性
        initLineChart();

        // 获取数据
        dataObserver = new Observer<List<DataPerDay>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(List<DataPerDay> dataPerDays) {
                dataPerDayList = dataPerDays;
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getParent(), "加载失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void onComplete() {
                setChartData();
            }
        };
        DataServer.getDataPerDay(dataObserver, districtName, 14);
    }

    private void setChartData(){
        Axis axisX = new Axis(), axisY = new Axis();
        List<PointValue> pointValueList_dead = new ArrayList<>(60);
        List<PointValue> pointValueList_cured = new ArrayList<>(60);
        List<PointValue> pointValueList_confirmed = new ArrayList<>(60);
        List<AxisValue> axisXValueList = new ArrayList<>(60);
        List<AxisValue> axisYValueList = new ArrayList<>(10);

        axisX.setName("日期");
        axisX.setLineColor(Color.BLACK);     // 设置轴颜色
        axisX.setTextColor(Color.BLACK);     // 设置文字颜色
        axisX.setTextSize(12);            // 设置文字大小
//        axisX.setTypeface(Typeface.DEFAULT);      // 设置文字样式，此处为默认
//        axisX.setHasSeparationLine(boolean isHasSeparationLine);  // 设置是否有分割线
        axisX.setHasTiltedLabels(true);     // 设置文字向左旋转45度
        axisX.setHasLines(true);            // 是否显示轴网格线
        axisX.setValues(axisXValueList);


        axisY.setName("人数");
        axisY.setLineColor(Color.BLACK);
        axisY.setTextColor(Color.BLACK);
        axisY.setTextSize(12);            // 设置文字大小
        axisY.setHasTiltedLabels(true);     // 设置文字向左旋转45度
        axisY.setHasLines(true);       // 是否显示轴网格线
        axisY.setValues(axisYValueList);

        int maxY = -1, minY = Integer.MAX_VALUE;

        for (int i = 0; i < dataPerDayList.size(); ++i){
            DataPerDay dataPerDay = dataPerDayList.get(i);

            axisXValueList.add(new AxisValue(i).setLabel(dataPerDay.date));
            pointValueList_dead.add(new PointValue(i, dataPerDay.dead));
            pointValueList_cured.add(new PointValue(i, dataPerDay.cured));
            pointValueList_confirmed.add(new PointValue(i, dataPerDay.confirmed));

            maxY = Math.max(maxY, dataPerDay.dead);
            maxY = Math.max(maxY, dataPerDay.cured);
            maxY = Math.max(maxY, dataPerDay.confirmed);
            minY = Math.min(minY, dataPerDay.dead);
            minY = Math.min(minY, dataPerDay.cured);
            minY = Math.min(minY, dataPerDay.confirmed);
        }


        double sectionY = (maxY - minY) / 5.0;
        for (int i = 0; i < 6; ++i){
            int Y = (int) (sectionY * i) + minY;
            axisYValueList.add(new AxisValue(Y).setLabel(String.valueOf(Y)));
        }


        // Line的list，包括dead/cured/confirmed
        List<Line> lines = new ArrayList<>(3);

        // dead
        Line line_dead = new Line(pointValueList_dead);
        line_dead.setColor(Color.BLACK);
        line_dead.setHasLabelsOnlyForSelected(true);    // 点击时才显示节点数据
        line_dead.setShape(ValueShape.CIRCLE);          // 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line_dead.setFilled(true);
        // cured
        Line line_cured = new Line(pointValueList_cured);
        line_cured.setColor(Color.GREEN);
        line_cured.setHasLabelsOnlyForSelected(true);    // 点击时才显示节点数据
        line_cured.setShape(ValueShape.DIAMOND);        // 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line_cured.setFilled(true);
        // confirmed
        Line line_confirmed = new Line(pointValueList_confirmed);
        line_confirmed.setColor(Color.RED);
        line_confirmed.setHasLabelsOnlyForSelected(true);    // 点击时才显示节点数据
        line_confirmed.setShape(ValueShape.SQUARE);         // 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line_confirmed.setFilled(true);

        lines.add(line_dead);
        lines.add(line_cured);
        lines.add(line_confirmed);

        LineChartData data_lineChart = new LineChartData(lines);
        data_lineChart.setAxisXBottom(axisX);
        data_lineChart.setAxisYLeft(axisY);

        previewLineChartView.setLineChartData(data_lineChart);

        // 对显示界面进行处理
        previewLineChartView.setInteractive(true);
        previewLineChartView.setZoomEnabled(false);
        previewLineChartView.setContainerScrollEnabled(
                true, ContainerScrollType.HORIZONTAL);

        Viewport viewport = new Viewport(previewLineChartView.getMaximumViewport());
        viewport.top = maxY + (maxY - minY) / 5;
        previewLineChartView.setMaximumViewport(viewport);

        viewport.left = 0;
        viewport.right = 4;
        previewLineChartView.setCurrentViewport(viewport);
    }


    private void initLineChart(){
        previewLineChartView.setZoomEnabled(true);
        previewLineChartView.setInteractive(false);
    }
}
