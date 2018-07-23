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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            if (resultCode == RESULT_OK) {
//                // Successfully signed in
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                // ...
//            } else {
//                // Sign in failed. If response is null the user canceled the
//                // sign-in flow using the back button. Otherwise check
//                // response.getError().getErrorCode() and handle the error.
//                // ...
//                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
    }



}
