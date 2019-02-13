package com.gz.mozixing.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.gz.mozixing.MainActivity;
import com.gz.mozixing.R;
import com.gz.mozixing.activity.setting.SettingActivity;
import com.gz.mozixing.utils.ACacheUtil;
import com.gz.mozixing.utils.AppFolderUtil;
import com.gz.mozixing.utils.CapturePhotoHelper;
import com.gz.mozixing.utils.GetImagePath;
import com.gz.mozixing.utils.LoadingImageUtil;
import com.gz.mozixing.utils.LogUtil;
import com.gz.mozixing.utils.WaitingDialogUtil;
import com.gz.mozixing.utils.zibinluban.Luban;
import com.gz.mozixing.utils.zibinluban.OnCompressListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * 儿童页面
 *
 * @author Alex
 * @since 2019/2/10
 */
public class ChildrenFragment extends Fragment {
    @BindView(R.id.setting_bt)
    ImageView settingBt;
    @BindView(R.id.map_bt)
    ImageView mapBt;
    @BindView(R.id.refresh_bt)
    ImageView refreshBt;
    @BindView(R.id.user_head)
    ImageView userHead;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.temperature_tv)
    TextView temperatureTv;
    @BindView(R.id.body_condition)
    TextView bodyCondition;
    @BindView(R.id.ic_clock)
    ImageView icClock;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.chart)
    LineChart chart;
    View rootView;
    @BindView(R.id.layout_ll)
    LinearLayout layoutLl;

    private String children_id,parentId;
    private static final String CHILDREN_ID = "Children_Id";
    private static final String PARENT_ID = "Parent_Id";
    private Activity activity;
    private String[] xDatas = {"00:00", "6:00", "12:00", "18:00", "24:00"};
    private double[] yDatas = {10.4, 20.4, 36.3, 33.1, 25.5};
    Unbinder bind;

    public static ChildrenFragment getInstance(String id, String parentId) {
        ChildrenFragment sf = new ChildrenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CHILDREN_ID, id);
        bundle.putString(PARENT_ID, parentId);
        sf.setArguments(bundle);
        return sf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            children_id = bundle.getString(CHILDREN_ID);
            parentId = bundle.getString(PARENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_children, null);
        bind = ButterKnife.bind(this, rootView);

        LineData mLineData = getLineData();
        showChart(chart, mLineData);

        return rootView;
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData) {
        lineChart.setDrawBorders(false); // 是否在折线图上添加边框

        // no description text
        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("暂无数据");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        // enable scaling and dragging
        lineChart.setDragEnabled(false);// 是否可以拖拽
        lineChart.setScaleEnabled(false);// 是否可以缩放
        lineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        lineChart.getAxisLeft().setEnabled(false); // 隐藏左边 的坐标轴

        lineChart.getXAxis().setGridColor(Color.TRANSPARENT);//去掉网格中竖线的显示
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextSize(8f);
        //X轴设置显示位置在底部
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        lineChart.setBackgroundColor(Color.TRANSPARENT);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
//
//        // modify the legend ...
//        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
//        mLegend.setForm(Legend.LegendForm.LINE);// 样式
//        mLegend.setFormSize(6f);// 字体
//        mLegend.setTextColor(Color.WHITE);// 颜色
        // mLegend.setTypeface(mTf);// 字体
        mLegend.setEnabled(false);//设置禁用比例块

        lineChart.animateXY(1000, 1000); // 立即执行的动画,x轴
    }

    /**
     * 生成一个数据
     */
    private LineData getLineData() {
        String[] xData = xDatas;//获得的数据，下同
        double[] yData = yDatas;//
        for (int i = 0; i < yData.length; i++) {
            System.out.println("lineChart_yData---:" + yData);
        }
        int dataLength = xData.length;
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < dataLength; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            // xValues.add("" + i);
            xValues.add(xData[i]);
        }

        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < dataLength; i++) {
            yValues.add(new Entry((float) yData[i], i));
        }

        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "温度数据一览图" /* 显示在比例图上 */);
        // 用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.0f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawFilled(false);
//        lineDataSet.setFillColor(activity.getResources().getColor(R.color.color_33E2D2));
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(8f);
        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);

        return lineData;
    }

    /**
     * @param strings 字符串转换成float
     */
    public Float[] stringTofloat(String[] strings) {
        Float[] data = new Float[strings.length];
        for (int i = 0; i < strings.length; i++) {
            data[i] = Float.valueOf(strings[i].trim().replace("℃", ""));
            System.out.println("转换后的数据:" + data[i]);
        }
        return data;
    }


    @OnClick(R.id.setting_bt)
    void setting() {//设置
        SettingActivity.actionStart(activity);
    }

    @OnClick(R.id.map_bt)
    void map() {//地图
        Toast.makeText(activity, "地图", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.refresh_bt)
    void refresh() {//刷新
        Toast.makeText(activity, "刷新", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

}
