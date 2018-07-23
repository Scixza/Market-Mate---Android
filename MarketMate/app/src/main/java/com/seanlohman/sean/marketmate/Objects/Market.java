package com.seanlohman.sean.marketmate.Objects;

import android.media.Image;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class Market {

    private String mName;
    private String mAddress;
    private Double mLat;
    private Double mLng;
    private ArrayList<String> mItems;
    private String mHours;

    public Market(String name, String address, Double lat, Double lng, String hours, ArrayList<String> items){
        mName = name;
        mAddress = address;
        mLat = lat;
        mLng = lng;
        mHours = hours;
        mItems = items;
    }

    public String getmName() {
        return mName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public Double getmLat() {
        return mLat;
    }

    public Double getmLng() {
        return mLng;
    }

    public ArrayList<String> getmItems() {
        return mItems;
    }

    public String getItem(int i){
        return mItems.get(i);
    }

    public String getItem2(String s){

        String returnString = "null";

        for (String string: mItems) {
            if (s.equals(string)){
                returnString = string;
            }
        }

        return returnString;
    }

    public String getmHours() {
        return mHours;
    }
}
