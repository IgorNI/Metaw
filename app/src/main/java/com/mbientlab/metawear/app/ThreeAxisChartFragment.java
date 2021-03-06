package com.mbientlab.metawear.app;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mbientlab.metawear.AsyncOperation;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.data.CartesianFloat;
import com.mbientlab.metawear.processor.Time;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created by nilif on 2016/5/3.
 */
public abstract class ThreeAxisChartFragment extends ModuleFragmentBase {
    private final ArrayList<Entry> xAxisData = new ArrayList<>(), yAxisData = new ArrayList<>(), zAxisData = new ArrayList<>();
    private final String dataType, streamKey;
    float acceler = 0;
    protected float samplePeriod;
    LinkedList<Float> dataArray = new LinkedList<Float>();
    protected RouteManager streamRouteManager;
    protected LineChart chart;
    protected final ArrayList<String> chartXValues= new ArrayList<>();
    protected int sampleCount;
    private  final int layoutId ;
    SqliteHelper sqliteHelper = null;
    SQLiteDatabase db = null;

    private static int i = 0;
    private final int THRESHOLD=10;
    private final int MIN_DISTANCE=7;
    private final int MAX_DISTANCE=40;
    private String step ;
    private int mStep = 0;
    private final int FILTER_NUM = 8;
    private final int NUMBER = 35;
    private int peak = 0;
    private ArrayList<Float> acceleData = new ArrayList<Float>();
    private float bigger = 0;
    public static final String TAG = "Sensor";
    private ArrayList<Float> dataOfAccelere = new ArrayList<Float>();
    // private ArrayList<Float> saveDataOfAccelere = new ArrayList<Float>();
    LinkedList<Float> stepDataArray = new LinkedList<Float>();

    protected final AsyncOperation.CompletionHandler<RouteManager> dataStreamManager = new AsyncOperation.CompletionHandler<RouteManager>() {
        @Override
        public void success(RouteManager result) {
            streamRouteManager = result;
            result.subscribe(streamKey, new RouteManager.MessageHandler() {
                @Override
                public void process(Message message) {
                    final CartesianFloat spin = message.getData(CartesianFloat.class);

                    LineData data = chart.getData();
                    acceler = (float) Math.sqrt(spin.x() * spin.x() + spin.y() * spin.y() + spin.z() * spin.z());
                    data.addXValue(String.format("%.2f", sampleCount * samplePeriod));
                   // data.addEntry(new Entry(acceler, sampleCount), 0);
                    data.addEntry(new Entry(acceler, sampleCount), 0);
                    motionGesture(acceler);
                    bigger = acceler * 10;
                    filter(bigger);

                    /*data.addEntry(new Entry(spin.x(), sampleCount), 0);
                    data.addEntry(new Entry(spin.y(), sampleCount), 1);
                    data.addEntry(new Entry(spin.z(), sampleCount), 2);*/

                    sampleCount++;

                }
            });
            //isStanding = STOPPING;
            step = mStep*2+"";
        }
    };


    private static int MOTION_NUM = 32;
    private final static float normal_acceler = 1.00f;// 定义一个一般情况下的加速度。
    private static float static_threshold = 0.02f;
    private static float sit_to_stand_maxthreshold = 0.14f;
    private static float sit_to_stand_minthreshold = 0.10f;
    private static float stand_to_sit_maxthreshold = 0.19f;
    private static float stand_to_sit_minthreshold = 0.12f;
    private static int SIT_OR_STAND = 0; // 站
    private static int STATE = 0;
    private static int MOVING = 0;
    private static int STOPPING = 1;
    private static final int STANDING = 1;
    private static final int SITTING = 2;
    private static final int WALKING = 3;
    private static int maxIndex = 0;
    private static int minIndex = 0;
    private Handler chartHandler = new Handler();
    private static final float FPS= 30.f;
    private static final long UPDATE_PERIOD= (long) ((1 / FPS) * 1000L);
    private Runnable updateChartTask = new Runnable() {
        @Override
        public void run() {
            chart.notifyDataSetChanged();
            moveViewToLast();
            chartHandler.postDelayed(updateChartTask, UPDATE_PERIOD);
        }
    };


