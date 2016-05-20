//package com.mbientlab.metawear.app;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.mbientlab.metawear.AsyncOperation;
//import com.mbientlab.metawear.Message;
//import com.mbientlab.metawear.RouteManager;
//import com.mbientlab.metawear.data.CartesianFloat;
//import com.mbientlab.metawear.processor.Time;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.Set;
//
///*
// * Created by nilif on 2016/5/3.
//*/
//
//public abstract class CountStepFragment extends SensorFragment {
//
//    private final ArrayList<Entry> xAxisData= new ArrayList<>(), yAxisData= new ArrayList<>(), zAxisData= new ArrayList<>();
//    private final String dataType, streamKey;
//
//    protected float samplePeriod;
//    public static ArrayList<Float> xValue = new ArrayList<Float>();
//    public static ArrayList<Float> yValue = new ArrayList<Float>();
//    public static ArrayList<Float> zValue = new ArrayList<Float>();
//    private static int i = 0;
//    private final int THRESHOLD=15;
//    private final int MIN_DISTANCE=7;
//    private final int MAX_DISTANCE=40;
//    private String step ;
//    private int mStep = 0;
//    private final int FILTER_NUM = 8;
//    private final int NUMBER = 35;
//    private int peak = 0;
//    private ArrayList<Float> acceleData = new ArrayList<Float>();
//    private float bigger = 0;
//    public static final String TAG = "Sensor";
//    private ArrayList<Float> dataOfAccelere = new ArrayList<Float>();
//    // private ArrayList<Float> saveDataOfAccelere = new ArrayList<Float>();
//    LinkedList<Float> dataArray = new LinkedList<Float>();
//    LinkedList<Float> stepDataArray = new LinkedList<Float>();
//
//
//
//
//    protected final AsyncOperation.CompletionHandler<RouteManager> dataStreamManager1= new AsyncOperation.CompletionHandler<RouteManager>() {
//        @Override
//        public void success(RouteManager result) {
//            streamRouteManager = result;
//            result.subscribe(streamKey, new RouteManager.MessageHandler() {
//                @Override
//                public void process(Message message) {
//                    final CartesianFloat spin = message.getData(CartesianFloat.class);
//
//                    LineData data = chart.getData();
//                    data.addXValue(String.format("%.2f",sampleCount*samplePeriod));
//                    Log.w(TAG,"is ii");
//                    // 向表中添加数据
////                    data.addEntry(new Entry(spin.x(), sampleCount), 0);
////                    data.addEntry(new Entry(spin.y(), sampleCount), 1);
////                    data.addEntry(new Entry(spin.z(), sampleCount), 2);
//                    float acceleData = (float) Math.sqrt(spin.x() * spin.x() + spin.y() * spin.y() + spin.z() * spin.z());
//                    data.addEntry(new Entry(acceleData,sampleCount),0);
//                    motionGesture(acceleData);
//                    bigger = acceleData * 10;
//                    filter(bigger);
//
//                    //isSit(acceleData)  判断是否坐着
//                    //isStanding(acceleData) 判断是否站着
//                    //isRunning(acceleData) 判断是否跑步
//                    // new CountStepActivity().stepCount(); // 获取步数
//                    //System.out.println("bushu: "+mStep);
//                    sampleCount++;
//                }
//            });
//
//            SensorFragment.isStanding = STOPPING;
//            step = mStep*2+"";
//            SensorFragment.stepNum = mStep*2;
//
//            // 保存步数，时间
////            new DataSaveActivity().saveStep(step);
//LineData data = chart.getData();
//            // 显示步数
//            data.addXValue(String.format("%.2f", 1.0f));
//            for (int i=0;i<10;i++) {
//                data.addEntry(new Entry(mStep * 2, i), 0);
//            }
//
//        }
//    };
//
//
//    @Override
//    protected void resetData(boolean clearData) {
//        if (clearData) {
//            sampleCount = 0;
//            chartXValues.clear();
//            xAxisData.clear();
//            yAxisData.clear();
//            zAxisData.clear();
//        }
//
//        ArrayList<LineDataSet> spinAxisData= new ArrayList<>();
//        spinAxisData.add(new LineDataSet(xAxisData, "x-" + dataType));
//        spinAxisData.get(0).setColor(Color.RED);
//        spinAxisData.get(0).setDrawCircles(false);
//
////        spinAxisData.add(new LineDataSet(yAxisData, "y-" + dataType));
////        spinAxisData.get(1).setColor(Color.GREEN);
////        spinAxisData.get(1).setDrawCircles(false);
////
////        spinAxisData.add(new LineDataSet(zAxisData, "z-" + dataType));
////        spinAxisData.get(2).setColor(Color.BLUE);
////        spinAxisData.get(2).setDrawCircles(false);
//
//        LineData data= new LineData(chartXValues);
//        for(LineDataSet set: spinAxisData) {
//            data.addDataSet(set);
//        }
//        data.setDrawValues(false);
//        chart.setData(data);
//    }
//
//
//    private static int MOTION_NUM = 32;
//    private final static float normal_acceler = 1.00f;// 定义一个一般情况下的加速度。
//    private static float static_threshold = 0.02f;
//    private static float sit_to_stand_maxthreshold = 0.14f;
//    private static float sit_to_stand_minthreshold = 0.10f;
//    private static float stand_to_sit_maxthreshold = 0.19f;
//    private static float stand_to_sit_minthreshold = 0.12f;
//    private static int SIT_OR_STAND = 0;
//    private static int STATE  = 0;
//    private static int MOVING = 0;
//    private static  int STOPPING = 0;
//    private static final int STANDING = 1;
//    private static final int SITTING = 2;
//    private static int maxIndex = 0;
//    private static int minIndex = 0;
//
//
//
//    private void motionGesture(float acceler) {
//        float sum = 0;
//        float average = 0;
//        dataArray.add(acceler);
//        // 一个站-坐或者坐-站的动作大约为0.64s，能采集32个值。
//        if (dataArray.size() == MOTION_NUM) {
//            float a = Math.abs(getMax(dataArray) - normal_acceler); // 值为正
//            float b = Math.abs(normal_acceler - getMin(dataArray)); // 值为负
//if (a > 0.9f) {
//                MOVING = 1;
//            }
//
//
//            int maxIndex = dataArray.indexOf(getMax(dataArray));
//            int minIndex = dataArray.indexOf(getMin(dataArray));
//            if (a < static_threshold && b < static_threshold) {
//                STOPPING = 1;
//                Log.d("now", "is" + STOPPING);
//            }
//            // 如果超过了阈值
//            if (a > sit_to_stand_maxthreshold && b > sit_to_stand_minthreshold) {
//                if (SIT_OR_STAND == 0) {
//                    Log.d("now", "the gesture is 坐 - 站");
//                    SIT_OR_STAND = 1;
//                }else {
//                    Log.d("now", "the gesture is 站 - 坐");
//                    SIT_OR_STAND = 0;
//                }
//            }
//
//
//            else {
//                Log.d("now", "the gesture is ....");
////                Log.w("STOPPING","is"+STOPPING);
//            }
//            dataArray.clear();
//        }
//
//        if(STOPPING == 1 && SIT_OR_STAND == 1) {
//            STATE = STANDING;
//            Log.d("now","the gesture is 站立"+STATE);
//            // TODO: 2016/5/13 更新状态
//            SensorFragment.state = STATE;
//        }
//        if(STOPPING == 1&& SIT_OR_STAND == 0){
//            STATE = SITTING;
//            Log.d("now","the gesture is 坐"+STATE);
//            //// TODO: 2016/5/13 更新状态
//            SensorFragment.state = STATE;
//        }
//
//    }
//
//
//
//    private float getMax(LinkedList<Float> data) {
//        float max = 0;
//        max = data.get(0);
//        for(int i = 0;i < MOTION_NUM;i++){
//            if(max < data.get(i)){
//                max = data.get(i);
//            }
//        }
//        return max;
//    }
//    private float getMin(LinkedList<Float> data) {
//        float min = 0;
//        min = data.get(0);
//        for(int i = 0;i < MOTION_NUM;i++){
//            if(min > data.get(i)){
//                min = data.get(i);
//            }
//        }
//        return min;
//    }
//public float getxValue(){
//        return xValue;
//    }
//
//    public static LineData getLineData(){
//        return chart.getLineData();
//    }
//
//    private void sumPeakAndValley(ArrayList<Float> data){
//
//        ArrayList<Float>buffer=new ArrayList<Float>();
//        ArrayList<Integer>peakposition=new ArrayList<Integer>();
//        ArrayList<Integer>valleryposition=new ArrayList<Integer>();
//        float threshold=0;
//        int flag=0;
//        int distance=0;
//        for(int i=0;i<NUMBER-1;i++){
//            buffer.add(data.get(i+1)-data.get(i));
//        }
//        for(int i=0;i<buffer.size()-1;i++){
//            if(buffer.get(i)>0 && buffer.get(i+1)<0){
//                peakposition.add(i+1); // 波峰
//            }
//            if(buffer.get(i)<0 && buffer.get(i+1)>0){
//                valleryposition.add(i+1); // 波谷
//            }
//Log.w(TAG,"peakNum: "+peakposition.size());
//            Log.w(TAG,"valleryNum: "+valleryposition.size());
//
//        }
//
//        if((peakposition.size()>valleryposition.size() ||
//                peakposition.size()<valleryposition.size())&& peakposition.size()!=0 && valleryposition.size()!=0){
//            while(flag<peakposition.size() && flag<valleryposition.size()){
//                distance=Math.abs(peakposition.get(flag)-valleryposition.get(flag));
//                threshold=data.get(peakposition.get(flag))-data.get(valleryposition.get(flag));
//                if (threshold>0 && threshold<1) {
//                    System.out.println("静止");
//
//                }
//                if( threshold>THRESHOLD ){
//                    peak++;
//
//                }
//                flag++;
//            }
//            threshold=data.get(peakposition.get(peakposition.size()-1))-data.get(
//                    valleryposition.get(valleryposition.size()-1));
//            distance=Math.abs(peakposition.get(peakposition.size()-1)-valleryposition.get(valleryposition.size()-1));
//            if (threshold>0 && threshold<1) {
//                System.out.println("静止");
//
//            }
//            if( threshold>THRESHOLD){
//                peak++;
//
//            }
//        }
//        if(peakposition.size()==valleryposition.size())
//        {
//            while(flag<peakposition.size() && flag<valleryposition.size()){
//                distance=Math.abs(peakposition.get(flag)-valleryposition.get(flag));
//                threshold=data.get(peakposition.get(flag))-data.get(valleryposition.get(flag));
//                if (threshold>0 && threshold<1) {
//                    System.out.println("静止");
//
//                }
//                if( threshold>THRESHOLD ){
//
//                    peak++;
//                }
//                flag++;
//            }
//        }
//        mStep = peak;
//    }
//
//    private void filter(float data) {
//        float sum = 0;
//        float averger = 0;
//        stepDataArray.addLast(data);
//        if (stepDataArray.size() == FILTER_NUM){
//            for (int i=0;i<FILTER_NUM;i++) {
//                sum+=stepDataArray.get(i);
//            }
//            averger = sum/FILTER_NUM;
//            dataOfAccelere.add(averger);
//            if (dataOfAccelere.size() == NUMBER){
//                sumPeakAndValley(dataOfAccelere);
//                stepNum += peak;
//                dataOfAccelere.clear();
//                // peak = 0;
//            }
//            Log.e(TAG,"pingjung"+averger);
//            stepDataArray.removeFirst();
//        }
//    }
//
//private boolean isMoving(){
//
//    }
//
//    private float average;
//
// protected  void tempData(float data) {
//         ArrayList<Float> tempArrayData = new ArrayList<>();
//
//         while (tempArrayData.size()<1000) {
//
//                 tempArrayData.add(data);
//
//if (data>0&&data<1) {i++;}
//                if (data>-1&&data<0) {Log.w("zitai","zhanzhe");}
//
//            tempArrayData.add(data);
//            temp += tempArrayData.get(i);
//        }
//
//average = temp/1000;
//        if (average > 0 && average <1)
//        {
//
//        }
//        //存储了100个值后，
//        Log.w("sensor","the data is more than 100");
//
//
//
//    }
//
//    protected CountStepFragment(String dataType, int layoutId, int sensorResId, String streamKey, float min, float max, float sampleFreq) {
//        super(sensorResId, layoutId, min, max);
//        this.dataType= dataType;
//        this.streamKey= streamKey;
//        this.samplePeriod= 1 / sampleFreq;
//        LineData data = chart.getData();
//    }
//
//    protected CountStepFragment(String dataType, int layoutId, int sensorResId, String streamKey, float min, float max) {
//        super(sensorResId, layoutId, min, max);
//        this.dataType= dataType;
//        this.streamKey= streamKey;
//        this.samplePeriod= -1.f;
//    }
//
//    @Override
//    protected String saveData() {
//        final String CSV_HEADER = String.format("time,x-%s,y-%s,z-%s%n", dataType, dataType, dataType);
//        String filename = String.format("%s_%tY%<tm%<td-%<tH%<tM%<tS%<tL.csv", getContext().getString(sensorResId), Calendar.getInstance());
//
//        try {
//            FileOutputStream fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
//            fos.write(CSV_HEADER.getBytes());
//
//            LineData data = chart.getLineData();
//            LineDataSet xSpinDataSet = data.getDataSetByIndex(0), ySpinDataSet = data.getDataSetByIndex(1),
//                    zSpinDataSet = data.getDataSetByIndex(2);
//            for (int i = 0; i < data.getXValCount(); i++) {
//                fos.write(String.format("%.3f,%.3f,%.3f,%.3f%n", i * samplePeriod,
//                        xSpinDataSet.getEntryForXIndex(i).getVal(),
//                        ySpinDataSet.getEntryForXIndex(i).getVal(),
//                        zSpinDataSet.getEntryForXIndex(i).getVal()).getBytes());
//            }
//            fos.close();
//            return filename;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
