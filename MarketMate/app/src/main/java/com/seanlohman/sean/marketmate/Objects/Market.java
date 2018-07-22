package com.seanlohman.sean.marketmate.Objects;

import android.media.Image;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class Market {

    private String mName;
    private String mAddress;
    private String mZip;
    private Double mLat;
    private Double mLong;
    private Image mImage;
    private ArrayList<String> mItems;

    public Market(String name, String address, String zip, Double lat, Double longi, Image image, ArrayList<String> items){
        mName = name;
        mAddress = address;
        mZip = zip;
        mLat = lat;
        mLong = longi;
        mImage = image;
        mItems = items;
    }

    public Market(String name, String address, ArrayList<String> items){
        mName = name;
        mAddress = address;
        mItems = items;
    }

    public String getmName() {
        return mName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmZip() {
        return mZip;
    }

    public Double getmLat() {
        return mLat;
    }

    public Double getmLong() {
        return mLong;
    }

    public Image getmImage() {
        return mImage;
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
}