    private void motionGesture(float acceler) {
        float sum = 0;
        float average = 0;
        dataArray.add(acceler);
        // 一个站-坐或者坐-站的动作大约为0.64s，能采集32个值。
        if (dataArray.size() == MOTION_NUM) {

            float a = Math.abs(getMax(dataArray) - normal_acceler); // 值为正
            float b = Math.abs(normal_acceler - getMin(dataArray)); // 值为负
                int maxIndex = dataArray.indexOf(getMax(dataArray));
                int minIndex = dataArray.indexOf(getMin(dataArray));
                if (a < static_threshold && b < static_threshold) {
                    STOPPING = 1;
                    Log.w("now", "is" + STOPPING);
                }
                // 如果超过了阈值
                if (a > sit_to_stand_maxthreshold && b > sit_to_stand_minthreshold) {
                    if (SIT_OR_STAND == 1) {
                        Log.w("now", "the gesture is 坐 - 站");
                        SIT_OR_STAND = 0;   // 站着为0
                    } else {
                        Log.w("now", "the gesture is 站 - 坐");
                        SIT_OR_STAND = 1;  // 坐着为1
                    }
                } else {
                    Log.w("now", "the gesture is ....");
//                Log.w("STOPPING","is"+STOPPING);
                }
                dataArray.clear();
        }
        if (STOPPING == 1 && SIT_OR_STAND == 0) {
            STATE = STANDING;
            Log.w("now", "the gesture is 站立" + STATE);
            // TODO: 2016/5/13 更新状态
        }
        if (STOPPING == 1 && SIT_OR_STAND == 1) {
            STATE = SITTING;
            Log.w("now", "the gesture is 坐" + STATE);
            //// TODO: 2016/5/13 更新状态
        }


    }


    private float getMax(LinkedList<Float> data) {
        float max = 0;
        max = data.get(0);
        for (int i = 0; i < MOTION_NUM; i++) {
            if (max < data.get(i)) {
                max = data.get(i);
            }
        }
        return max;
    }

    private float getMin(LinkedList<Float> data) {
        float min = 0;
        min = data.get(0);
        for (int i = 0; i < MOTION_NUM; i++) {
            if (min > data.get(i)) {
                min = data.get(i);
            }
        }
        return min;
    }

    private void sumPeakAndValley(ArrayList<Float> data){

        ArrayList<Float>buffer=new ArrayList<Float>();
        ArrayList<Integer>peakposition=new ArrayList<Integer>();
        ArrayList<Integer>valleryposition=new ArrayList<Integer>();
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

/*Log.w(TAG,"peakNum: "+peakposition.size());
            Log.w(TAG,"valleryNum: "+valleryposition.size());*/

        }

