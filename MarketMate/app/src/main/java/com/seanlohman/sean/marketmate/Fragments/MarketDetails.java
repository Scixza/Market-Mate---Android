package com.seanlohman.sean.marketmate.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.seanlohman.sean.marketmate.R;

import java.util.ArrayList;

public class MarketDetails extends android.support.v4.app.Fragment {

    private Marker currentMarker;
    private String mTitle;
    private Double mLat;
    private Double mLong;

    private TextView name, hours;
    private ArrayList<String> items;
    private ListView mList;

    public static MarketDetails newInstance(Marker m) {

        Bundle args = new Bundle();
        args.putString("Title", m.getTitle());
        args.putDouble("Lat", m.getPosition().latitude);
        args.putDouble("Long", m.getPosition().longitude);
        MarketDetails fragment = new MarketDetails();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null){
            mTitle = getArguments().getString("Title");
            mLat = getArguments().getDouble("Lat");
            mLong = getArguments().getDouble("Long");
        }

        getFragmentManager().beginTransaction().replace(R.id.frameLayout_detail, MarketDetailsMap.newInstance()).commit();
        name.setText(mTitle);
        //ArrayAdapter stringAdapter = new ArrayAdapter();
        //mList.setAdapter();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_market_details, container, false);

        name = rootView.findViewById(R.id.textView_marketName);
        hours = rootView.findViewById(R.id.textView_marketHours);
        mList = rootView.findViewById(R.id.listView_marketStock);
        return rootView;
    }
}
