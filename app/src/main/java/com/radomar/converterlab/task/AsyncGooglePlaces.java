package com.radomar.converterlab.task;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Radomar on 18.09.2015
 */

public class AsyncGooglePlaces extends AsyncTask<String, Void, LatLng> {

    private static final String URL = "http://maps.google.com/maps/api/geocode/json";


    /**
     * 1) make request by keywords
     * 2) parse json string
     */
    @Override
    protected LatLng doInBackground(String... params) {

        String jsonString = null;
        try {
            StringBuilder http_request = new StringBuilder(URL);
            http_request.append("?address=Ukraine+");
            http_request.append(URLEncoder.encode(params[0], "utf8"));
            http_request.append("+");
            http_request.append(URLEncoder.encode(params[1], "utf8"));
            http_request.append("&sensor=false");

            Log.d("sometag", http_request.toString());
            jsonString = getJsonString(http_request.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("sometag", jsonString);

        return parseJson(jsonString);
    }
    /**
     * Get json string by url
     * @return json string
     */
    private String getJsonString(String _url) throws IOException {
        URL url = new URL(_url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("GET");

        int respCode = conn.getResponseCode();
        if (respCode == 200) {
            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            inputStream.close();
            conn.disconnect();
            return new String(bos.toByteArray());
        }
        return null;
    }

    /**
     * Parse json string
     *
     * @return LatLng object (position). Return default value if happened exception
     */
    private LatLng parseJson(String _jsonString) {

        JSONObject rootObject = null;
        try {
            rootObject = new JSONObject(_jsonString);
            JSONArray results = rootObject.getJSONArray("results");

            if (results.length() > 0) {
                JSONObject o2 = results.getJSONObject(0);
                JSONObject location = o2.getJSONObject("geometry").getJSONObject("location");

                return new LatLng(location.getDouble("lat"), location.getDouble("lng"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new LatLng(50.438901, 30.499082);
    }

}
