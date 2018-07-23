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
import com.seanlohman.sean.marketmate.Objects.Market;
import com.seanlohman.sean.marketmate.R;

import java.util.ArrayList;

public class MarketDetails extends android.support.v4.app.Fragment {

    private Marker currentMarker;
    private String mTitle;
    private Double mLat;
    private Double mLng;
    private String mHours;
    private ArrayList<String> mItems;

    private TextView name, hours;
    private ArrayList<String> items;
    private ListView mList;

    public static MarketDetails newInstance(Market m) {

        Bundle args = new Bundle();
        args.putString("Title", m.getmName());
        args.putDouble("Lat", m.getmLat());
        args.putDouble("Lng", m.getmLng());
        args.putString("Hours", m.getmHours());
        args.putStringArrayList("products", m.getmItems());
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
            mLng = getArguments().getDouble("Lng");
            mHours = getArguments().getString("Hours");
            mItems = getArguments().getStringArrayList("products");
        }

        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().replace(R.id.frameLayout_detail, MarketDetailsMap.newInstance(mLat, mLng)).commit();
        }
        name.setText(mTitle);
        hours.setText(mHours);
        ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, mItems);
        mList.setAdapter(stringAdapter);

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
