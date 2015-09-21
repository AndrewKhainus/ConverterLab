package com.radomar.converterlab.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Radomar on 17.09.2015
 **/
public final class BankInfoItemModel implements Parcelable {

    public String organizationId;
    public String organizationName;
    public String regionName;
    public String cityName;
    public String phoneNumber;
    public String address;
    public String link;

    public BankInfoItemModel(String _organizationId, String _organizationName,
                             String _regionName, String _cityName, String _phoneNumber,
                             String _address, String _link) {
        this.organizationId = _organizationId;
        this.organizationName = _organizationName;
        this.regionName = _regionName;
        this.cityName = _cityName;
        this.phoneNumber = _phoneNumber;
        this.address = _address;
        this.link = _link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public BankInfoItemModel(Parcel in) {
        super();
        readFromParcel(in);
    }


    public static final Creator<BankInfoItemModel> CREATOR = new Creator<BankInfoItemModel>() {
        public BankInfoItemModel createFromParcel(Parcel in) {
            return new BankInfoItemModel(in);
        }

        public BankInfoItemModel[] newArray(int size) {

            return new BankInfoItemModel[size];
        }

    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(organizationId);
        dest.writeString(organizationName);
        dest.writeString(regionName);
        dest.writeString(cityName);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeString(link);
    }

    public void readFromParcel(Parcel in) {
        organizationId = in.readString();
        organizationName = in.readString();
        regionName = in.readString();
        cityName = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        link = in.readString();

    }

}
