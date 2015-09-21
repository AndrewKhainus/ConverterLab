package com.radomar.converterlab.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.radomar.converterlab.R;
import com.radomar.converterlab.model.CurrencyInfoItemModel;

import java.util.ArrayList;

/**
 * Created by Radomar on 19.09.2015
 */
public class CurrenciesBaseAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CurrencyInfoItemModel> mData;
    private LayoutInflater mInflater;
//TODO rename variables
    private TextView currencyName;
    private TextView askCost;
    private TextView bidCost;
    private ImageView askMarker;
    private ImageView bidMarker;

    public CurrenciesBaseAdapter(Context _context, ArrayList<CurrencyInfoItemModel> _data ) {
        this.mContext = _context;
        this.mData = _data;
        mInflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.currency_row, parent, false);
        }

        findViews(view);
        initViews(position);

        return view;
    }

    private void findViews(View _view) {
        currencyName = (TextView) _view.findViewById(R.id.tvCurrency_CR);
        askCost      = (TextView) _view.findViewById(R.id.tvCost_CR);
        bidCost      = (TextView) _view.findViewById(R.id.tvCost2_CR);

        askMarker    = (ImageView) _view.findViewById(R.id.ivMarker_CR);
        bidMarker    = (ImageView) _view.findViewById(R.id.ivMarker2_CR);
    }

    private void initViews(int _position) {
        CurrencyInfoItemModel currencyInfoItem = mData.get(_position);

        currencyName.setText(currencyInfoItem.currencyName);
        askCost.setText(currencyInfoItem.ask);
        bidCost.setText(currencyInfoItem.bid);

        if (currencyInfoItem.askFlag.equals("1")) {
            askMarker.setImageResource(R.drawable.ic_green_arrow_up);
            askCost.setTextColor(mContext.getResources().getColor(R.color.green_arrow_up_color));
        } else {
            askMarker.setImageResource(R.drawable.ic_red_arrow_down);
            askCost.setTextColor(mContext.getResources().getColor(R.color.red_arrow_down_color));
        }

        if (currencyInfoItem.bidFlag.equals("1")) {
            bidMarker.setImageResource(R.drawable.ic_green_arrow_up);
            bidCost.setTextColor(mContext.getResources().getColor(R.color.green_arrow_up_color));
        } else {
            bidMarker.setImageResource(R.drawable.ic_red_arrow_down);
            bidCost.setTextColor(mContext.getResources().getColor(R.color.red_arrow_down_color));
        }
    }
}
