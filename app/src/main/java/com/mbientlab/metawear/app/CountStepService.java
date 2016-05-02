/*
package com.mbientlab.metawear.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class CountStepService extends Service {


    public CountStepService() {
        super.onCreate();
        Thread workThread = new Thread(null,backgroundWork,"WorkThread");
    }

    private Runnable backgroundWork = new Runnable() {
        @Override
        public void run() {

           CountStepActivity countStepActivity = new CountStepActivity();
            countStepActivity.stepCount();
            // 调用countStep的方法。
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
*/
