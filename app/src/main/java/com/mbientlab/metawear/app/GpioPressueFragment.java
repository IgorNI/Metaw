package com.mbientlab.metawear.app;

/**
 * Created by nilif on 2016/5/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.mbientlab.metawear.AsyncOperation;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.app.help.HelpOption;
import com.mbientlab.metawear.app.help.HelpOptionAdapter;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Gpio;
import com.mbientlab.metawear.module.Timer;

import java.util.HashMap;
import java.util.Map;

/*import httpclient.http.HttpUrl;
import httpclient.service.PatientService;*/
import http.http.HttpUrl;
import http.service.PatientService;


public class GpioPressueFragment extends SingleDataSensorFragment {
    private static final String STREAM_KEY= "gpio_stream";
    private static final byte READ_ADC= 0, READ_ABS_REF= 1, READ_DIGITAL= 2, DEFAULT_GPIO_PIN= 0;
    private static final byte CHART1 = 0,CHART2 = 1, CHART3 = 2,CHART4 = 3;
    private static final int GPIO_SAMPLE_PERIOD= 33;
    public static float temp;
    private long time = System.currentTimeMillis();
    private static final int[] CONTROL_RES_IDS= {
            R.id.sample_control,
            /*R.id.gpio_digital_up, R.id.gpio_digital_down, R.id.gpio_digital_none,
            R.id.gpio_output_set, R.id.gpio_output_clear*/
    };

    private byte gpioPin= DEFAULT_GPIO_PIN;
    private int readMode= 0;
    private int chartSet=0;
    private Gpio gpioModule;
    private Timer timerModule;
    private long startTime= -1;


    private final AsyncOperation.CompletionHandler<RouteManager> GpioStreamSetup= new AsyncOperation.CompletionHandler<RouteManager>() {
        @Override
        public void success(RouteManager result) {
            streamRouteManager= result;
            result.subscribe(STREAM_KEY, new RouteManager.MessageHandler() {
                @Override
                public void process(Message message) {
                     final  short gpioValue = message.getData(Short.class);
                    LineData data = chart.getData();
                    if (startTime == -1) {
                        data.addXValue("0");
                        startTime= System.currentTimeMillis();
                    } else {
                        data.addXValue(String.format("%.2f", sampleCount * samplingPeriod));
                    }
                    temp = (float)(gpioValue*0.091+5.539);
                    data.addEntry(new Entry(temp, sampleCount), 0);
                    Log.w("pressure","is"+temp);

                    sampleCount++;
                    /*String params;
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("mobile", "15757115927");
                    dataMap.put("password", "123456");*/

                    /*params = "{\"sensorData1\":\"" + 0 + "\"," +
                            "\"sensorData2\":\"" + 0 + "\"," +
                            "\"sensorData3\":\"" + 0 + "\"," +
                            "\"sensorData4\":\"" + 0 + "\",\"count\":\"" + 0 + "\"," +
                            "\"gait\":\"" + 0 + "\"," +
                            "\"time\":\"" + 0 + "\"," +
                            "\"patientMobile\":\"15733333333\"}";

                    Map<Object, Object> user = PatientService.AddData(HttpUrl.NLF_DATA, params);*/

                    Thread thread = new Thread(new VisitWebRunnable());
                    thread.start();
                }

            });

        }

    };
    class VisitWebRunnable implements Runnable {
        public void run() {
            String params/* = "{\"mobile\":\"15711111111\",\"password\":\"123456\"}"*/;

            // Map<String, String> user =
            // PatientService.Register(HttpUrl.PATIENT_REGISTER,
            // params);

            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("mobile", "15757115927");
            dataMap.put("password", "123456");
            // PatientService.Login(HttpUrl.PATIENT_LOGIN, dataMap);

            // Map<String, String> user
            // =PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO,
            // "15700000000");

            // Map<String, String> user =
            // PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO,
            // "15757115922");// 首先获取patientId
            // System.out.println(user.get("patientId"));
            // params = "{\"patientId\":\"" + user.get("patientId")
            // + "\",\"mobile\":\"15700000000\"}";
            // Map<String, String> data =
            // PatientService.updatePatientInfo(HttpUrl.PATIENT_UPDATE,
            // params);// 然后根据patientId修改数据
            //
            params = "{\"sensorData1\":\"" + temp + "\"," +
                    "\"sensorData2\":\"" + 0 + "\"," +
                    "\"sensorData3\":\"" + 0 + "\"," +
                    "\"sensorData4\":\"" + 0 + "\",\"count\":\"" + 0 + "\"," +
                    "\"gait\":\"" + 0 + "\"," +
                    "\"time\":\"" + System.currentTimeMillis() + "\"," +
                    "\"patientMobile\":\"15733333333\"}";

            Map<String, String> user = PatientService.AddData(HttpUrl.NLF_DATA, params);

        }
    }


