package com.clwater.mpandroid.linechart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.clwater.mpandroid.linechart.databinding.ActivityMainBinding;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private OnChartGestureListener onChartGestureListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        initView();
    }

    private void initView() {
        initListener();
        initLineChart();
    }

    /**
     * @description 设置滑动监听
     */
    private void initListener() {
        onChartGestureListener = new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                //高亮屏幕中心位置的数据
                binding.lcReportInfo.highlightValue(getChartMiddle(), 0);
            }
        };
    }

    private int random(){
        return new Random().nextInt(1000 - 100 + 1) + 100;
    }

    private void initLineChart() {
        //-------------数据源-------------
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            entries.add(new Entry(i, random()));
        }
        //-------------数据源-------------

        //-------------折线样式-------------
        //设置一条折线信息
        LineDataSet dataSet = new LineDataSet(entries, "");

        //设置折线下部阴影是否填充
        dataSet.setDrawFilled(true);
        //设置折现填充的颜色(透明度为0x85/0x255 在33左右)
        //详情原因可以查看{@link LineRadarDataSet#mFillAlpha 参数相关定义}
        dataSet.setFillColor(Color.parseColor("#c04851"));
        //设置折现的颜色
        dataSet.setColor(Color.parseColor("#c04851"));
        //折线的宽度
        dataSet.setLineWidth(2);
        //设置折线模式为圆滑的曲线
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //是否绘制
        dataSet.setDrawValues(false);
        //是否绘制折线的点(下面会手动添加)
        dataSet.setDrawCircles(false);
        //是否绘制高亮效果
        dataSet.setDrawHighlightIndicators(false);



        //设置折线信息, 一个折线图中可以包含多个折线, 这么只添加了一条折线
        LineData lineData = new LineData();
        lineData.addDataSet(dataSet);
        //-------------折线样式-------------


        //-------------x轴样式-------------
        XAxis xAxis = binding.lcReportInfo.getXAxis();
        //将x轴置于底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        //两侧设置留空效果, 具体数值可以根据实际调整
        xAxis.setAxisMinimum(-0.3f);
        xAxis.setAxisMaximum(entries.size() - 0.7f);


        //x轴文件显示颜色
        xAxis.setTextColor(Color.WHITE);
        //关闭网络线
        xAxis.setDrawGridLines(false);
        //关闭延x轴线
        xAxis.setDrawAxisLine(false);

        //可选, 是否控制x轴间隔数量
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1);



        List<String> xAxisValues = new ArrayList<>();
        //自定义x轴显示样式
        for (Entry entry: entries){
            xAxisValues.add(Math.round(entry.getX()) + "时");
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        //-------------x轴样式-------------


        //-------------y轴样式-------------
        YAxis yAxis = binding.lcReportInfo.getAxisLeft();
        //控制y轴最大最小范围, 避免因为使用了曲线而导致的折线截断
        yAxis.setAxisMinimum(-100);
        yAxis.setAxisMaximum(1100);

        //关闭两侧y轴显示
        binding.lcReportInfo.getAxisRight().setEnabled(false);
        yAxis.setEnabled(false);
        //-------------y轴样式-------------


        //-------------图表通用设置-------------
        //设置折线图显示的数据
        binding.lcReportInfo.setData(lineData);
        //不可手动缩放
        binding.lcReportInfo.setDragDecelerationEnabled(true);

        Matrix matrix = new Matrix();
        //图标缩放显示, 使得一次最多显示6个数据
        matrix.postScale(entries.size() / 6f, 1f);
        binding.lcReportInfo.getViewPortHandler().refresh(matrix, binding.lcReportInfo, false);

        //设置四周边界位移
        binding.lcReportInfo.setMinOffset(0);

        //设置空描述
        binding.lcReportInfo.setDescription(null);
        //关闭缩放
        binding.lcReportInfo.setScaleEnabled(false);


        //设置图例不显示
        Legend legend = binding.lcReportInfo.getLegend();
        legend.setEnabled(false);

//        //创建覆盖物并选择中心位置数据高亮
        if (entries.size() != 0) {
            createMakerView();
            //此操作需要晚于setData(), 否则会导致空指针异常
            binding.lcReportInfo.highlightValue(getChartMiddle(), 0);
            updateBottomInfo(entries.get(getChartMiddle()).getY());
        }

        //设置滑动监听
        binding.lcReportInfo.setOnChartGestureListener(onChartGestureListener);


        //监听选中
        binding.lcReportInfo.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //查看覆盖物是否被回收
                if (binding.lcReportInfo.isMarkerAllNull()) {
                    //重新绑定覆盖物
                    createMakerView();
                    //并且手动高亮覆盖物
                    binding.lcReportInfo.highlightValue(h);
                }
                updateBottomInfo(e.getY());
            }

            @Override
            public void onNothingSelected() {

            }
        });


        binding.lcReportInfo.invalidate(); // refresh
        //-------------图表通用设置-------------

    }


    /**
     * @description 创建图表高亮覆盖物
     */
    public void createMakerView() {
        ReportInfoMarkerView reportInfoMarkerView = new ReportInfoMarkerView(this, "m");
        //避免覆盖物超出屏幕显示范围
        reportInfoMarkerView.setChartView(binding.lcReportInfo);
        binding.lcReportInfo.setReportInfoMarkerView(reportInfoMarkerView);
        binding.lcReportInfo.setReportPointMarkerView(new ReportPointMarkerView(this));
    }


    /**
     * @description 更新底部显示
     */
    private void updateBottomInfo(float index){
        binding.tvReportChooseText.setText("" + index);
    }



    /**
     * @description 获取当前页面中心坐标
     */
    private int getChartMiddle(){
        ViewPortHandler handler = binding.lcReportInfo.getViewPortHandler();
        MPPointD topLeft = binding.lcReportInfo.getValuesByTouchPoint(handler.contentLeft(), handler.contentTop(), YAxis.AxisDependency.LEFT);
        MPPointD bottomRight = binding.lcReportInfo.getValuesByTouchPoint(handler.contentRight(), handler.contentBottom(), YAxis.AxisDependency.LEFT);

        int middle = (int) ((bottomRight.x - topLeft.x) / 2 + topLeft.x);
        return middle;
    }
}