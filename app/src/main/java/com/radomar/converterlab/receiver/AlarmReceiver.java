package com.radomar.converterlab.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.radomar.converterlab.service.DataBaseUpdaterService;

/**
 * Created by Radomar on 16.09.2015
 * This receiver start service
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context _context, Intent _intent) {
        Intent update = new Intent(_context, DataBaseUpdaterService.class);
        _context.startService(update);
    }
}
