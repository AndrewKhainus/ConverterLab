package com.radomar.converterlab.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.radomar.converterlab.model.BankInfoItemModel;
import com.radomar.converterlab.model.CurrencyInfoItemModel;
import com.radomar.converterlab.model.CurrencyRecords;
import com.radomar.converterlab.model.OrganizationModel;

import java.util.ArrayList;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "db_name4.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CITIES = "cities";
    private static final String TABLE_REGIONS = "regions";
    private static final String TABLE_CURRENCIES = "currencies";
    private static final String TABLE_ORGANIZATIONS = "organizations";
    private static final String TABLE_CURRENCY_RECORDS = "currencyRecords";
    private static final String TABLE_LAST_UPDATE = "lastUpdate";

    private static final String COL_CITY_ID = "cityId";
    private static final String COL_CITY_NAME = "name";
    private static final String COL_REGION_ID = "regionId";
    private static final String COL_REGION_NAME = "name";
    private static final String COL_CURRENCY_ID = "currencyId";
    private static final String COL_CURRENCY_NAME = "name";
    private static final String COL_ORGANIZATION_ID = "organizationId";
    private static final String COL_TITLE = "title";
    private static final String COL_PHONE = "phone";
    private static final String COL_ADDRESS = "address";
    private static final String COL_LINK = "link";
    private static final String COL_ASK = "ask";
    private static final String COL_BID = "bid";
    private static final String COL_ASK_FLAG = "askFlag";
    private static final String COL_BID_FLAG = "bidFlag";
    private static final String COL_DATE = "date";


    /**
     *  ====================================================================================
     *    |___cityId___|_____name_____|
     *    |            |              |
     *  ====================================================================================
     */

    private static final String CITIES_TABLE_CREATE =
            "CREATE TABLE " +  TABLE_CITIES + " (" +
                    COL_CITY_ID + " TEXT PRIMARY KEY NOT NULL, " +
                    COL_CITY_NAME + " TEXT);";

    /**
     *  ====================================================================================
     *    |___regionId___|_____name_____|
     *    |              |              |
     *  ====================================================================================
     */

    private static final String REGIONS_TABLE_CREATE =
            "CREATE TABLE " +  TABLE_REGIONS + " (" +
                    COL_REGION_ID + " TEXT PRIMARY KEY NOT NULL, " +
                    COL_REGION_NAME + " TEXT);";

    /**
     *  ====================================================================================
     *    |___currencyId___|_____name_____|
     *    |                |              |
     *  ====================================================================================
     */

    private static final String CURRENCIES_TABLE_CREATE =
            "CREATE TABLE " +  TABLE_CURRENCIES + " (" +
                    COL_CURRENCY_ID + " TEXT PRIMARY KEY NOT NULL, " +
                    COL_CURRENCY_NAME + " TEXT);";

    /**
     *  ====================================================================================
     *    |_organizationId_|_title_|__regionId__|__cityId__|__phone__|__address__|__link__|
     *    |                |       |            |          |         |           |        |
     *  ====================================================================================
     */
    private static final String ORGANIZATIONS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ORGANIZATIONS + " (" +
                    COL_ORGANIZATION_ID + " TEXT PRIMARY KEY NOT NULL, " +
                    COL_TITLE + " TEXT, " +
                    COL_REGION_ID + " TEXT, " +
                    COL_CITY_ID + " TEXT, " +
                    COL_PHONE + " TEXT, " +
                    COL_ADDRESS + " TEXT, " +
                    COL_LINK + " TEXT, " +
                    "FOREIGN KEY (" + COL_REGION_ID  +") REFERENCES " + TABLE_REGIONS + "(" + COL_REGION_ID + "), " +
                    "FOREIGN KEY (" + COL_CITY_ID + ") REFERENCES " + TABLE_CITIES + "(" + COL_CITY_ID + "));";

    /**
     *  ====================================================================================
     *    |_organizationId_|_currencyId_|__ask__|__bid__|__askFlag__|__bidFlag__|
     *    |                |            |       |       |           |           |
     *  ====================================================================================
     */
    private static final String CURRENCY_RECORDS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_CURRENCY_RECORDS + " (" +
                    COL_ORGANIZATION_ID +  " TEXT, " +
                    COL_CURRENCY_ID +  " TEXT, " +
                    COL_ASK + " TEXT, " +
                    COL_BID + " TEXT, " +
                    COL_ASK_FLAG + " INTEGER, " +
                    COL_BID_FLAG + " INTEGER);";

    /**
     *  ====================================================================================
     *    |_______date________|
     *    |                   |
     *  ====================================================================================
     */


    private static final String LAST_UPDATE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_LAST_UPDATE + " (" +
                    COL_DATE + " TEXT);";

    private SQLiteDatabase mDataBase;

    public DatabaseHelper(Context _context) {
        super(_context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase _sqLiteDatabase) {

        _sqLiteDatabase.execSQL(ORGANIZATIONS_TABLE_CREATE);

        _sqLiteDatabase.execSQL(CITIES_TABLE_CREATE);

        _sqLiteDatabase.execSQL(REGIONS_TABLE_CREATE);

        _sqLiteDatabase.execSQL(CURRENCIES_TABLE_CREATE);

        _sqLiteDatabase.execSQL(CURRENCY_RECORDS_TABLE_CREATE);

        _sqLiteDatabase.execSQL(LAST_UPDATE_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int _i, int _i1) {

    }

    public void updateCitiesTable(Map<String,String> _map) {
        mDataBase = getWritableDatabase();

        mDataBase.execSQL("DELETE FROM '" + TABLE_CITIES + "'");

        for(Map.Entry<String, String> pair: _map.entrySet()) {

            String insert = "INSERT INTO " + TABLE_CITIES + " (" +
                    COL_CITY_ID + ", " +
                    COL_CITY_NAME + ") VALUES (" +
                    "'" + pair.getKey() + "', '" + pair.getValue() + "')";

            mDataBase.execSQL(insert);
        }
        mDataBase.close();
    }

    public void updateRegionsTable(Map<String,String> _map) {
        mDataBase = getWritableDatabase();

        mDataBase.execSQL("DELETE FROM '" + TABLE_REGIONS + "'");

        for(Map.Entry<String, String> pair: _map.entrySet()) {

            String insert = "INSERT INTO " + TABLE_REGIONS + " (" +
                    COL_REGION_ID + ", " +
                    COL_REGION_NAME + ") VALUES (" +
                    "'" + pair.getKey() + "', '" + pair.getValue() + "')";

            mDataBase.execSQL(insert);
        }
        mDataBase.close();
    }

    public void updateCurrenciesTable(Map<String,String> _map) {
        mDataBase = getWritableDatabase();

        mDataBase.execSQL("DELETE FROM '" + TABLE_CURRENCIES + "'");

        for(Map.Entry<String, String> pair: _map.entrySet()) {

            String insert = "INSERT INTO " + TABLE_CURRENCIES + " (" +
                    COL_CURRENCY_ID + ", " +
                    COL_CURRENCY_NAME + ") VALUES (" +
                    "'" + pair.getKey() + "', '" + pair.getValue() + "')";

            mDataBase.execSQL(insert);
        }
        mDataBase.close();
    }

    public void updateOrganizationsTable(ArrayList<OrganizationModel> _list) {
        mDataBase = getWritableDatabase();

        mDataBase.execSQL("DELETE FROM '" + TABLE_ORGANIZATIONS + "'");

        for (OrganizationModel organizationModel: _list) {

            String insert = "INSERT INTO " + TABLE_ORGANIZATIONS + " (" +
                    COL_ORGANIZATION_ID + ", " +
                    COL_TITLE + ", " +
                    COL_REGION_ID + ", " +
                    COL_CITY_ID + ", " +
                    COL_PHONE +  ", " +
                    COL_ADDRESS +  ", " +
                    COL_LINK + ") VALUES (" +
                    "'" + organizationModel.id + "', '" + organizationModel.title + "', '" +
                    organizationModel.regionId + "', '" + organizationModel.cityId + "', '" +
                    organizationModel.phone + "', '" + organizationModel.address + "', '" +
                    organizationModel.link + "')";

            mDataBase.execSQL(insert);
        }
        mDataBase.close();
    }

    public void updateCurrencyRecordsTable(ArrayList<OrganizationModel> _list) {
        mDataBase = getWritableDatabase();

        mDataBase.execSQL("DELETE FROM '" + TABLE_CURRENCY_RECORDS + "'");

        for (OrganizationModel organizationModel: _list) {
            for (CurrencyRecords currencyRecords: organizationModel.recordsList) {
                String insert = "INSERT INTO " + TABLE_CURRENCY_RECORDS + " (" +
                        COL_ORGANIZATION_ID + ", " +
                        COL_CURRENCY_ID + ", " +
                        COL_ASK + ", " +
                        COL_BID + ", " +
                        COL_ASK_FLAG + ", " +
                        COL_BID_FLAG + ") VALUES (" +
                        "'" + currencyRecords.organizationId + "', '" + currencyRecords.currencyId +
                        "', '" + currencyRecords.ask + "', '" + currencyRecords.bid +
                        "', '" + currencyRecords.askFlag + "', '" + currencyRecords.bidFlag + "')";

                mDataBase.execSQL(insert);
            }
        }
        mDataBase.close();
    }

    public void updateTableDate(String _newDate) {
        mDataBase = getWritableDatabase();

        mDataBase.execSQL("DELETE FROM '" + TABLE_LAST_UPDATE + "'");

        String insert = "INSERT INTO " + TABLE_LAST_UPDATE + " (" +
                COL_DATE + ") VALUES (" +
                "'" +  _newDate + "')";

        mDataBase.execSQL(insert);

        mDataBase.close();
    }

    public String getLastUpdateTime() {
        mDataBase = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_LAST_UPDATE;
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        String time = "nothing";
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                time = cursor.getString(0);
            }
            cursor.close();
        }
        mDataBase.close();

        return time;
    }

    public ArrayList<BankInfoItemModel> getData() {
        ArrayList<BankInfoItemModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT table1.organizationId," +
                " table1.title," +
                " table2.name," +
                " table3.name," +
                " table1.phone," +
                " table1.address," +
                " table1.link" +
                " FROM organizations table1" +
                " INNER JOIN" +
                " regions table2 ON table1.regionId = table2.regionId" +
                " INNER JOIN" +
                " cities table3 ON table1.cityId = table3.cityId";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new BankInfoItemModel(cursor.getString(0),
                                               cursor.getString(1),
                                               cursor.getString(2),
                                               cursor.getString(3),
                                               cursor.getString(4),
                                               cursor.getString(5),
                                               cursor.getString(6)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<CurrencyInfoItemModel> getCurrenciesList(String _organizationId) {
        ArrayList<CurrencyInfoItemModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT table1.currencyId, " +
                "table2.name," +
                "table1.ask," +
                "table1.bid," +
                "table1.askFlag, " +
                "table1.bidFlag" +
                " FROM currencyRecords table1" +
                " INNER JOIN" +
                " currencies table2 ON table1.currencyId = table2.currencyId" +
                " WHERE organizationId = '" + _organizationId + "'";


        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new CurrencyInfoItemModel(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            } while (cursor.moveToNext());
        }


        cursor.close();

        db.close();

        return list;
    }

    public String getPrevAskPrice(String _organizationId, String _currencyId) {
        mDataBase = getReadableDatabase();

        String selectQuery = "SELECT " + COL_ASK + " FROM " + TABLE_CURRENCY_RECORDS +
                " WHERE " + COL_ORGANIZATION_ID + " = '" + _organizationId + "' AND " +
                COL_CURRENCY_ID + " = '" + _currencyId + "'";
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);

        String result = null;
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                result = cursor.getString(0);
            }
            cursor.close();
        }
        mDataBase.close();

        return result;
    }

    public String getPrevBidPrice(String _organizationId, String _currencyId) {
        mDataBase = getReadableDatabase();

        String selectQuery = "SELECT " + COL_BID + " FROM " + TABLE_CURRENCY_RECORDS +
                " WHERE " + COL_ORGANIZATION_ID + " = '" + _organizationId + "' AND " +
                COL_CURRENCY_ID + " = '" + _currencyId + "'";
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);

        String result = null;
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                result = cursor.getString(0);
            }
            cursor.close();
        }
        mDataBase.close();

        return result;
    }

}