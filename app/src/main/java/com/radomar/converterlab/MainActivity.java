package com.radomar.converterlab;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.radomar.converterlab.adapters.BankRecyclerAdapter;
import com.radomar.converterlab.db.DatabaseHelper;
import com.radomar.converterlab.model.BankInfoItemModel;
import com.radomar.converterlab.receiver.AlarmReceiver;
import com.radomar.converterlab.service.DataBaseUpdaterService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String START_RECYCLER_ADAPTER = "start recycler adapter";
    private static final String BUNDLE_ARRAY_KEY = "key";
    private static final String BUNDLE_TEXT_KEY = "search text";

    private Receiver mReceiver = new Receiver();
    private IntentFilter mIntentFilter;
    private DatabaseHelper mDbHelper = new DatabaseHelper(this);
    private SearchView mSearchView;
    private ArrayList<BankInfoItemModel> mData = new ArrayList<>();
    private String mSearchableText;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private BankRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshInit();
        initRecyclerView();
        restoreFromBundle(_savedInstanceState);
        initToolbar();
        initializeIntentFilterAndAddAction();
        startAlert();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.menu_main, _menu);

        mSearchView = (SearchView)_menu.findItem(R.id.search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        if (mSearchableText != null) {
            mSearchView.setQuery(mSearchableText, true);
        }

        return super.onCreateOptionsMenu(_menu);
    }

    private void startService() {
        startService(new Intent(this, DataBaseUpdaterService.class));
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tbToolbar_AM);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle _outState) {
        super.onSaveInstanceState(_outState);
        _outState.putParcelableArrayList(BUNDLE_ARRAY_KEY, mData);
        _outState.putString(BUNDLE_TEXT_KEY, mSearchView.getQuery().toString());
    }

    private void restoreFromBundle(Bundle _bundle) {
        if (_bundle == null) {
            startService();
        } else {
            mData = _bundle.getParcelableArrayList(BUNDLE_ARRAY_KEY);
            mSearchableText = _bundle.getString(BUNDLE_TEXT_KEY);
            initAdapter();
        }
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rvList_AM);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new BankRecyclerAdapter(MainActivity.this, mData);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void startAlert() {

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                30 * 60 * 1000,
                30 * 60 * 1000,
                pendingIntent);
    }

    private void initializeIntentFilterAndAddAction() {
        mIntentFilter = new IntentFilter();

        mIntentFilter.addAction(START_RECYCLER_ADAPTER);
    }

    private void swipeRefreshInit() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srRefresh_AM);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                if(mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, 1000);
    }


    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context _context, Intent _intent) {
            mData = mDbHelper.getData();
            initAdapter();
        }
    }

}
