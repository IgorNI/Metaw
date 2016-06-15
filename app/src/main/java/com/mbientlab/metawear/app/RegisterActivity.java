package com.mbientlab.metawear.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Map;

import httpclient.http.HttpUrl;
import httpclient.service.PatientService;

/**
 * Created by nilif on 2016/6/14.
 */
public class RegisterActivity extends Activity{
    private Button confirmBtn;
    private EditText patientName;
    private EditText userName;
    private EditText phoneNum;
    private EditText password;
    private DatePicker datePicker;
    private int year;
    private int month;
    private int day;
    private Thread thread;
    private String resultStr = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        thread = new Thread(new VisitWebRunnable());
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        patientName = (EditText) findViewById(R.id.et_patientName);

        phoneNum = (EditText) findViewById(R.id.et_mobile);
        password = (EditText) findViewById(R.id.et_password);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        //获取日期
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                RegisterActivity.this.year = year;
                RegisterActivity.this.month = monthOfYear+1;
                RegisterActivity.this.day = dayOfMonth;
                Toast.makeText(RegisterActivity.this, "时间是"+year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日", Toast.LENGTH_SHORT).show();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/6/14 将所有的字段保存？
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                thread.start();

                /*HttpRequest httpRequest =new HttpRequest();
                JSONArray message = null;
                try {
                    message = PatientService.getAdvice("http://121.42.164.101:8080/member/patient/getAdviceByPatientMobile/", "?mobile=15700000000");
                    if(message != null){
                        Map<Object, Object> data = new HashMap<Object, Object>();
                        for(int i=0;i<message.size();i++){
                            data = JsonUtil.JsontoMap(message.get(i));

                            System.out.println(data.get("adviceId"));
                        }
                    }else{
                        System.out.println("获取失败或者医生未给建议");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        });

    }

    private class VisitWebRunnable implements Runnable {
        @Override
        public void run() {
            String params = "{\"mobile\":\"15733333333\",\"password\":\"123456\"}";




//			PatientService.Login(HttpUrl.PATIENT_LOGIN, dataMap);

//			Map<String, String> user =PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO,
//					"15700000000");

//			Map<String, String> user = PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO, "15757115922");// 首先获取patientId
//			System.out.println(user.get("patientId"));
//			params = "{\"patientId\":\"" + user.get("patientId")
//					+ "\",\"mobile\":\"15700000000\"}";
//			Map<String, String> data = PatientService.updatePatientInfo(HttpUrl.PATIENT_UPDATE, params);// 然后根据patientId修改数据
//
            params = "{\"mobile\":\"" + phoneNum.getText().toString() + "\"," +
                    "\"password\":\"" + password.getText().toString() + "\"," +
                    "\"patientName\":\""+patientName.getText().toString()+"\"," +
                    "\"province\":\"22.0\",\"city\":\"" + 0 + "\"," +
                    "\"county\":\"" +32 +"\"," +
                    "\"details\":\""+ 32+"\","+
                    "\"birthday\":\""+ 0 +"\","+
                    "\"isNLFData\":\""+ true+"\"}";

//            Map<String, String> user = PatientService.AddData(HttpUrl.NLF_DATA, params);
            Map<Object, Object> user = PatientService.Register(HttpUrl.PATIENT_REGISTER, params);
            Intent intent = new Intent(RegisterActivity.this,MyInforActivity.class);
            intent.putExtra("Login_phoneNum",phoneNum.getText().toString());
            startActivity(intent);
        }
    }
}