        if((peakposition.size()>valleryposition.size() ||
                peakposition.size()<valleryposition.size())&& peakposition.size()!=0 && valleryposition.size()!=0){
            while(flag<peakposition.size() && flag<valleryposition.size()){
                distance=Math.abs(peakposition.get(flag)-valleryposition.get(flag));
                threshold=data.get(peakposition.get(flag))-data.get(valleryposition.get(flag));
                if (threshold>0 && threshold<1) {
                    System.out.println("静止");
                    STOPPING = 1;
                }
                if( threshold>THRESHOLD ){
                    peak++;
                    STOPPING = 0;
                    STATE = WALKING;
                }
                flag++;
            }
            threshold=data.get(peakposition.get(peakposition.size()-1))-data.get(
                    valleryposition.get(valleryposition.size()-1));
            distance=Math.abs(peakposition.get(peakposition.size()-1)-valleryposition.get(valleryposition.size()-1));
            if (threshold>0 && threshold<1) {
                System.out.println("静止");
                STOPPING = 1;
            }
            if( threshold>THRESHOLD){
                peak++;
                STOPPING = 0;
                STATE = WALKING;
            }
        }
        if(peakposition.size()==valleryposition.size())
        {
            while(flag<peakposition.size() && flag<valleryposition.size()){
                distance=Math.abs(peakposition.get(flag)-valleryposition.get(flag));
                threshold=data.get(peakposition.get(flag))-data.get(valleryposition.get(flag));
                if (threshold>0 && threshold<1) {
                    System.out.println("静止");
                    STOPPING = 1;
                }
                if( threshold>THRESHOLD ){
                    STOPPING = 0;
                    STATE = WALKING;
                    peak++;
                }
                flag++;
            }
        }
        mStep = peak;
    }

    private void filter(float data) {
        float sum = 0;
        float averger = 0;
        stepDataArray.addLast(data);
        if (stepDataArray.size() == FILTER_NUM){
            for (int i=0;i<FILTER_NUM;i++) {
                sum+=stepDataArray.get(i);
            }
            averger = sum/FILTER_NUM;
            dataOfAccelere.add(averger);
            if (dataOfAccelere.size() == NUMBER){
                sumPeakAndValley(dataOfAccelere);
                mStep += peak;
                dataOfAccelere.clear();

                // peak = 0;
            }
            Log.e(TAG,"pingjung"+averger);
            stepDataArray.removeFirst();
        }
    }
    protected ThreeAxisChartFragment(String dataType, int layoutId, int sensorResId, String streamKey, float min, float max) {
        this.dataType = dataType;
        this.layoutId = layoutId;
        this.sensorResId = sensorResId;
        this.streamKey = streamKey;
        this.min = min;
        this.max = max;
        this.samplePeriod = -1.f;
    }

   // @Override
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

    //@Override
    protected void resetData(boolean clearData) {
        if (clearData) {
            sampleCount = 0;
            chartXValues.clear();
            xAxisData.clear();
            yAxisData.clear();
            zAxisData.clear();
        }

        ArrayList<LineDataSet> spinAxisData = new ArrayList<>();
        spinAxisData.add(new LineDataSet(xAxisData, "x-" + dataType));
        spinAxisData.get(0).setColor(Color.RED);
        spinAxisData.get(0).setDrawCircles(false);

        /*spinAxisData.add(new LineDataSet(yAxisData, "y-" + dataType));
        spinAxisData.get(1).setColor(Color.GREEN);
        spinAxisData.get(1).setDrawCircles(false);

        spinAxisData.add(new LineDataSet(zAxisData, "z-" + dataType));
        spinAxisData.get(2).setColor(Color.BLUE);
        spinAxisData.get(2).setDrawCircles(false);*/

        LineData data = new LineData(chartXValues);
        for (LineDataSet set : spinAxisData) {
            data.addDataSet(set);
        }
        data.setDrawValues(false);
        chart.setData(data);
    }
    protected void refreshChart(boolean clearData) {
        chart.resetTracking();
        chart.clear();
        resetData(clearData);
        chart.invalidate();
        chart.fitScreen();
    }
    private byte globalLayoutListenerCounter= 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View v= inflater.inflate(R.layout.fragment_sensor_config_spinner, container, false);
        final View scrollView = v.findViewById(R.id.scrollView);
        if (scrollView != null) {
            globalLayoutListenerCounter= 1;
            scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    LineChart.LayoutParams params = chart.getLayoutParams();
                    params.height = scrollView.getHeight();
                    chart.setLayoutParams(params);
                    globalLayoutListenerCounter--;
                    if (globalLayoutListenerCounter < 0) {
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }

        return v;
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss ");
        final Date curDate = new Date(System.currentTimeMillis());

        chart = (LineChart) view.findViewById(R.id.data_chart);

        initializeChart();
        resetData(false);
        chart.invalidate();
        chart.setDescription(null);

        Button clearButton= (Button) view.findViewById(R.id.layout_two_button_left);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshChart(true);
            }
        });
        clearButton.setText(R.string.label_clear);


       /* moveViewToLast();
        setup();
        chartHandler.postDelayed(updateChartTask, UPDATE_PERIOD);*/
        ((Switch) view.findViewById(R.id.sample_control)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //          send a SamlingFlag
                if (b) {
                    moveViewToLast();
                    setup();
                    chartHandler.postDelayed(updateChartTask, UPDATE_PERIOD);
                } else {
                    //Log.w("ss","ss");
                    chart.setVisibleXRangeMinimum(1);
                    chart.setVisibleXRangeMaximum(sampleCount);
                    clean();
                    PersonDao personDao = new PersonDao(getActivity());
                    Person person = null;
//                    personDao.creatTable();

                    personDao.addInfor(0f,0f,0f,0f,mStep*2,STATE,formater.format(curDate));
//                    personDao.addInfor(1f,1f,1f,1f,2,2,"222");
                    //personDao.getInfor();
//                    personDao.addInfor(0,0,0,0,1,0,"ss");
                    //personDao.addStep(1);
                    //personDao.addInfor(0f,0f,0f,0f,1,0, "222");
                   // personDao.deleteTable();
                    System.out.println("stepNumber: "+mStep*2);
                    TextView textView = (TextView) view.findViewById(R.id.ShowStepNum);
                    textView.setText(String.valueOf(mStep*2));
                    TextView textView1 = (TextView) view.findViewById(R.id.showState);
                    if(STATE == 1){
                        textView1.setText("站");
                    }
                    if (STATE == 2){
                        textView1.setText("坐");
                    }


                    if (streamRouteManager != null) {
                        streamRouteManager.remove();
                        streamRouteManager = null;
                    }
                    chartHandler.removeCallbacks(updateChartTask);
                   /* Thread thread = new Thread(new VisitWebRunnable());
                    thread.start();*/
                }
            }
        });
        Button saveButton= (Button) view.findViewById(R.id.layout_two_button_right);
        saveButton.setText(R.string.label_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = saveData();

                if (filename != null) {
                    File dataFile = getActivity().getFileStreamPath(filename);
                    Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.mbientlab.metawear.app.fileprovider", dataFile);

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, filename);
                    intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    startActivity(Intent.createChooser(intent, "Saving Data"));
                }
            }
        });
    }

    protected float max;
    protected float min;
    protected void initializeChart() {
        ///< configure axis settings
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue(max);
        leftAxis.setAxisMinValue(min);
        chart.getAxisRight().setEnabled(false);
    }

    private void moveViewToLast() {
        chart.setVisibleXRangeMinimum(120);
        chart.setVisibleXRangeMaximum(120);
        if (sampleCount > 120) {
            chart.moveViewToX(sampleCount - 120);
        } else {
            chart.moveViewToX(0);
        }
    }
    protected abstract void setup();
    protected abstract void clean();

}

