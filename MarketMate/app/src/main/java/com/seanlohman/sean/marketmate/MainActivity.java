package com.seanlohman.sean.marketmate;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seanlohman.sean.marketmate.Fragments.GoogleMapsFragment;
import com.seanlohman.sean.marketmate.Fragments.MarketDetails;
import com.seanlohman.sean.marketmate.Objects.Market;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleMapsFragment.MarkerListener {

    private boolean mHome = true;
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (mAuth.getCurrentUser() != null){

            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.textView_name);
            navUsername.setText(mAuth.getCurrentUser().getDisplayName());
            TextView navEmail = headerView.findViewById(R.id.textView_email);
            navEmail.setText(mAuth.getCurrentUser().getEmail());
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAuth.getCurrentUser() != null){
                        Intent account = new Intent(MainActivity.this, AccountActivity.class);
                        account.putExtra("name", mAuth.getCurrentUser().getDisplayName());
                        account.putExtra("email", mAuth.getCurrentUser().getEmail());

                        Log.e("Logged In","Name: " + mAuth.getCurrentUser().getDisplayName() + " Email: " + mAuth.getCurrentUser().getEmail());
                        startActivity(account);
                    }else {
                        // Choose authentication providers-
                        List<AuthUI.IdpConfig> providers = Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build());

                        // Create and launch sign-in intent
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setAvailableProviders(providers)
                                        .build(),
                                RC_SIGN_IN);
                    }

                    mHome = false;
                }
            });
        }

        //TODO: Start map fragment here

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_Main, GoogleMapsFragment.newInstance())
                .commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //TODO:
        if (id == R.id.nav_home && !mHome) {
            //Send to home
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Main, GoogleMapsFragment.newInstance()).commit();
            mHome = true;
        }  else if (id == R.id.nav_account) {

            if (mAuth.getCurrentUser() != null){
                Intent account = new Intent(MainActivity.this, AccountActivity.class);
                account.putExtra("name", mAuth.getCurrentUser().getDisplayName());
                account.putExtra("email", mAuth.getCurrentUser().getEmail());

                Log.e("Logged In","Name: " + mAuth.getCurrentUser().getDisplayName() + " Email: " + mAuth.getCurrentUser().getEmail());
                startActivity(account);
            }else {
                // Choose authentication providers-
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build());

                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
            }

            mHome = false;
        } else if (id == R.id.nav_list) {
            if (mAuth.getCurrentUser() != null){
                Intent account = new Intent(MainActivity.this, ListActivity.class);
                startActivity(account);
            }else {
                // Choose authentication providers-
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build());

                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
            }
            mHome = false;
        }
        //else if (id == R.id.nav_market) {
//            mHome = false;
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void openDetails(Market m) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Main, MarketDetails.newInstance(m)).commit();
        mHome = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
                NavigationView navigationView = findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                TextView navUsername = headerView.findViewById(R.id.textView_name);
                navUsername.setText(mAuth.getCurrentUser().getDisplayName());
                TextView navEmail = headerView.findViewById(R.id.textView_email);
                navEmail.setText(mAuth.getCurrentUser().getEmail());
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
