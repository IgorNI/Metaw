package com.mbientlab.metawear.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    private EditText et_name;
    private EditText et_mobile;
    private EditText et_birthday;
    private String phone;
    private Handler handler=null;
    private Map<Object, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me);
        handler = new Handler();
        et_name= (EditText) findViewById(R.id.et_patientName);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_birthday = (EditText) findViewById(R.id.et_birthday);
        Button backBtn = (Button) findViewById(R.id.back_btn);
        /*backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyInforActivity.this,NavigationActivity.class);
                startActivity(intent);
            }
        });*/
        Intent intent = getIntent();
        phone =  intent.getStringExtra("Login_phoneNum");
        Thread thread = new Thread(new GetInforRunnable());
        thread.start();
    }

    class GetInforRunnable implements Runnable {
        @Override
        public void run() {
             user = PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO, phone);
           // System.out.println(user.get("patientName").toString());
            handler.post(runnableUi);
            //et_name.setText(user.get("patientName").toString());
            //et_mobile.setText(user.get("mobile").toString());
        }
    }
    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            et_name.setText(user.get("patientName").toString());
            et_mobile.setText(user.get("mobile").toString());
            et_birthday.setText(user.get("createTime2").toString());
        }
    };

}
