package com.gz.mozixing.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gz.mozixing.event.UpdateEvent;
import com.gz.mozixing.network.model.ChildrenInfoModel;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.utils.LoadingImageUtil;
import com.gz.mozixing.utils.RemindDialogUtil;
import com.gz.mozixing.utils.WaitingDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    @BindView(R.id.c_tv)
    TextView cTv;
    @BindView(R.id.temperature_ll)
    LinearLayout temperatureLl;

    private String childrenId, parentId;
    private static final String CHILDREN_ID = "Children_Id";
    private static final String PARENT_ID = "Parent_Id";
    private Activity activity;
    private ArrayList<ChildrenInfoModel.DataBean.ResultBean> list = new ArrayList<>();
    Unbinder bind;
//        private double[] yDatas = {10.4, 20.4, 36.3, 33.1, 25.5};
//        private String[] xDatas = {"00:00", "6:00", "12:00", "18:00", "24:00"};

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
            childrenId = bundle.getString(CHILDREN_ID);
            parentId = bundle.getString(PARENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_children, null);
        bind = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);//注册eventBus
        getData();
        return rootView;
    }

    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("childrenId", childrenId);
        map.put("parentId", parentId);
        ChildrenInfoModel.getResponse(map, new NetWorkCallback<ChildrenInfoModel>() {
            @Override
            public void onResponse(ChildrenInfoModel response) {
                setView(response);
                list.clear();
                if (response.getData().getResultX() != null && response.getData().getResultX().size() > 0) {
                    list.addAll(response.getData().getResultX());
                }
                LineData mLineData = getLineData();
                showChart(chart, mLineData);
//                chart.invalidate(); //用来刷新图表
            }

            @Override
            public void onFailure(String message) {
                RemindDialogUtil.showEasy(activity, message);
            }
        });
    }

    /**
     *
     */
    private void setView(ChildrenInfoModel model) {
        LoadingImageUtil.loadingCircle(userHead, model.getData().getPhoto());
        userName.setText(model.getData().getName());
        if (model.getData().getTemperature() != null && !model.getData().getTemperature().equalsIgnoreCase("")) {
            temperatureTv.setText(model.getData().getTemperature());
        } else {
            temperatureTv.setText("0");
        }
        timeTv.setText(model.getData().getTime());
        switch (model.getData().getState()) {//1 代表健康 , 2 代表低烧 ,  3 代表高烧
            case 1:
                temperatureLl.setBackgroundResource(R.mipmap.ic_temperature);
                temperatureTv.setTextColor(activity.getResources().getColor(R.color.color_green));
                cTv.setTextColor(activity.getResources().getColor(R.color.color_green));
                bodyCondition.setText(activity.getString(R.string.healthy));
                bodyCondition.setBackgroundResource(R.drawable.add_email_eidt_shape_green);
                break;
            case 2:
                temperatureLl.setBackgroundResource(R.mipmap.ic_temperature_o);
                temperatureTv.setTextColor(activity.getResources().getColor(R.color.color_FDA62A));
                cTv.setTextColor(activity.getResources().getColor(R.color.color_FDA62A));
                bodyCondition.setText(activity.getString(R.string.low_fever));
                bodyCondition.setBackgroundResource(R.drawable.add_email_eidt_shape_o);
                break;
            case 3:
                temperatureLl.setBackgroundResource(R.mipmap.ic_temperature_r);
                temperatureTv.setTextColor(activity.getResources().getColor(R.color.color_FB3C2A));
                cTv.setTextColor(activity.getResources().getColor(R.color.color_FB3C2A));
                bodyCondition.setText(activity.getString(R.string.high_fever));
                bodyCondition.setBackgroundResource(R.drawable.add_email_eidt_shape_r);
                break;

        }

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
//        String[] xData = xDatas;//获得的数据，下同
//        double[] yData = yDatas;//
//        for (int i = 0; i < yData.length; i++) {
//            System.out.println("lineChart_yData---:" + yData);
//        }
        int dataLength = list.size();
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < dataLength; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            // xValues.add("" + i);
            xValues.add(list.get(i).getTime());
        }

        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < dataLength; i++) {
            yValues.add(new Entry(Float.valueOf(list.get(i).getTemperature()), i));
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


    @OnClick(R.id.setting_bt)
    void setting() {//设置
        SettingActivity.actionStart(activity, childrenId, parentId);
    }

    @OnClick(R.id.map_bt)
    void map() {//地图
        Toast.makeText(activity, "功能开发中...", Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.refresh_bt)
    void refresh() {//刷新
        WaitingDialogUtil.show(activity);
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
        EventBus.getDefault().unregister(this);//注销eventBus
    }


    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateEvent event) {
        if (event.getMsg() != null && event.getMsg().equalsIgnoreCase("true")) {
            Toast.makeText(activity, childrenId + "号刷新了", Toast.LENGTH_SHORT).show();
            getData();
        }
    }
}
