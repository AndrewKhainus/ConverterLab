package com.radomar.converterlab.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Radomar on 14.09.2015.
 */
public final class JsonDataModel {

    public String date;
    public ArrayList<OrganizationModel> organizations;

    @Expose(serialize = false, deserialize = false)
    public HashMap<String, String> currencies;

    @Expose(serialize = false, deserialize = false)
    public HashMap<String, String> cities;

    @Expose(serialize = false, deserialize = false)
    public HashMap<String, String> regions;

}
