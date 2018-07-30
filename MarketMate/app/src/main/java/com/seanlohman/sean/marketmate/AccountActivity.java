package com.seanlohman.sean.marketmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_screen);
        Intent intent = getIntent();
        String name =  intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        TextView nameText = findViewById(R.id.textView_name);
        TextView emailText = findViewById(R.id.textView_email);
        nameText.setText(name);
        emailText.setText(email);
        Button signout = findViewById(R.id.button_signout);
        signout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                finish();
            }
        });
    }

}