    public GpioPressueFragment() {
        super(R.string.navigation_fragment_gpio, "adc", R.layout.fragment_gpio, GPIO_SAMPLE_PERIOD / 1000.f, 0, 1023);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LineChart chart1 = (LineChart) view.findViewById(R.id.data_chart);
        final LineChart chart2 = (LineChart) view.findViewById(R.id.data_chart2);
        final LineChart chart3 = (LineChart) view.findViewById(R.id.data_chart3);
        final LineChart chart4 = (LineChart) view.findViewById(R.id.data_chart4);


        Spinner accRangeSelection= (Spinner) view.findViewById(R.id.gpio_read_mode);
        accRangeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                readMode = position;
                YAxis leftAxis = chart.getAxisLeft();

                switch (readMode) {
                    case READ_ADC:
                        max = 100;
                        leftAxis.setAxisMaxValue(max);
                        csvHeaderDataName = "压力值（mmHg）";
                        break;
                    case READ_ABS_REF:
                        max = 3000;
                        leftAxis.setAxisMaxValue(max);
                        csvHeaderDataName = "abs reference";
                        break;
                    case READ_DIGITAL:
                        max = 1;
                        leftAxis.setAxisMaxValue(max);
                        csvHeaderDataName = "digital";
                        break;
                }

                leftAxis.setAxisMaxValue(max);
                refreshChart(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner chartSelection = (Spinner) view.findViewById(R.id.gpio_select_chart);
        chartSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chartSet = position;

                switch (chartSet){
                    case CHART1:

                        chart1.setVisibility(View.VISIBLE);
                        chart2.setVisibility(View.GONE);
                        chart3.setVisibility(view.GONE);
                        chart4.setVisibility(view.GONE);
                        Log.d("s", "onItemSelected: 1");
                        csvHeaderDataName = "chart1";
                        break;
                    case CHART2:
                        //chart = (LineChart) view.findViewById(R.id.data_chart2);
                        chart1.setVisibility(View.GONE);
                        chart2.setVisibility(View.VISIBLE);
                        chart3.setVisibility(view.GONE);
                        chart4.setVisibility(view.GONE);
                        csvHeaderDataName = "chart2";
                        break;
                    case CHART3:
                        //chart = (LineChart) view.findViewById(R.id.data_chart3);
                        chart1.setVisibility(View.GONE);
                        chart2.setVisibility(View.GONE);
                        chart3.setVisibility(view.VISIBLE);
                        chart4.setVisibility(view.GONE);
                        csvHeaderDataName = "chart3";
                        break;
                    case CHART4:
                        //chart = (LineChart) view.findViewById(R.id.data_chart4);
                        chart1.setVisibility(View.GONE);
                        chart2.setVisibility(View.GONE);
                        chart3.setVisibility(view.GONE);
                        chart4.setVisibility(view.VISIBLE);
                        csvHeaderDataName = "chart4";
                        break;
                }
                //refreshChart(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter= ArrayAdapter.createFromResource(getContext(), R.array.values_gpio_read_mode, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accRangeSelection.setAdapter(spinnerAdapter);
        accRangeSelection.setSelection(readMode);
        ArrayAdapter<CharSequence> spinnerAda = ArrayAdapter.createFromResource(
                getContext(),R.array.values_chart_selection,android.R.layout.simple_spinner_item);
        spinnerAda.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        chartSelection.setAdapter(spinnerAda);
        accRangeSelection.setSelection(chartSet);           


        //EditText gpioPinText= (EditText) view.findViewById(R.id.gpio_pin_value);
      //  gpioPinText.setText(String.format("%d", gpioPin));
        /*gpioPinText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final TextInputLayout gpioTextWrapper = (TextInputLayout) view.findViewById(R.id.gpio_pin_wrapper);

                try {
                    gpioPin = Byte.valueOf(s.toString());
                    gpioTextWrapper.setError(null);
                    for (int id : CONTROL_RES_IDS) {
                        view.findViewById(id).setEnabled(true);
                    }
                } catch (Exception e) {
                    gpioTextWrapper.setError(e.getLocalizedMessage());
                    for (int id : CONTROL_RES_IDS) {
                        view.findViewById(id).setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/

        /*view.findViewById(R.id.gpio_digital_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpioModule.setPinPullMode(gpioPin, Gpio.PullMode.PULL_UP);
            }
        });
        view.findViewById(R.id.gpio_digital_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpioModule.setPinPullMode(gpioPin, Gpio.PullMode.PULL_DOWN);
            }
        });
        view.findViewById(R.id.gpio_digital_none).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpioModule.setPinPullMode(gpioPin, Gpio.PullMode.NO_PULL);
            }
        });

        Button setDoBtn= (Button) view.findViewById(R.id.gpio_output_set);
        setDoBtn.setText(R.string.value_gpio_output_set);
        setDoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpioModule.setDigitalOut(gpioPin);
            }
        });

        Button clearDoBtn= (Button) view.findViewById(R.id.gpio_output_clear);
        clearDoBtn.setText(R.string.value_gpio_output_clear);
        clearDoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpioModule.clearDigitalOut(gpioPin);
            }
        });*/
    }
    private  Accelerometer accelModule = null;
    @Override
    protected void boardReady() throws UnsupportedModuleException {
        gpioModule= mwBoard.getModule(Gpio.class);
        timerModule= mwBoard.getModule(Timer.class);
//        accelModule = mwBoard.getModule(Accelerometer.class);
    }

    @Override
    protected void fillHelpOptionAdapter(HelpOptionAdapter adapter) {
        adapter.add(new HelpOption(R.string.config_name_gpio_pin, R.string.config_desc_gpio_pin));
        adapter.add(new HelpOption(R.string.config_name_gpio_read_mode, R.string.config_desc_gpio_read_mode));
        adapter.add(new HelpOption(R.string.config_name_output_control, R.string.config_desc_output_control));
        adapter.add(new HelpOption(R.string.config_name_pull_mode, R.string.config_desc_pull_mode));
    }

    @Override
    protected void setup() {
        switch(readMode) {
            case READ_ADC:
                gpioModule.routeData().fromAnalogIn(gpioPin, Gpio.AnalogReadMode.ADC).stream(STREAM_KEY).commit()
                        .onComplete(GpioStreamSetup);
                break;
            case READ_ABS_REF:
                gpioModule.routeData().fromAnalogIn(gpioPin, Gpio.AnalogReadMode.ABS_REFERENCE).stream(STREAM_KEY).commit()
                        .onComplete(GpioStreamSetup);
                break;
            case READ_DIGITAL:
                gpioModule.routeData().fromDigitalIn(gpioPin).stream(STREAM_KEY).commit()
                        .onComplete(GpioStreamSetup);
                break;
        }
        filenameExtraString= String.format("%s_pin_%d", csvHeaderDataName, gpioPin);
        timerModule.scheduleTask(new Timer.Task() {
            @Override
            public void commands() {
                switch(readMode) {
                    case READ_ADC:
                        gpioModule.readAnalogIn(gpioPin, Gpio.AnalogReadMode.ADC);
                        break;
                    case READ_ABS_REF:
                        gpioModule.readAnalogIn(gpioPin, Gpio.AnalogReadMode.ABS_REFERENCE);
                        break;
                    case READ_DIGITAL:
                        gpioModule.readDigitalIn(gpioPin);
                        break;
                    default:
                        throw new RuntimeException("Unrecognized read mode: " + readMode);
                }
            }
        }, GPIO_SAMPLE_PERIOD, false).onComplete(new AsyncOperation.CompletionHandler<Timer.Controller>() {
            @Override
            public void success(Timer.Controller result) {
                result.start();
            }
        });
    }

    @Override
    protected void clean() {
        timerModule.removeTimers();
    }

    @Override
    protected void resetData(boolean clearData) {
        super.resetData(clearData);

        if (clearData) {
            startTime= -1;
        }
    }
}
