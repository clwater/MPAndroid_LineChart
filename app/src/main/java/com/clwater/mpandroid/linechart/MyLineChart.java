package com.clwater.mpandroid.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.lang.ref.WeakReference;

public class MyLineChart extends LineChart {
    //弱引用覆盖物对象,防止内存泄漏,不被回收
    private WeakReference<ReportInfoMarkerView> reportInfoMarkerViewWeakReference;
    private WeakReference<ReportPointMarkerView> reportPointMarkerViewWeakReference;

    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 所有覆盖物是否为空
     *
     * @return TRUE FALSE
     */
    public boolean isMarkerAllNull() {
        return reportInfoMarkerViewWeakReference == null || reportInfoMarkerViewWeakReference.get() == null
                || reportPointMarkerViewWeakReference == null || reportPointMarkerViewWeakReference.get() == null;
//                && mRoundMarkerReference.get() == null && mPositionMarkerReference.get() == null;
    }

    public void setReportInfoMarkerView(ReportInfoMarkerView reportInfoMarkerView) {
        reportInfoMarkerViewWeakReference = new WeakReference<>(reportInfoMarkerView);
    }
//
    public void setReportPointMarkerView(ReportPointMarkerView roundMarker) {
        reportPointMarkerViewWeakReference = new WeakReference<>(roundMarker);
    }
//
//    public void setPositionMarker(PositionMarker positionMarker) {
//        mPositionMarkerReference = new WeakReference<>(positionMarker);
//    }


    /**
     * 复制父类的 drawMarkers方法，并且更换上自己的markerView
     * draws all MarkerViews on the highlighted positions
     */
    protected void drawMarkers(Canvas canvas) {
        if (reportInfoMarkerViewWeakReference == null || reportInfoMarkerViewWeakReference.get() == null
            || reportPointMarkerViewWeakReference == null || reportPointMarkerViewWeakReference.get() == null
        ){
            return;
        }
        ReportInfoMarkerView reportInfoMarkerView = reportInfoMarkerViewWeakReference.get();
        ReportPointMarkerView reportPointMarkerView = reportPointMarkerViewWeakReference.get();
//        RoundMarker mRoundMarker = mRoundMarkerReference.get();
//        PositionMarker mPositionMarker = mPositionMarkerReference.get();

        // if there is no marker view or drawing marker is disabled
        if (!isDrawMarkersEnabled() || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);

            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1])) {
                continue;
            }

            reportInfoMarkerView.refreshContent(e, highlight);
            reportInfoMarkerView.draw(canvas, pos[0], pos[1] - reportInfoMarkerView.getHeight() / 2);
//
            reportPointMarkerView.refreshContent(e, highlight);
            reportPointMarkerView.draw(canvas, pos[0], pos[1] + reportPointMarkerView.getHeight() / 2);

            if (this.getHeight() - 50 > pos[1] + reportPointMarkerView.getHeight() / 2) {

                //绘制虚线
                Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setColor(this.getContext().getColor(R.color.color_orange));
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(3);
                mPaint.setPathEffect(new DashPathEffect(new float[]{15, 10}, 0));

                Path mPath = new Path();
                mPath.reset();
                mPath.moveTo(pos[0], pos[1] + reportPointMarkerView.getHeight() / 2);
                mPath.lineTo(pos[0], this.getHeight() - 50);
                canvas.drawPath(mPath, mPaint);
            }
        }
    }
}