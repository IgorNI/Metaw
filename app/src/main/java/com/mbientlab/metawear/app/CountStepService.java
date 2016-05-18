package com.mbientlab.metawear.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.mbientlab.metawear.AsyncOperation;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.data.CartesianFloat;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Bma255Accelerometer;
import com.mbientlab.metawear.module.Bmi160Accelerometer;
import com.mbientlab.metawear.module.Mma8452qAccelerometer;

import java.util.ArrayList;
import java.util.LinkedList;

public class CountStepService extends Service {
    private static final String TAG = "CountStepService";
    AccelerometerFragment accelerometerFragment;
    ThreeAxisChartFragment threeAxis;
    private final ArrayList<Entry> xAxisData = new ArrayList<>(), yAxisData = new ArrayList<>(), zAxisData = new ArrayList<>();
    private  String dataType, streamKey;
    float acceler = 0;
    protected float samplePeriod;
    LinkedList<Float> dataArray = new LinkedList<Float>();
    protected RouteManager streamRouteManager;
    protected LineChart chart;
    protected final ArrayList<String> chartXValues= new ArrayList<>();
    protected int sampleCount;
    private   int layoutId ;
    public CountStepService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.w(TAG, "onBind: ");
       /**/
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.w(TAG, "onStart: " );

        super.onStart(intent, startId);
    }

    private Accelerometer accelModule= null;
    private static final float INITIAL_RANGE= 2.f, ACC_FREQ= 50.f;
    private static final float[] MMA845Q_RANGES= {2.f, 4.f, 8.f}, BMI160_RANGES= {2.f, 4.f, 8.f, 16.f};
    private int rangeIndex= 0;
    private static final String STREAM_KEY= "accel_stream";
    public int onStartCommand(Intent intent, int flags, int startId) {
       new Thread(new Runnable() {
           @Override
           public void run() {

           }
           }).start();



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
