package com.mbientlab.metawear.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.app.help.HelpOptionAdapter;
import com.mbientlab.metawear.module.Timer;

/**
 * Created by nilif on 2016/5/18.
 */
public class SettingFragment extends ModuleFragmentBase implements View.OnClickListener {

    private static final String TAG = "Button";
    private Timer timer;
    public static long time = 0l;
    private static long time1 = 0l;
    private Handler handler;
    private static Intent httpPsotIntent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settting, container, false);
        Button postTime1 = (Button) v.findViewById(R.id.post_time_1s);
        Button postTime2 = (Button) v.findViewById(R.id.post_time_10s);
        Button postTime3 = (Button) v.findViewById(R.id.post_time_60s);
        Button postData = (Button) v.findViewById(R.id.post_data_btn);
        httpPsotIntent = new Intent(getActivity(),MyIntentService.class);
        postData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ResultReceiver rr = new InnerResultReceiver(handler);
                Log.d(TAG, "onClick: ");
                //getActivity().startService(httpPsotIntent);
            }
        });
        postTime1.setOnClickListener(this);
        postTime2.setOnClickListener(this);
        postTime3.setOnClickListener(this);
        handler = new Handler();
        return v;

    }

        public void onClick(View v){
        switch (v.getId()){
            case R.id.post_time_1s:
                Log.w(TAG, "onClick: 1");
                getActivity().stopService(httpPsotIntent);
                time1 = 1000l;
                Toast.makeText(getActivity().getApplicationContext(),"上传间隔已设置为1s",Toast.LENGTH_SHORT).show();
                break;
            case R.id.post_time_10s:
                Log.w(TAG, "onClick: 2");
                getActivity().stopService(httpPsotIntent);
                time1 = 10000l;
                Toast.makeText(getActivity().getApplicationContext(),"上传间隔已设置为10s",Toast.LENGTH_SHORT).show();
                break;
            case R.id.post_time_60s:
                time1 = 60000l;
                Log.w(TAG, "onClick: 3");
                getActivity().stopService(httpPsotIntent);
                Toast.makeText(getActivity().getApplicationContext(),"上传间隔已设置为60s",Toast.LENGTH_SHORT).show();
                break;
        }
            time = time1;
            httpPsotIntent.putExtra("receiver", time);
            getActivity().startService(httpPsotIntent);

    }


    @Override
    protected void boardReady() throws UnsupportedModuleException {

        timer = mwBoard.getModule(Timer.class);
    }

    @Override
    protected void fillHelpOptionAdapter(HelpOptionAdapter adapter) {

    }
}
