package com.seanlohman.sean.marketmate.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seanlohman.sean.marketmate.Objects.Market;
import com.seanlohman.sean.marketmate.R;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class GoogleMapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private ArrayList<Market> markets;

    private final HashMap<Marker, Integer> mHashMap = new HashMap<>();

    private MarkerListener mListener;

    private ProgressDialog pd;

    public static GoogleMapsFragment newInstance() {
        return new GoogleMapsFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.e("Tag","Settings Pressed");


            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Search By Zipcode");
            alert.setMessage("Enter a zipcode");

            // Set an EditText view to get user input
            final EditText input = new EditText(getContext());
            alert.setView(input);

            alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String srt = input.getEditableText().toString();

                    if (srt.length() == 5){
                        new SearchTask().execute(srt);

                        LatLng latLng = new LatLng(38,
                                -96);

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 3);

                        mMap.animateCamera(cameraUpdate);

                    }else{
                        Toast.makeText(getContext(), "Invalid Zipcode", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }

                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int pos = mHashMap.get(marker);
        mListener.openDetails(markets.get(pos));
    }

    public interface MarkerListener {
        void openDetails(Market m);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MarkerListener) {
            mListener = (MarkerListener) context;
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(this);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mMap.setMyLocationEnabled(true);

            Location locationCt;
            LocationManager locationManagerCt = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationCt = locationManagerCt
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (locationCt != null){

                Log.e("Users Location", "Lat: " + locationCt.getLatitude() + " Lng: " + locationCt.getLongitude());

                LatLng latLng = new LatLng(locationCt.getLatitude(),
                        locationCt.getLongitude());

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);

                mMap.animateCamera(cameraUpdate);

                //TODO: Get Users Longitude and Latitude and parse the Google GEOCODE API to get the users current ZIPCODE

                new JSONTask().execute(String.valueOf(locationCt.getLatitude()), String.valueOf(locationCt.getLongitude()));

            } else {

                LatLng latLng = new LatLng(38,
                        -96);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 3);

                mMap.animateCamera(cameraUpdate);
            }

        }else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    private void updateMap(){
        mMap.clear();

        for (int i = 0; i < markets.size(); i++){
            Market market = markets.get(i);
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(market.getmLat(), market.getmLng())).title(market.getmName()));
            mHashMap.put(marker, i);
        }

    }

    private class JSONTask extends AsyncTask<String, String, String>{

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading Market Information");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpsURLConnection connection;
            BufferedReader reader = null;
            markets = new ArrayList<>();

            try {

                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + strings[0] + "," + strings[1] + "&key=AIzaSyA6tsrH6pbFP3by7CQ3DrfimZoigvbXoAA");
                Log.e("Tag",url.toString());
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();

                String data = IOUtils.toString(is, "UTF-8");

                is.close();

                connection.disconnect();

                JSONObject jsonObject = new JSONObject(data);
                JSONArray resultArray = jsonObject.getJSONArray("results");
                JSONObject objectZero = resultArray.getJSONObject(0);
                JSONArray addressArray = objectZero.getJSONArray("address_components");
                JSONObject innerArrayObject = addressArray.getJSONObject(7);
                String zip = innerArrayObject.getString("long_name");


                Log.e("GEOCODE information","Location Data: " + zip);

                url = new URL("https://search.ams.usda.gov/farmersmarkets/v1/data.svc/zipSearch?zip=" + zip);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                is = connection.getInputStream();

                data = IOUtils.toString(is, "UTF-8");

                is.close();

                connection.disconnect();


                JSONObject outterobject = new JSONObject(data);
                JSONArray marketArray = outterobject.getJSONArray("results");

                markets.clear();

                for (int i = 0; i < marketArray.length(); i++){

                    JSONObject arrayObject = marketArray.getJSONObject(i);
                    String id = arrayObject.getString("id");
                    String firstName =  arrayObject.getString("marketname");

                    String name = firstName.substring(firstName.indexOf(" ")+1);

                    //Log.e("MarketName", name);

                    url = new URL("https://search.ams.usda.gov/farmersmarkets/v1/data.svc/mktDetail?id=" + id);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.connect();

                    is = connection.getInputStream();

                    data = IOUtils.toString(is, "UTF-8");

                    is.close();

                    connection.disconnect();

                    outterobject = new JSONObject(data);
                    JSONObject marketObject = outterobject.getJSONObject("marketdetails");

                    String address = marketObject.getString("Address");

                    String productString = marketObject.getString("Products");

                    ArrayList<String> products = new ArrayList<String>(Arrays.asList(productString.split(";")));

                    if (products.size() > 1){

                        String timeString = marketObject.getString("Schedule");

                        String timeEdit = timeString.substring(0, timeString.indexOf(";"));

                        String time = timeEdit.substring(timeEdit.indexOf(":")+1).trim();

                        Log.e("time test ", time);

                        //Log.e("Market: ", "Name: " + name + " products size: " + products.size());

                        url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(' ', '+') + "&key=AIzaSyCKZZdcJBVkFpz_JdhW6AGqOT6YtvYB0AQ");
                        //Log.e("URL ",url.toString());
                        connection = (HttpsURLConnection) url.openConnection();
                        connection.connect();

                        is = connection.getInputStream();

                        data = IOUtils.toString(is, "UTF-8");

                        is.close();

                        connection.disconnect();

                        outterobject = new JSONObject(data);
                        JSONArray resultsArray = outterobject.getJSONArray("results");
                        JSONObject innerObject = resultsArray.getJSONObject(0);
                        JSONObject geometryObject = innerObject.getJSONObject("geometry");
                        //Log.e("Geometry Object", geometryObject.toString());
                        JSONObject locationObject = geometryObject.getJSONObject("location");
                        Double lat = locationObject.getDouble("lat");
                        Double lng = locationObject.getDouble("lng");

                        Log.e("Market Information ", "Name: " + name + " products size: " + products.size() + " Lat: " + lat + " Lng: " + lng);
                        markets.add(new Market(name, address, lat, lng, time, products));
                    }

            }

        } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
                updateMap();
            }
        }

    }

    private class SearchTask extends AsyncTask<String, String, String>{

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading Market Information");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpsURLConnection connection;
            BufferedReader reader = null;
            markets = new ArrayList<>();

            try {

                URL url = new URL("https://search.ams.usda.gov/farmersmarkets/v1/data.svc/zipSearch?zip=" + strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();

                String data = IOUtils.toString(is, "UTF-8");

                is.close();

                connection.disconnect();


                JSONObject outterobject = new JSONObject(data);
                JSONArray marketArray = outterobject.getJSONArray("results");

                markets.clear();

                for (int i = 0; i < marketArray.length(); i++){

                    JSONObject arrayObject = marketArray.getJSONObject(i);
                    String id = arrayObject.getString("id");
                    String firstName =  arrayObject.getString("marketname");

                    String name = firstName.substring(firstName.indexOf(" ")+1);

                    //Log.e("MarketName", name);

                    url = new URL("https://search.ams.usda.gov/farmersmarkets/v1/data.svc/mktDetail?id=" + id);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.connect();

                    is = connection.getInputStream();

                    data = IOUtils.toString(is, "UTF-8");

                    is.close();

                    connection.disconnect();

                    outterobject = new JSONObject(data);
                    JSONObject marketObject = outterobject.getJSONObject("marketdetails");

                    String address = marketObject.getString("Address");

                    String productString = marketObject.getString("Products");

                    ArrayList<String> products = new ArrayList<String>(Arrays.asList(productString.split(";")));

                    if (products.size() > 1){

                        String timeString = marketObject.getString("Schedule");

                        String timeEdit = timeString.substring(0, timeString.indexOf(";"));

                        String time = timeEdit.substring(timeEdit.indexOf(":")+1).trim();

                        Log.e("time test ", time);

                        //Log.e("Market: ", "Name: " + name + " products size: " + products.size());

                        url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(' ', '+') + "&key=AIzaSyCKZZdcJBVkFpz_JdhW6AGqOT6YtvYB0AQ");
                        //Log.e("URL ",url.toString());
                        connection = (HttpsURLConnection) url.openConnection();
                        connection.connect();

                        is = connection.getInputStream();

                        data = IOUtils.toString(is, "UTF-8");

                        is.close();

                        connection.disconnect();

                        outterobject = new JSONObject(data);
                        JSONArray resultsArray = outterobject.getJSONArray("results");
                        JSONObject innerObject = resultsArray.getJSONObject(0);
                        JSONObject geometryObject = innerObject.getJSONObject("geometry");
                        //Log.e("Geometry Object", geometryObject.toString());
                        JSONObject locationObject = geometryObject.getJSONObject("location");
                        Double lat = locationObject.getDouble("lat");
                        Double lng = locationObject.getDouble("lng");

                        Log.e("Market Information ", "Name: " + name + " products size: " + products.size() + " Lat: " + lat + " Lng: " + lng);
                        markets.add(new Market(name, address, lat, lng, time, products));
                    }

                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
                updateMap();
            }
        }

    }

}
