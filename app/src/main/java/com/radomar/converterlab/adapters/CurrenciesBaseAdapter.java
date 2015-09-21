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
 * Base adapter. Has more suitable design than recycler adapter
 */
public class CurrenciesBaseAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CurrencyInfoItemModel> mData;
    private LayoutInflater mInflater;

    private TextView mCurrencyName;
    private TextView mAskCost;
    private TextView mBidCost;
    private ImageView mAskMarker;
    private ImageView mBidMarker;

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
        mCurrencyName = (TextView) _view.findViewById(R.id.tvCurrency_CR);
        mAskCost      = (TextView) _view.findViewById(R.id.tvCost_CR);
        mBidCost      = (TextView) _view.findViewById(R.id.tvCost2_CR);

        mAskMarker    = (ImageView) _view.findViewById(R.id.ivMarker_CR);
        mBidMarker    = (ImageView) _view.findViewById(R.id.ivMarker2_CR);
    }

    private void initViews(int _position) {
        CurrencyInfoItemModel currencyInfoItem = mData.get(_position);

        mCurrencyName.setText(currencyInfoItem.currencyName);
        mAskCost.setText(currencyInfoItem.ask);
        mBidCost.setText(currencyInfoItem.bid);

        if (currencyInfoItem.askFlag.equals("1")) {
            mAskMarker.setImageResource(R.drawable.ic_green_arrow_up);
            mAskCost.setTextColor(mContext.getResources().getColor(R.color.green_arrow_up_color));
        } else {
            mAskMarker.setImageResource(R.drawable.ic_red_arrow_down);
            mAskCost.setTextColor(mContext.getResources().getColor(R.color.red_arrow_down_color));
        }

        if (currencyInfoItem.bidFlag.equals("1")) {
            mBidMarker.setImageResource(R.drawable.ic_green_arrow_up);
            mBidCost.setTextColor(mContext.getResources().getColor(R.color.green_arrow_up_color));
        } else {
            mBidMarker.setImageResource(R.drawable.ic_red_arrow_down);
            mBidCost.setTextColor(mContext.getResources().getColor(R.color.red_arrow_down_color));
        }
    }
}
