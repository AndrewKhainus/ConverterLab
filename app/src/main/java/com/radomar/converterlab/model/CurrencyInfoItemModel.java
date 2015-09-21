package com.radomar.converterlab.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Radomar on 17.09.2015
 */
public final class CurrencyInfoItemModel implements Parcelable {

    public String currencyId;
    public String currencyName;
    public String ask;
    public String bid;
    public String askFlag;
    public String bidFlag;

    public CurrencyInfoItemModel(String currencyId, String currencyName,
                                 String ask, String bid,
                                 String askFlag, String bidFlag) {
        this.currencyId = currencyId;
        this.currencyName = currencyName;
        this.ask = ask;
        this.bid = bid;
        this.askFlag = askFlag;
        this.bidFlag = bidFlag;
    }



    public CurrencyInfoItemModel(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Creator<CurrencyInfoItemModel> CREATOR = new Creator<CurrencyInfoItemModel>() {
        public CurrencyInfoItemModel createFromParcel(Parcel in) {
            return new CurrencyInfoItemModel(in);
        }

        public CurrencyInfoItemModel[] newArray(int size) {

            return new CurrencyInfoItemModel[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currencyId);
        dest.writeString(currencyName);
        dest.writeString(ask);
        dest.writeString(bid);
        dest.writeString(askFlag);
        dest.writeString(bidFlag);
    }

    public void readFromParcel(Parcel in) {
        currencyId = in.readString();
        currencyName = in.readString();
        ask = in.readString();
        bid = in.readString();
        askFlag = in.readString();
        bidFlag = in.readString();
    }
}
