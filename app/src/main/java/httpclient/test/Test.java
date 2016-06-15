package httpclient.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbientlab.metawear.app.R;
import com.mbientlab.metawear.app.SensorFragment;

import java.util.HashMap;
import java.util.Map;

import httpclient.http.HttpUrl;
import httpclient.service.PatientService;

/**
 * Created by nilif on 2016/5/3.
 */public class Test extends Activity {
    Button visitWebBtn = null;
    TextView showTextView = null;
    String resultStr =null;
    ProgressBar progressBar = null;
    ViewGroup viewGroup = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initUI();
        visitWebBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextView.setVisibility(View.VISIBLE);
                Thread visitBaiduThread = new Thread(new VisitWebRunnable());
                visitBaiduThread.start();
                try {
                    visitBaiduThread.join();
                    if(resultStr!=null){
                        showTextView.setText(resultStr);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }
    public void initUI(){
//        showTextView = (TextView)findViewById(R.id.textview_show);
//        visitWebBtn = (Button)findViewById(R.id.btn_visit_web);
    }
    class VisitWebRunnable implements Runnable{

        @Override
        public void run() {
            String params = "{\"mobile\":\"15733333333\",\"password\":\"123456\"}";

//			Map<String, String> user = PatientService.Register(HttpUrl.PATIENT_REGISTER, params);

            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("mobile", "15757115927");
            dataMap.put("password", "123456");
//			PatientService.Login(HttpUrl.PATIENT_LOGIN, dataMap);

//			Map<String, String> user =PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO,
//					"15700000000");

//			Map<String, String> user = PatientService.getPatientInfoByMobile(HttpUrl.PATIENT_GETINFO, "15757115922");// 首先获取patientId
//			System.out.println(user.get("patientId"));
//			params = "{\"patientId\":\"" + user.get("patientId")
//					+ "\",\"mobile\":\"15700000000\"}";
//			Map<String, String> data = PatientService.updatePatientInfo(HttpUrl.PATIENT_UPDATE, params);// 然后根据patientId修改数据
//
             params = "{\"sensorData1\":\"" + 32.0 + "\"," +
                     "\"sensorData2\":\"10\"," +
                     "\"sensorData3\":\"22.0\"," +
                     "\"sensorData4\":\"22.0\",\"count\":\"" + /*SensorFragment.stepNum*/0 + "\"," +
                     "\"gait\":\"" +32 +"\"," +
                     "\"patientMobile\":\"15733333333\"}";



            Map<Object, Object> user = PatientService.AddData(HttpUrl.NLF_DATA, params);
        }

    }
}
