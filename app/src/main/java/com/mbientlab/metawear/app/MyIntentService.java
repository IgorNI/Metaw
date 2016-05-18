package com.mbientlab.metawear.app;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import httpclient.http.HttpUrl;
import httpclient.service.PatientService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.mbientlab.metawear.app.action.FOO";
    private static final String ACTION_BAZ = "com.mbientlab.metawear.app.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.mbientlab.metawear.app.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.mbientlab.metawear.app.extra.PARAM2";
    private static int i = 0;
    private static long time = 0;
    Thread thread = new Thread(new VisitWebRunnable());


    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        // 上传

        Timer timer = new Timer();
        time = intent.getLongExtra("receiver",2000l);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                thread.run();
                //Log.w("ss", "run: " );
            }
        },time,time);

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class VisitWebRunnable implements Runnable {
        public void run() {
            Log.w("s", "run: ");;

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
                    "\"sensorData4\":\"22.0\",\"count\":\"" + 0 + "\"," +
                    "\"gait\":\"" +0 +"\"," +
                    "\"patientMobile\":\"15733333333\"}";


            Map<String, String> user = PatientService.AddData(HttpUrl.NLF_DATA, params);
        }
    }

}
