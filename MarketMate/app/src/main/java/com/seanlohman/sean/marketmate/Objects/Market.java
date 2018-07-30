package com.seanlohman.sean.marketmate.Objects;

import java.util.ArrayList;

public class Market {

    private final String mName;
    private final Double mLat;
    private final Double mLng;
    private final ArrayList<String> mItems;
    private final String mHours;

    public Market(String name, String address, Double lat, Double lng, String hours, ArrayList<String> items){
        mName = name;
        mLat = lat;
        mLng = lng;
        mHours = hours;
        mItems = items;
    }

    public String getmName() {
        return mName;
    }

// --Commented out by Inspection START (7/26/2018 7:50 PM):
//    public String getmAddress() {
//        return mAddress;
//    }
// --Commented out by Inspection STOP (7/26/2018 7:50 PM)

    public Double getmLat() {
        return mLat;
    }

    public Double getmLng() {
        return mLng;
    }

    public ArrayList<String> getmItems() {
        return mItems;
    }

// --Commented out by Inspection START (7/26/2018 7:50 PM):
//    public String getItem(int i){
//        return mItems.get(i);
//    }
// --Commented out by Inspection STOP (7/26/2018 7:50 PM)

// --Commented out by Inspection START (7/26/2018 7:50 PM):
//    public String getItem2(String s){
//
//        String returnString = "null";
//
//        for (String string: mItems) {
//            if (s.equals(string)){
//                returnString = string;
//            }
//        }
//
//        return returnString;
//    }
// --Commented out by Inspection STOP (7/26/2018 7:50 PM)

    public String getmHours() {
        return mHours;
    }
}
