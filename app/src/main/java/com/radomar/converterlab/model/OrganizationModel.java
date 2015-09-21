package com.radomar.converterlab.model;


import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radomar on 14.09.2015
 */
public final class OrganizationModel {

    public String id;
    public String title;
    public String regionId;
    public String cityId;
    public String phone;
    public String address;
    public String link;

    @Expose(serialize = false, deserialize = false)
    public List<CurrencyRecords> recordsList = new ArrayList<>();

}
