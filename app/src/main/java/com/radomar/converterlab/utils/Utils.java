package com.radomar.converterlab.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.radomar.converterlab.GoogleMapActivity;

/**
 * Created by Radomar on 16.09.2015
 * Something like singleton class. I hope it's a good idea =)
 */
public class Utils {

    public static final String KEY_CITY = "key city";
    public static final String KEY_ADDRESS = "key address";

    private static Utils instance;
    private static Context sContext;

    private Utils(Context _context) {
        sContext = _context;
    }

    public static Utils getInstance(Context _context) {
        if (instance == null) {
            instance = new Utils(_context);
        }
        return instance;
    }

    public void makeCall(String _phoneNumber) {
        Log.d("sometag", "trying to call " + _phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sContext.startActivity(intent);
    }

    public void openBrowser(String _link) {
        Log.d("sometag", "trying to open link " + _link);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_link));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sContext.startActivity(intent);
    }

    public void openMap(String _cityName, String _address) {
        Intent intent = new Intent(sContext, GoogleMapActivity.class);
        intent.putExtra(KEY_CITY, _cityName);
        intent.putExtra(KEY_ADDRESS, _address);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        sContext.startActivity(intent);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
