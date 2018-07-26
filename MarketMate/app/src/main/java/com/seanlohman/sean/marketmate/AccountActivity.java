package com.seanlohman.sean.marketmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

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
