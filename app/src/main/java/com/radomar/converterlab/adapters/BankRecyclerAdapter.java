package com.radomar.converterlab.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.radomar.converterlab.DetailsActivity;
import com.radomar.converterlab.R;
import com.radomar.converterlab.model.BankInfoItemModel;
import com.radomar.converterlab.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radomar on 13.09.2015
 *  recycler adapter
 */
public class BankRecyclerAdapter extends RecyclerView.Adapter<BankRecyclerAdapter.CustomViewHolder> implements Filterable {

    private final Context mContext;
    private List<BankInfoItemModel> mData;
    private List<BankInfoItemModel> mCopyOfData;

    public BankRecyclerAdapter(Context _context, List<BankInfoItemModel> _data) {
        mContext = _context;
        mData = _data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup _viewGroup, int _viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.bank_card_view, _viewGroup, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder _customViewHolder, int _position) {
        _customViewHolder.onBind(_position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<BankInfoItemModel> results = new ArrayList<>();
                if (mCopyOfData == null)
                    mCopyOfData  = mData;
                if (constraint != null){
                    if(mCopyOfData != null & mCopyOfData.size() > 0) {
                        for ( final BankInfoItemModel i :mCopyOfData) {
                            if (i.organizationName.toLowerCase().contains(constraint.toString()) ||
                                    i.cityName.toLowerCase().contains(constraint.toString()) ||
                                    i.regionName.toLowerCase().contains(constraint.toString())) {
                                results.add(i);
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mData = (ArrayList<BankInfoItemModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }


    /**
     View Holder
     */
    class CustomViewHolder extends RecyclerView.ViewHolder implements TabLayout.OnTabSelectedListener {

        TextView bankName;
        TextView regionName;
        TextView cityName;
        TextView phoneNumber;
        TextView address;
        String link;
        String id;
        TabLayout tabLayout;

        public CustomViewHolder(View _itemView) {
            super(_itemView);

            findViews(_itemView);
            tabLayoutInit();
        }

        public void onBind(int position) {
            BankInfoItemModel bankInfo = mData.get(position);
            bankName.setText(bankInfo.organizationName);
            regionName.setText(bankInfo.regionName);
            cityName.setText(bankInfo.cityName);
            if (!bankInfo.phoneNumber.equals("null")) {
                phoneNumber.setText(bankInfo.phoneNumber);
            } else {
                phoneNumber.setText("- - -");
            }
            address.setText(bankInfo.address);

            link = bankInfo.link;
            id = bankInfo.organizationId;
        }

        private void findViews(View _view) {
            bankName = (TextView) _view.findViewById(R.id.tvBankName_BCV);
            regionName = (TextView) _view.findViewById(R.id.tvArea_BCV);
            cityName = (TextView) _view.findViewById(R.id.tvCity_BCV);
            phoneNumber = (TextView) _view.findViewById(R.id.tvPhoneNumber_BCV);
            address = (TextView) _view.findViewById(R.id.tvAddress_BCV);
            tabLayout = (TabLayout) _view.findViewById(R.id.tabs);
        }

        private void tabLayoutInit() {
            tabLayout.setOnTabSelectedListener(this);
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_link), false);
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_map));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_phone));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_next));

        }

        @Override
        public void onTabSelected(TabLayout.Tab _tab) {
            actionForTabs(_tab);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab _tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab _tab) {
            actionForTabs(_tab);
        }

        private void actionForTabs(TabLayout.Tab _tab) {
            switch (_tab.getPosition()) {
                case 0:
                    Utils.getInstance(mContext).openBrowser(link);
                    break;
                case 1:
                    Utils.getInstance(mContext).openMap(cityName.getText().toString(),
                            address.getText().toString());
                    break;
                case 2:
                    Utils.getInstance(mContext).makeCall(phoneNumber.getText().toString());
                    break;
                case 3:
                    openDetails();
                    break;
            }
        }

        private void openDetails() {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("bank id", id)
                    .putExtra("bank name", bankName.getText().toString())
                    .putExtra("phone number", phoneNumber.getText().toString())
                    .putExtra("address", address.getText().toString())
                    .putExtra("city name", cityName.getText().toString())
                    .putExtra("link", link);

            mContext.startActivity(intent);
        }
    }
}
