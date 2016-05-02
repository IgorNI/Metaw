package com.mbientlab.metawear.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.nfc.Tag;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by nilif on 2016/4/25.
 */
public class CountStepActivity extends Activity{


    private final int STANDING = 0; // 站着
    private final int LYING = 1; // 平躺
    private final int THRESHOLD=8;
    private final int MIN_DISTANCE=7;
    private final int MAX_DISTANCE=40;
    private int mStep = 0;
    private final int FILTER_NUM = 8;
    private final int NUMBER = 20;
    private int peak = 0;
    private int count = 0;
    public TextView textView;
    private ArrayList<Float> acceleData = new ArrayList<Float>();
    private float bigger = 0;
    public static final String TAG = "Sensor";
    private ArrayList<Float> dataOfAccelere = new ArrayList<Float>();
    private ArrayList<Float> saveDataOfAccelere = new ArrayList<Float>();
    LinkedList<Float> dataArray = new LinkedList<Float>();
    private ArrayList<Float>buffer=new ArrayList<Float>();
    private ArrayList<Integer>peakposition=new ArrayList<Integer>();
    private ArrayList<Integer>valleryposition=new ArrayList<Integer>();

    public void stepCount() {
        // 如果仍旧在采集数据，则保存这些数据
        LineData data = ThreeAxisChartFragment.getLineData();
        if (isSampling()) {
            System.out.println("is sampling...");
        } else {
            for (int i = 0; i < ThreeAxisChartFragment.xValue.size(); i++) {
                acceleData.add((float) Math.sqrt(ThreeAxisChartFragment.xValue.get(i) * ThreeAxisChartFragment.xValue.get(i) +
                        ThreeAxisChartFragment.yValue.get(i) * ThreeAxisChartFragment.yValue.get(i) +
                        ThreeAxisChartFragment.zValue.get(i) * ThreeAxisChartFragment.zValue.get(i)));
                bigger = acceleData.get(i) * 10;
                //getStatutes(acceleData.get(i));
                filter(bigger);
            }
            // 取数据，计算步数。
            Log.e(TAG, "peak" + peakposition.size());
            Log.e(TAG, "valley: " + valleryposition.size());
            if (peakposition.size() == 0 && valleryposition.size() == 0)
            {
                System.out.println("静止");
            }else {
                countStep(); // 计算步数
                System.out.println("stepNumber: " + getStepNumber());
                //((TextView)findViewById(R.id.ShowStepNum)).setText("12");
                /*new AlertDialog.Builder(CountStepActivity.this)

                        .setTitle("标题")

                        .setMessage("计步结果为：" + getStringNum())

                        .setPositiveButton("确定", null)

                        .show();*/
            }

        }
    }
    public boolean isSampling () {
        if (SensorFragment.getSamplingFlag())
            return true;
        else
            return false;
    }


    private void sumPeakAndValley(ArrayList<Float> data){

        float threshold=0;
        int flag=0;
        int distance=0;
        for(int i=0;i<NUMBER-1;i++){
            buffer.add(data.get(i+1)-data.get(i));
        }
        for(int i=0;i<buffer.size()-1;i++){
            if(buffer.get(i)>0 && buffer.get(i+1)<0){
                peakposition.add(i+1); // 波峰
            }
            if(buffer.get(i)<0 && buffer.get(i+1)>0){
                valleryposition.add(i+1); // 波谷
            }
            Log.w(TAG,"peakNum: "+peakposition.size());
            Log.w(TAG,"valleryNum: "+valleryposition.size());
        }
        /**/
    }

    private void filter(float data) {
         float sum = 0;
        float averger = 0;
        dataArray.addLast(data);
        if (dataArray.size() == FILTER_NUM){
            for (int i=0;i<FILTER_NUM;i++) {
                sum+=dataArray.get(i);
            }
            averger = sum/FILTER_NUM;
            dataOfAccelere.add(averger);
            saveDataOfAccelere.add(averger);
            if (dataOfAccelere.size() == NUMBER){
                sumPeakAndValley(dataOfAccelere);
                dataOfAccelere.clear();
            }
           Log.e(TAG,"pingjung"+averger);
            dataArray.removeFirst();
        }
    }
    public void countStep() {

        float threshold=0;
        int flag=0;
        int distance=0;
       /* if((peakposition.size()==0 || valleryposition.size()==0)&&!(peakposition.size()==0&&
                valleryposition.size()==0)){
            float min=0,max=0;
            int position=0;
            if(peakposition.size()==0){
                max=getMax(saveDataOfAccelere,position);
                threshold=max-saveDataOfAccelere.get(valleryposition.get(0));
                distance=Math.abs(position-valleryposition.get(0));
                if( threshold>THRESHOLD){
                    peak++;
                }
            }
            if(valleryposition.size()==0){
                min=getMin(saveDataOfAccelere,position);
                threshold=saveDataOfAccelere.get(peakposition.get(0));
                distance=Math.abs(position-peakposition.get(0));
                if( threshold>THRESHOLD ){
                    peak++;
                }
            }
        }
        if(peakposition.size()==0 && valleryposition.size()==0){
            float max=0,min=0;
            int maxPosition=0,minPosition=0;
            threshold=max-min;
            distance=Math.abs(minPosition-maxPosition);
            if( threshold>THRESHOLD ){
                peak++;
            }

        }*/
        if((peakposition.size()>valleryposition.size() ||
                peakposition.size()<valleryposition.size())&& peakposition.size()!=0 && valleryposition.size()!=0){
            while(flag<peakposition.size() && flag<valleryposition.size()){
                distance=Math.abs(peakposition.get(flag)-valleryposition.get(flag));
                threshold=saveDataOfAccelere.get(peakposition.get(flag))-saveDataOfAccelere.get(valleryposition.get(flag));
                if( threshold>THRESHOLD ){
                    peak++;
                }
                flag++;
            }
            threshold=saveDataOfAccelere.get(peakposition.get(peakposition.size()-1))-saveDataOfAccelere.get(
                    valleryposition.get(valleryposition.size()-1));
            distance=Math.abs(peakposition.get(peakposition.size()-1)-valleryposition.get(valleryposition.size()-1));
            if( threshold>THRESHOLD){
                peak++;
            }
        }
        if(peakposition.size()==valleryposition.size())
        {
            while(flag<peakposition.size() && flag<valleryposition.size()){
                distance=Math.abs(peakposition.get(flag)-valleryposition.get(flag));
                threshold=saveDataOfAccelere.get(peakposition.get(flag))-saveDataOfAccelere.get(valleryposition.get(flag));
                if( threshold>THRESHOLD ){
                    peak++;
                }
                flag++;
            }
        }
        mStep = peak;
    }

    // 获取最大值
    private float getMax(ArrayList<Float>data,int position){
        float max=0;
        max=data.get(0);
        for(int i=1;i<data.size();i++){
            if(max<data.get(i)){
                max=data.get(i);
                position=i;
            }
        }
        return max;
    }

   //获取最小值
    private float getMin(ArrayList<Float>data,int position){
        float min=0;
        min=data.get(0);
        for(int i=1;i<data.size();i++){
            if(min>data.get(i)){
                min=data.get(i);
                position=i;
            }
        }
        return min;
    }

 /*public int getStatutes(float data) {
     if () {
         return STANDING;
     }

     return LYING;


 }*/
    public int getStepNumber(){
        return mStep;
    }
    public String getStringNum() {
        return String.valueOf(mStep);
    }
}
