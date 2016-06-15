package com.mbientlab.metawear.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.app.help.HelpOptionAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import httpclient.http.HttpUrl;
import httpclient.service.PatientService;

/**
 * Created by nilif on 2016/5/18.
 */
public class LoginFragment extends ModuleFragmentBase implements View.OnClickListener {
    private static final String TAG = "click";
    private Button LoginBtn;
    private Button RegisterBtn;
    private EditText et_phoneNum;
    private EditText et_password;
    private Thread thread;
    private String phone;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    NavigationActivity activity = (NavigationActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        thread = new Thread(new VisitWebRunnable());
        LoginBtn = (Button) v.findViewById(R.id.login_btn);
        RegisterBtn = (Button) v.findViewById(R.id.register_btn);
        et_phoneNum = (EditText) v.findViewById(R.id.input_phoneNum);
        et_password = (EditText) v.findViewById(R.id.input_password);
        phone = et_phoneNum.getText().toString();
        LoginBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        return v;
    }

    @Override
    protected void boardReady() throws UnsupportedModuleException {
    }

    @Override
    protected void fillHelpOptionAdapter(HelpOptionAdapter adapter) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                if (et_phoneNum.length() == 0 || et_password.length() == 0){
                    Toast.makeText(getActivity().getApplicationContext(), "请输入电话或者密码", Toast.LENGTH_SHORT).show();
                }else {
                    // 与服务器端进行匹配，如果相同，则ok，
                        thread.start();


                    /*FragmentManager manager = getFragmentManager();
                    FragmentTransaction ft =manager.beginTransaction();
                    ft.replace(R.id)*/
                }
                break;
            case R.id.register_btn:
                Intent intent = new Intent(getActivity(),RegisterActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    private class VisitWebRunnable implements Runnable {
        @Override
        public void run() {
            String params = "{\"mobile\":\"15733333333\",\"password\":\"123456\"}";

//			Map<String, String> user = PatientService.Register(HttpUrl.PATIENT_REGISTER, params);

            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("mobile", et_phoneNum.getText().toString().trim());
            dataMap.put("password", et_password.getText().toString().trim());
//			PatientService.Login(HttpUrl.PATIENT_LOGIN, dataMap);

//			Map<String, String> user =PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO,
//					"15700000000");

//			Map<String, String> user = PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO, "15757115922");// 首先获取patientId
//			System.out.println(user.get("patientId"));
//			params = "{\"patientId\":\"" + user.get("patientId")
//					+ "\",\"mobile\":\"15700000000\"}";
//			Map<String, String> data = PatientService.updatePatientInfo(HttpUrl.PATIENT_UPDATE, params);// 然后根据patientId修改数据
//
            //不清楚
            /*params = "{\"mobile\":\"" + et_phoneNum.getText().toString() + "\"," +
                    "\"password\":\"" + et_password.getText().toString() + "\"," +
                    "\"time\":\""+ 0 +"\"}";*/
            Map<Object, Object> user = PatientService.Login(HttpUrl.PATIENT_LOGIN, dataMap);
            ArrayList<String> arrayList = new ArrayList<String>();
            Intent intent = new Intent();
            intent.setClass(getActivity(),MyInforActivity.class);
            intent.putExtra("Login_phoneNum",phone);
            getActivity().startActivity(intent);
        }
    }
}
