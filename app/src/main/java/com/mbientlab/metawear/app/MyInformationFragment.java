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

import java.util.HashMap;
import java.util.Map;

import httpclient.http.HttpUrl;
import httpclient.service.PatientService;

/**
 * Created by nilif on 2016/5/18.
 */
public class MyInformationFragment extends ModuleFragmentBase implements View.OnClickListener {
    private static final String TAG = "click";
    private Button LoginBtn;
    private Button RegisterBtn;
    private EditText et_phoneNum;
    private EditText et_password;

    private EditText show_Name;
    private EditText show_phone;
    private EditText show_Birthday;
    private Thread Loginthread;
    private Thread getInforThread;
    private Thread registerThread;
    private String phone;
    private View loginView;
    private View myInforView;
    private View register;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    NavigationActivity activity = (NavigationActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myinfomation, container, false);
        Loginthread = new Thread(new LoginRunnable()); // 初始化登陆的线程
       // registerThread = new Thread(new registerRunnable()); // 初始化注册的线程
        LoginBtn = (Button) v.findViewById(R.id.login_btn);
        RegisterBtn = (Button) v.findViewById(R.id.register_btn);
        et_phoneNum = (EditText) v.findViewById(R.id.input_phoneNum);
        et_password = (EditText) v.findViewById(R.id.input_password);
        show_Name = (EditText) v.findViewById(R.id.et_patientName);
        show_phone = (EditText) v.findViewById(R.id.et_mobile);
        show_Birthday = (EditText) v.findViewById(R.id.et_birthday);
        loginView = v.findViewById(R.id.login);
        myInforView = v.findViewById(R.id.me);
        register = v.findViewById(R.id.regist);

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
                    Loginthread.start();
                    /*loginView.setVisibility(View.GONE);
                    myInforView.setVisibility(View.VISIBLE);*/

                        //Loginthread.interrupt();

                    /*getInforThread.start();*/

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

    private class LoginRunnable implements Runnable {
        @Override
        public void run() {
            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("mobile", et_phoneNum.getText().toString().trim());
            dataMap.put("password", et_password.getText().toString().trim());

            Map<Object, Object> user = PatientService.Login(HttpUrl.PATIENT_LOGIN, dataMap);
            phone = et_phoneNum.getText().toString();
//            ArrayList<String> arrayList = new ArrayList<String>();
            Intent intent = new Intent();
            intent.setClass(getActivity(),MyInforActivity.class);
            intent.putExtra("Login_phoneNum",phone);
            getActivity().startActivity(intent);
        }
    }



}
