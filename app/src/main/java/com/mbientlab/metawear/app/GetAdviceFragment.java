package com.mbientlab.metawear.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.app.help.HelpOptionAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import httpclient.common.JsonUtil;
import httpclient.service.PatientService;

/**
 * Created by nilif on 2016/6/14.
 */
public class GetAdviceFragment extends ModuleFragmentBase{
    private Handler handler;
    private EditText et_doctorId;
    private EditText et_mobile;
    private EditText et_content;
    private String doctorId;
    private String mobile;
    private String content;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_getadvice, container, false);
        handler = new Handler();
        et_doctorId = (EditText) v.findViewById(R.id.et_doctorName);
        et_mobile = (EditText) v.findViewById(R.id.et_mobile);
        et_content = (EditText) v.findViewById(R.id.et_content);
        Thread thread = new Thread(new ViewWeb());
        thread.start();
        return v;
    }

    @Override
    protected void boardReady() throws UnsupportedModuleException {

    }

    @Override
    protected void fillHelpOptionAdapter(HelpOptionAdapter adapter) {

    }

    private class ViewWeb implements Runnable {
        @Override
        public void run() {
            com.alibaba.fastjson.JSONArray message = null;
            try {
                message = PatientService.getAdvice("http://121.42.164.101:8080/member/patient/getAdviceByPatientMobile/", "?mobile=15700000000");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("sk"+message);
            if(message != null){
                Map<Object, Object> data = new HashMap<Object, Object>();
//                message.da

                data = JsonUtil.JsontoMap(message.get(0));//获取第一条数据
                handler.post(runnableUtil);
                System.out.println(data.get("doctorMobile"));
                doctorId = data.get("adviceId").toString();
                mobile = data.get("patientMobile").toString();
                content = data.get("message").toString();

//                for(int i=0;i<message.size();i++){
//                    try {
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    System.out.println(data.get("adviceId"));
//                }
            }else{
                System.out.println("获取失败或者医生未给建议");
            }
        }
    }
    Runnable runnableUtil = new Runnable() {
        @Override
        public void run() {
            et_doctorId.setText(doctorId);
            et_mobile.setText(mobile);
            et_content.setText(content);
        }
    };
}
