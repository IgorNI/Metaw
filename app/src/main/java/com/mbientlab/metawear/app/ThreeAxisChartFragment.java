package com.mbientlab.metawear.app;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mbientlab.metawear.AsyncOperation;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.data.CartesianFloat;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by nilif on 2016/5/3.
 */
public abstract class ThreeAxisChartFragment extends SensorFragment {

    private final ArrayList<Entry> xAxisData= new ArrayList<>(), yAxisData= new ArrayList<>(), zAxisData= new ArrayList<>();
    private final String dataType, streamKey;
    protected float samplePeriod;
    private final float[] mData = new float[2];
    public  static ArrayList<Float> xValue = new ArrayList<Float>();
    public  static ArrayList<Float> yValue = new ArrayList<Float>();
    public  static ArrayList<Float> zValue = new ArrayList<Float>();


    protected final AsyncOperation.CompletionHandler<RouteManager> dataStreamManager= new AsyncOperation.CompletionHandler<RouteManager>() {
        @Override
        public void success(RouteManager result) {
            streamRouteManager= result;
            result.subscribe(streamKey, new RouteManager.MessageHandler() {
                @Override
                public void process(Message message) {
                    final CartesianFloat spin = message.getData(CartesianFloat.class);

                    LineData data = chart.getData();

                    data.addXValue(String.format("%.2f", sampleCount * samplePeriod));
                    ArrayList<HashMap<String,Float>> mData = new ArrayList<HashMap<String, Float>>();
                    data.addEntry(new Entry(spin.x(), sampleCount), 0);
                    data.addEntry(new Entry(spin.y(), sampleCount), 1);
                    data.addEntry(new Entry(spin.z(), sampleCount), 2);

                    xValue.add(spin.x());
                    yValue.add(spin.y());
                    zValue.add(spin.z());
                    // 是静止还是运动的判断
                    // new CountStepActivity().stepCount(); // 获取步数
                }
            });
        }

    };

    protected ThreeAxisChartFragment(String dataType, int layoutId, int sensorResId, String streamKey, float min, float max, float sampleFreq) {
        super(sensorResId, layoutId, min, max);
        this.dataType= dataType;
        this.streamKey= streamKey;
        this.samplePeriod= 1 / sampleFreq;
        LineData data = chart.getData();
    }

    protected ThreeAxisChartFragment(String dataType, int layoutId, int sensorResId, String streamKey, float min, float max) {
        super(sensorResId, layoutId, min, max);
        this.dataType= dataType;
        this.streamKey= streamKey;
        this.samplePeriod= -1.f;
    }

    @Override
    protected String saveData() {
        final String CSV_HEADER = String.format("time,x-%s,y-%s,z-%s%n", dataType, dataType, dataType);
        String filename = String.format("%s_%tY%<tm%<td-%<tH%<tM%<tS%<tL.csv", getContext().getString(sensorResId), Calendar.getInstance());

        try {
            FileOutputStream fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(CSV_HEADER.getBytes());

            LineData data = chart.getLineData();
            LineDataSet xSpinDataSet = data.getDataSetByIndex(0), ySpinDataSet = data.getDataSetByIndex(1),
                    zSpinDataSet = data.getDataSetByIndex(2);
            for (int i = 0; i < data.getXValCount(); i++) {
                fos.write(String.format("%.3f,%.3f,%.3f,%.3f%n", i * samplePeriod,
                        xSpinDataSet.getEntryForXIndex(i).getVal(),
                        ySpinDataSet.getEntryForXIndex(i).getVal(),
                        zSpinDataSet.getEntryForXIndex(i).getVal()).getBytes());
            }
            fos.close();
            return filename;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void resetData(boolean clearData) {
        if (clearData) {
            sampleCount = 0;
            chartXValues.clear();
            xAxisData.clear();
            yAxisData.clear();
            zAxisData.clear();
        }

        ArrayList<LineDataSet> spinAxisData= new ArrayList<>();
        spinAxisData.add(new LineDataSet(xAxisData, "x-" + dataType));
        spinAxisData.get(0).setColor(Color.RED);
        spinAxisData.get(0).setDrawCircles(false);

        spinAxisData.add(new LineDataSet(yAxisData, "y-" + dataType));
        spinAxisData.get(1).setColor(Color.GREEN);
        spinAxisData.get(1).setDrawCircles(false);

        spinAxisData.add(new LineDataSet(zAxisData, "z-" + dataType));
        spinAxisData.get(2).setColor(Color.BLUE);
        spinAxisData.get(2).setDrawCircles(false);

        LineData data= new LineData(chartXValues);
        for(LineDataSet set: spinAxisData) {
            data.addDataSet(set);
        }
        data.setDrawValues(false);
        chart.setData(data);
    }


    /*public float getxValue(){
        return xValue;
    }*/
    public static LineData getLineData(){
        return chart.getLineData();
    }
    public interface MyInterface{
        public void sendMsg(Object object);
    }

}

