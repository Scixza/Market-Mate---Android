package com.seanlohman.sean.marketmate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ArrayList<String> mItems;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        ListView mList = findViewById(R.id.listView_listActivity);
        mItems = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mItems);

        mList.setAdapter(adapter);

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> items = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String item = noteDataSnapshot.getValue(String.class);
                    Log.e("Tag", item);
                    mItems.add(item);
                }

                if (mItems.size() > 0){
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(ListActivity.this, "No List Items Found! Add some to see them here", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
