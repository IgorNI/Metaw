package com.mbientlab.metawear.app;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import android.widget.Switch;

import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.mbientlab.metawear.AsyncOperation;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.MetaWearBleService;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.data.CartesianFloat;
import com.mbientlab.metawear.module.Accelerometer;

import com.mbientlab.metawear.module.Gpio;
import com.mbientlab.metawear.module.IBeacon;
import com.mbientlab.metawear.module.Logging;
import com.mbientlab.metawear.module.Timer;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

/**
 * Created by nilif on 2016/6/7.
 */
public class Test extends ActionBarActivity implements ServiceConnection{


    private MetaWearBoard metaWearBoard;
    private final String MW_MAC_ADDRESS= "C3:EC:A4:67:40:BC";

    private  Accelerometer accelModule = null;
    private Gpio gpioModule = null;
    private Timer timerModule;
    private AsyncOperation<Timer.Controller> timerController;
    private AsyncOperation<RouteManager> analogRoute;
    private AsyncOperation<RouteManager> acceleRoute;
    private final byte GPIO_PIN = 0;
    protected static final float samplingPeriod = 33/1000.f;
    protected LineChart chart;
    private int sampleCount;
    private long startTime= -1;
    private float max = 1023;
    private float min = 0;
    private static final float FPS= 30.f;
    private static final long UPDATE_PERIOD= (long) ((1 / FPS) * 1000L);
    private byte globalLayoutListenerCounter= 0;
    float acceler = 0;
    LinkedList<Float> dataArray = new LinkedList<Float>();

    private final int THRESHOLD=10;
    private int mStep = 0;
    private final int FILTER_NUM = 8;
    private final int NUMBER = 35;
    private int peak = 0;
    private float bigger = 0;
    public static final String TAG = "Sensor";
    private ArrayList<Float> dataOfAccelere = new ArrayList<Float>();
    LinkedList<Float> stepDataArray = new LinkedList<Float>();
    private String step ;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        getApplicationContext().bindService(new Intent(this, MetaWearBleService.class),
                this, Context.BIND_AUTO_CREATE);

