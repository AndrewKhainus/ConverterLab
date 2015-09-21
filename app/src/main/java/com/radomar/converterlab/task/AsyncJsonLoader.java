package com.radomar.converterlab.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.radomar.converterlab.db.DatabaseHelper;
import com.radomar.converterlab.model.CurrencyRecords;
import com.radomar.converterlab.model.JsonDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Radomar on 13.09.2015
 */
public class AsyncJsonLoader extends AsyncTask<Void, Void, Void> {

    private static final String URL = "http://resources.finance.ua/ru/public/currency-cash.json";
    private Context mContext;
    private DatabaseHelper mDbHelper;
    private JsonDataModel mJsonDataModel;

    public AsyncJsonLoader(Context _context) {
        this.mContext = _context;
        mDbHelper = new DatabaseHelper(_context);
    }

    /**
     *  1) download json string
     *  2) parse date from json
     *  3) compare date with date from database
     *  4) parse all data from json and save to database if required
     */
    @Override
    protected Void doInBackground(Void... params) {

        String jsonString = null;
        try {
            jsonString = getJsonString(URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonString == null) {
            return null;
        }
        
        String timeFromDb = getTimeFromDb();
        String timeFromJson = getTimeFromJson(jsonString);

        if (!timeFromJson.equals(timeFromDb)) {
            try {
                parseDataModel(jsonString);
                writeToDatabase(mJsonDataModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        return null;
    }

    /**
     *  read json from internet by URL
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
            byte[] buffer = new byte[1000];
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
     *  return time from jsonString
     */
    private String getTimeFromJson(String _jsonString) {

        String time = null;
        try {
            time = new JSONObject(_jsonString).getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return time;
    }

    /**
     *  return time from Db
     */
    private String getTimeFromDb() {
        return mDbHelper.getLastUpdateTime();
    }


    private void parseDataModel(String _jsonString) throws JSONException {

        JSONObject regionsJsonObject = new JSONObject(_jsonString)
                .getJSONObject("regions");
        JSONObject currenciesJsonObject = new JSONObject(_jsonString)
                .getJSONObject("currencies");
        JSONObject citiesJsonObject = new JSONObject(_jsonString)
                .getJSONObject("cities");

        Type type = new TypeToken<HashMap<String, String>>(){}.getType();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        mJsonDataModel = gson.fromJson(_jsonString, JsonDataModel.class);

        mJsonDataModel.currencies = new Gson().fromJson(currenciesJsonObject.toString(), type);
        mJsonDataModel.cities = new Gson().fromJson(citiesJsonObject.toString(), type);
        mJsonDataModel.regions = new Gson().fromJson(regionsJsonObject.toString(), type);

        parseOrganizations(_jsonString);
    }

    private void parseOrganizations(String _jsonString) throws JSONException {

        JSONArray jsonArray = new JSONObject(_jsonString).getJSONArray("organizations");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i).getJSONObject("currencies");
            for (String key: mJsonDataModel.currencies.keySet()) {
                if (object.has(key)) {

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    CurrencyRecords currencyRecords = gson.fromJson(object.getJSONObject(key).toString(), CurrencyRecords.class);
                    currencyRecords.currencyId = key;
                    currencyRecords.organizationId = mJsonDataModel.organizations.get(i).id;

                    mJsonDataModel.organizations.get(i).recordsList.add(currencyRecords);

                    String prevAskPrice = mDbHelper.getPrevAskPrice(currencyRecords.organizationId, currencyRecords.currencyId);

                    if (!TextUtils.isEmpty(prevAskPrice)) {
                        Double currentAsk = Double.parseDouble(currencyRecords.ask);
                        Double prevAsk = Double.parseDouble(prevAskPrice);
                        if (prevAsk > currentAsk) {
                            currencyRecords.askFlag = 0;
                        }
                        if (prevAsk < currentAsk) {
                            currencyRecords.askFlag = 1;
                        }
                    }

                    String prevBidPrice = mDbHelper.getPrevBidPrice(currencyRecords.organizationId, currencyRecords.currencyId);

                    if (!TextUtils.isEmpty(prevBidPrice)) {
                        Double currentBid = Double.parseDouble(currencyRecords.bid);
                        Double prevBid = Double.parseDouble(prevBidPrice);
                        if (prevBid > currentBid) {
                            currencyRecords.bidFlag = 0;
                        }
                        if (prevBid < currentBid) {
                            currencyRecords.bidFlag = 1;
                        }
                    }
                }
            }
        }
    }

    private void writeToDatabase(JsonDataModel _jsonDataModel) {
        mDbHelper.updateCitiesTable(_jsonDataModel.cities);
        mDbHelper.updateRegionsTable(_jsonDataModel.regions);
        mDbHelper.updateCurrenciesTable(_jsonDataModel.currencies);
        mDbHelper.updateOrganizationsTable(_jsonDataModel.organizations);
        mDbHelper.updateCurrencyRecordsTable(_jsonDataModel.organizations);
        mDbHelper.updateTableDate(_jsonDataModel.date);
    }

//    private void createNotification() {
//        mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        mBuilder = new NotificationCompat.Builder(mContext);
//        mBuilder.setContentTitle("Picture Download")
//                .setContentText("Download in progress")
//                .setSmallIcon(android.R.drawable.ic_dialog_alert);
//        mBuilder.setOngoing(true);
//        mBuilder.setProgress(100, 0, false);
//    }

}
