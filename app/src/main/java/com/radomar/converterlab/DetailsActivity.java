package com.radomar.converterlab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.radomar.converterlab.adapters.CurrenciesBaseAdapter;
import com.radomar.converterlab.db.DatabaseHelper;
import com.radomar.converterlab.fragments.DetailShareDialog;
import com.radomar.converterlab.model.CurrencyInfoItemModel;
import com.radomar.converterlab.utils.Utils;

import java.util.ArrayList;


/**
 * Created by Radomar on 14.09.2015
 */
public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String KEY_BANK = "bank name";
    public static final String KEY_CITY = "city name";
    public static final String KEY_BUNDLE = "key_bundle";
    private static final String KEY_LIST = "currency list";
    private static final String KEY_TRANSLUCENT = "translucent";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mBankName;
    private TextView mBankAddress;
    private TextView mPhoneNumber;
    private String mLink;
    private String mCityName;
    private boolean mIsBackGroundTranslucent = false ;

    private String mBankId;
    private DatabaseHelper mDbHelper = new DatabaseHelper(this);

    private ArrayList<CurrencyInfoItemModel> mData = new ArrayList<>();
    private CurrenciesBaseAdapter mAdapter;
    private ListView mListView;

    private FrameLayout mBackground;


    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_details);

        findViewsAndSetListeners();
        initViews();
        initToolbar();
        fabMenuInit();
        restoreFromBundle(_savedInstanceState);
        swipeRefreshInit();
        initRecyclerView();
        initAdapter();

    }

    private void restoreFromBundle(Bundle _bundle) {
        if (_bundle == null) {
            initDataList();
        } else {
            mData = _bundle.getParcelableArrayList(KEY_LIST);
            if (_bundle.getBoolean(KEY_TRANSLUCENT)) {
                mBackground.getBackground().setAlpha(240);
                mIsBackGroundTranslucent = true;
            }
        }
    }

    private void swipeRefreshInit() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srRefresh_AD);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initViews() {
        Intent intent = getIntent();
        mLink =  intent.getStringExtra("link");
        mBankId = intent.getStringExtra("bank id");
        mPhoneNumber.setText(getResources().getString(R.string.phone) + " " + intent.getStringExtra("phone number"));
        mBankName.setText(intent.getStringExtra("bank name"));
        mBankAddress.setText(getResources().getString(R.string.address) + " " + intent.getStringExtra("address"));
        mCityName = intent.getStringExtra("city name");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.menu_details, _menu);
        return super.onCreateOptionsMenu(_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                openShareDialog();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        mListView = (ListView) findViewById(R.id.lvMain);
    }

    private void initDataList() {
        mData = mDbHelper.getCurrenciesList(mBankId);
    }

    private void initAdapter() {
        mAdapter = new CurrenciesBaseAdapter(this, mData);
        mListView.setAdapter(mAdapter);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbToolbar_AD);
        toolbar.setTitle(mBankName.getText().toString());
        toolbar.setSubtitle(mCityName);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow);

    }

    private void fabMenuInit() {
        mBackground = (FrameLayout) findViewById(R.id.flForFAB_AD);
        mBackground.getBackground().setAlpha(0);
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fabMenu_AD);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mIsBackGroundTranslucent = true;
                mBackground.getBackground().setAlpha(240);
                mBackground.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                mIsBackGroundTranslucent = false;
                mBackground.getBackground().setAlpha(0);
                mBackground.setOnTouchListener(null);
            }
        });
    }

    private void findViewsAndSetListeners() {
        mBankName = (TextView)findViewById(R.id.tvBankName_AD);
        mBankAddress = (TextView)findViewById(R.id.tvAddress_AD);
        mPhoneNumber = (TextView)findViewById(R.id.tvPhoneNumber_AD);

        FloatingActionButton linkButton = (FloatingActionButton) findViewById(R.id.fabSite_AD);
        FloatingActionButton mapButton = (FloatingActionButton) findViewById(R.id.fabMap_AD);
        FloatingActionButton callButton = (FloatingActionButton) findViewById(R.id.fabCall_AD);

        linkButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        callButton.setOnClickListener(this);
    }

    private void openShareDialog() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_BUNDLE, mData);
        bundle.putString(KEY_BANK, mBankName.getText().toString());
        bundle.putString(KEY_CITY, mCityName);
        DetailShareDialog dialog = new DetailShareDialog();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "tag");
    }

    @Override
    public void onClick(View _v) {
        switch (_v.getId()) {
            case R.id.fabSite_AD:
                Utils.getInstance(this).openBrowser(mLink);
                break;
            case R.id.fabMap_AD:
                Utils.getInstance(this).openMap(mCityName, getIntent().getStringExtra("address"));
                break;
            case R.id.fabCall_AD:
                Utils.getInstance(this).makeCall(mPhoneNumber.getText().toString());
                break;
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                if (mAdapter != null) {
                    initDataList();
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, 1000);
    }

    @Override
    protected void onSaveInstanceState(Bundle _outState) {
        super.onSaveInstanceState(_outState);
        _outState.putParcelableArrayList(KEY_LIST, mData);
        _outState.putBoolean(KEY_TRANSLUCENT, mIsBackGroundTranslucent);
    }
}
