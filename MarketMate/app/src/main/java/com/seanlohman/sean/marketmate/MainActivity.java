package com.seanlohman.sean.marketmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.seanlohman.sean.marketmate.Fragments.MarketMapFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.frameLayout_Main, MarketMapFragment.newInstance()).commit();
        

    }
}
