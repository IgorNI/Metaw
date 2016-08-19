package com.mbientlab.metawear.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.components.YAxis;
import com.mbientlab.metawear.AsyncOperation;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.app.help.HelpOption;
import com.mbientlab.metawear.app.help.HelpOptionAdapter;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Bma255Accelerometer;
import com.mbientlab.metawear.module.Bmi160Accelerometer;
import com.mbientlab.metawear.module.Mma8452qAccelerometer;

/**
 * Created by nilif on 2016/5/3.
 */
public class AccelerometerFragment extends ThreeAxisChartFragment {
    private static final float[] MMA845Q_RANGES= {2.f, 4.f, 8.f}, BMI160_RANGES= {2.f, 4.f, 8.f, 16.f};
    private static final float INITIAL_RANGE= 2.f, ACC_FREQ= 50.f;
    private static final String STREAM_KEY= "accel_stream";

    private Spinner accRangeSelection;
    private Accelerometer accelModule= null;
    private int rangeIndex= 0;


    public AccelerometerFragment() {
        super("acceleration", R.layout.fragment_sensor_config_spinner,
                R.string.navigation_fragment_accelerometer, STREAM_KEY, -INITIAL_RANGE, INITIAL_RANGE);
    }



    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) view.findViewById(R.id.config_option_title)).setText(R.string.config_name_acc_range);

        accRangeSelection= (Spinner) view.findViewById(R.id.config_option_spinner);
        accRangeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rangeIndex = position;

                final YAxis leftAxis = chart.getAxisLeft();
                if (accelModule instanceof Bmi160Accelerometer || accelModule instanceof Bma255Accelerometer) {
                    leftAxis.setAxisMaxValue(BMI160_RANGES[rangeIndex]);
                    leftAxis.setAxisMinValue(-BMI160_RANGES[rangeIndex]);
                } else if (accelModule instanceof Mma8452qAccelerometer) {
                    leftAxis.setAxisMaxValue(MMA845Q_RANGES[rangeIndex]);
                    leftAxis.setAxisMinValue(-MMA845Q_RANGES[rangeIndex]);
                }
                refreshChart(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        fillRangeAdapter();
    }

    @Override
    protected void boardReady() throws UnsupportedModuleException{
        accelModule= mwBoard.getModule(Accelerometer.class);

        //fillRangeAdapter();
    }

    @Override
    protected void fillHelpOptionAdapter(HelpOptionAdapter adapter) {
        adapter.add(new HelpOption(R.string.config_name_acc_range, R.string.config_desc_acc_range));
    }

    //@Override
    public void setup() {
        samplePeriod= 1 / accelModule.setOutputDataRate(ACC_FREQ);

        if (accelModule instanceof Bmi160Accelerometer || accelModule instanceof Bma255Accelerometer) {
            accelModule.setAxisSamplingRange(BMI160_RANGES[rangeIndex]);
        } else if (accelModule instanceof Mma8452qAccelerometer) {
            accelModule.setAxisSamplingRange(MMA845Q_RANGES[rangeIndex]);
        }

        AsyncOperation<RouteManager> routeManagerResult= accelModule.routeData().fromAxes().stream(STREAM_KEY).commit();
        routeManagerResult.onComplete(dataStreamManager);
        routeManagerResult.onComplete(new AsyncOperation.CompletionHandler<RouteManager>() {
            @Override
            public void success(RouteManager result) {
                accelModule.enableAxisSampling();
                accelModule.start();
            }
        });
    }

    //@Override
    protected void clean() {
        accelModule.stop();
        accelModule.disableAxisSampling();
    }

    /*private void fillRangeAdapter() {
        ArrayAdapter<CharSequence> spinnerAdapter= null;
        if (accelModule instanceof Bmi160Accelerometer || accelModule instanceof Bma255Accelerometer) {
            spinnerAdapter= ArrayAdapter.createFromResource(getContext(), R.array.values_bmi160_acc_range, android.R.layout.simple_spinner_item);
        } else if (accelModule instanceof Mma8452qAccelerometer) {
            spinnerAdapter= ArrayAdapter.createFromResource(getContext(), R.array.values_mma8452q_acc_range, android.R.layout.simple_spinner_item);
        }

        if (spinnerAdapter != null) {
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            accRangeSelection.setAdapter(spinnerAdapter);
        }
    }*/
}