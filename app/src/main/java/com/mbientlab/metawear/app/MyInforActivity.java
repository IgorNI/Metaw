package com.mbientlab.metawear.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.app.help.HelpOptionAdapter;

import java.util.Map;

import httpclient.http.HttpUrl;
import httpclient.service.PatientService;

/**
 * Created by nilif on 2016/6/14.
 */
public class MyInforActivity extends Activity{

    private static final String TAG = "MyInfor";

    private String ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me);
        Intent intent = getIntent();
              ss =  intent.getStringExtra("Login_phoneNum");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<Object, Object> user = PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO, ss);
                System.out.println(user.get("patientName"));

            }
        });
        thread.start();
    }
}