        final View scrollView = findViewById(R.id.scrollView1);
        if (scrollView != null) {
            globalLayoutListenerCounter = 1;
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
            Button button1 = (Button) findViewById(R.id.connect);
            Button button2 = (Button) findViewById(R.id.readBattery);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectMe(v);

                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                //public static final String TAG = "Acceler";
                @Override
                public void onClick(View v) {
                    batteryMe(v);
                }
            });
        }
        chart = (LineChart) findViewById(R.id.data_chart1);
        initializeChart();
        resetData(false);
        chart.invalidate();
        chart.setDescription(null);
        // 设置表的数值范围为0-1023；
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMaxValue(max);


    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        MetaWearBleService.LocalBinder binder = (MetaWearBleService.LocalBinder) service;

        final BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice = manager.getAdapter().getRemoteDevice(MW_MAC_ADDRESS);

        binder.executeOnUiThread();
        metaWearBoard= binder.getMetaWearBoard(remoteDevice);
        metaWearBoard.setConnectionStateHandler(new MetaWearBoard.ConnectionStateHandler() {
            @Override
            public void connected() {
                Toast.makeText(Test.this, "Connected", Toast.LENGTH_LONG).show();

                Log.i("test", "Connected");
                Log.i("test", "MetaBoot? " + metaWearBoard.inMetaBootMode());


                metaWearBoard.readDeviceInformation().onComplete(new AsyncOperation.CompletionHandler<MetaWearBoard.DeviceInformation>() {
                    @Override
                    public void success(MetaWearBoard.DeviceInformation result) {
                        Log.i("test", "Device Information: " + result.toString());
                    }

                    @Override
                    public void failure(Throwable error) {
                        Log.e("test", "Error reading device information", error);
                    }
                });

                try {
                    boardReady();
                    metaWearBoard.getModule(IBeacon.class).readConfiguration().onComplete(new AsyncOperation.CompletionHandler<IBeacon.Configuration>() {
                        @Override
                        public void success(IBeacon.Configuration result) {
                            Log.i("test", result.toString());
                        }

                        @Override
                        public void failure(Throwable error) {
                            Log.e("test", "Error reading ibeacon configuration", error);
                        }
                    });
                } catch (UnsupportedModuleException e) {
                    Log.e("test", "Cannot get module", e);
                }
            }

            @Override
            public void disconnected() {
                Toast.makeText(Test.this, "Disconnected", Toast.LENGTH_LONG).show();
                Log.i("test", "Disconnected");
            }

            @Override
            public void failure(int status, final Throwable error) {
                Toast.makeText(Test.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.e("test", "Error connecting", error);
            }
        });
    }
    public void connectMe(View v) {
        metaWearBoard.connect();
    }

    public void disconnectMe(View v) {
        metaWearBoard.disconnect();
    }


    public void batteryMe(View v) {
        metaWearBoard.readBatteryLevel().onComplete(new AsyncOperation.CompletionHandler<Byte>() {
            public static final String TAG = "Read Battery";
            @Override
            public void success(final Byte result) {
                Log.w(TAG, "success: "+result);
            }

            @Override
            public void failure(Throwable error) {
                Log.e("test", "Error reading battery level", error);
            }
        });
    }

    class AcceleThread implements Runnable{
        final String TAG = "Acceler";

        @Override
        public void run() {
                    acceleRoute.onComplete(new AsyncOperation.CompletionHandler<RouteManager>() {

                        @Override
                        public void success(RouteManager result) {


                            result.subscribe("accelSub", new RouteManager.MessageHandler() {
                                @Override
                                public void process(Message msg) {
                                    final CartesianFloat axisData = msg.getData(CartesianFloat.class);

                                    acceler = (float) Math.sqrt(axisData.x() * axisData.x()
                                            + axisData.y() * axisData.y() + axisData.z() * axisData.z());
                                    Log.e(TAG, "process:"+acceler);
                                    motionGesture(acceler);
                                    bigger = acceler * 10;
                                    filter(bigger);

                                }
                            });
                            step = mStep*2+"";
                            accelModule.setOutputDataRate(50.f);
                            accelSetup = true;
                            accelModule.enableAxisSampling();
                            accelModule.start();
                            Log.e(TAG, "success:"+step);
                        }
        });
    }
    }

    class GpioThread implements Runnable{
        @Override
        public void run() {

            analogRoute.onComplete(new AsyncOperation.CompletionHandler<RouteManager>() {
                public static final String TAG = "GPIO";

                @Override
                public void success(RouteManager result) {
                    result.subscribe("analog_gpio", new RouteManager.MessageHandler() {
                        @Override
                        public void process(final Message msg) {
                            final Short gpioValue = msg.getData(Short.class);
                            Log.e(TAG, "process: " + gpioValue);
                             LineData data = chart.getData();
                           if (startTime == -1) {
                                data.addXValue("0");
                                startTime= System.currentTimeMillis();
                            } else {
                                data.addXValue(String.format("%.2f", sampleCount * samplingPeriod));
                            }
                            data.addEntry(new Entry(gpioValue, sampleCount), 0);
                            sampleCount++;
                        }
                    });
                    timerController = timerModule.scheduleTask(new Timer.Task() {
                        @Override
                        public void commands() {
                            gpioModule.readAnalogIn(GPIO_PIN, Gpio.AnalogReadMode.ADC);
                        }
                    }, 50, false);
                    timerController.onComplete(new AsyncOperation.CompletionHandler<Timer.Controller>() {
                        @Override
                        public void success(Timer.Controller result) {
                            result.start();
                        }
                    });
                }
            });
        }
    }
    private boolean accelSetup= false;
    public void accelerometerMe(View v) {
        final Switch mySwitch= (Switch) v;
        AcceleThread accThread = new AcceleThread();
        Thread thread = new Thread(accThread);
        if (mySwitch.isChecked()) {
            thread.start();
        }
        else{
            //thread.interrupt();
            accelModule.disableAxisSampling();
            accelModule.stop();
        }
    }
    public void gpioMe(View v){
        final Switch mySwitch = (Switch) v;
        if (mySwitch.isChecked()){
            GpioThread gpioThread = new GpioThread();
            new Thread(gpioThread).start();
        }
        else {
            /*try {
                analogRoute.result().remove();
            } catch (ExecutionException e) {
                Log.e("test", "Exception in route", e);
            } catch (InterruptedException e) {
                Log.e("test", "Result not ready");
            }
            timerController.onComplete(new AsyncOperation.CompletionHandler<Timer.Controller>() {
                @Override
                public void success(Timer.Controller result) {
                    result.stop();
                    result.remove();
                }
            });*/
        }
    }

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


    /*
    * 对开发板各个模块进行初始化操作。
    * */
    protected void boardReady() throws UnsupportedModuleException{
        accelModule = metaWearBoard.getModule(Accelerometer.class);
        gpioModule = metaWearBoard.getModule(Gpio.class);
        timerModule= metaWearBoard.getModule(Timer.class);
        analogRoute= gpioModule.routeData().fromAnalogIn(GPIO_PIN, Gpio.AnalogReadMode.ADC)
                       /* .process("mathprocesser")*//* "math?operation=mult&rhs=1")*/
                .stream("analog_gpio")
                .commit();
        acceleRoute= accelModule.routeData().fromAxes().stream("accelSub").commit();

    }

    private final Runnable updateChartTask= new Runnable() {
        @Override
        public void run() {
            chart.notifyDataSetChanged();
            moveViewToLast();
            chartHandler.postDelayed(updateChartTask, UPDATE_PERIOD);
        }
    };
    private final Handler chartHandler= new Handler();
    private void moveViewToLast() {
        chart.setVisibleXRangeMinimum(120);
        chart.setVisibleXRangeMaximum(120);
        if (sampleCount > 120) {
            chart.moveViewToX(sampleCount - 120);
        } else {
            chart.moveViewToX(0);
        }
    }

    private void initializeChart() {
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue(max);
        leftAxis.setAxisMinValue(min);
        chart.getAxisRight().setEnabled(false);
    }
    protected void refreshChart(boolean clearData) {
        chart.resetTracking();
        chart.clear();
        resetData(clearData);
        chart.invalidate();
        chart.fitScreen();
    }

    protected void resetData(boolean clearData) {
        if (clearData) {
            startTime= -1;
        }
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
