package com.radomar.converterlab.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.radomar.converterlab.MainActivity;
import com.radomar.converterlab.task.AsyncJsonLoader;
import com.radomar.converterlab.utils.Utils;

/**
 * Created by Radomar on 13.09.2015
 */
public class DataBaseUpdaterService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        if (Utils.getInstance(getApplicationContext()).isOnline()) {
            new AsyncJsonLoader(getApplicationContext()) {
                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    sendBroadcast();
                    stopSelf();
                }
            }.execute();
        } else {
            sendBroadcast();
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("sometag", "onDestroy service");
        super.onDestroy();
    }

    private void sendBroadcast() {
        Intent intent = new Intent(MainActivity.START_RECYCLER_ADAPTER);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
