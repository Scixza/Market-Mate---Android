package com.seanlohman.sean.marketmate.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seanlohman.sean.marketmate.Objects.Market;
import com.seanlohman.sean.marketmate.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MarketDetails extends android.support.v4.app.Fragment {

    private Marker currentMarker;
    private String mTitle;
    private Double mLat;
    private Double mLng;
    private String mHours;
    private ArrayList<String> mItems;
    private int index;

    private TextView name, hours;
    private ArrayList<String> storedItems;
    private ListView mList;

    private DetailsListener mListener;

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

    public interface DetailsListener{
         void addItem(ArrayList<String> items);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DetailsListener){
            mListener = (DetailsListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        index = 0;

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

        mList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Add Item?")
                        .setMessage("Are you sure you want to add \"" + mItems.get(i) + "\" to your shopping list?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //storedItems.add(mItems.get(i));
                                addToFireBase(mItems.get(i), String.valueOf(index));
                                index++;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });

    }

    private void addToFireBase(String item, String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("list").child(id).setValue(item);
        }

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
