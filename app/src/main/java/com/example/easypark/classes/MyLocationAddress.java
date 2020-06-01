package com.example.easypark.classes;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationAddress {
    private static final String TAG = "MyLocationAddress";
    Geocoder geocoder;
    List<Address> addresses;

    public MyLocationAddress(final Context context,  final double longitude, final double latitude) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try{
                    geocoder = new Geocoder(context, Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                    Log.d(TAG, "MyLocationAddress: "+address+" city: "+city+"state: "+state+", country: "+country+", postalCode: "+postalCode+", knownName: "+knownName);
                } catch (IOException ioe) {
                    Log.d("AddTicket", "auth gps refused: "+ioe.getMessage());
                }
            }
        };

        thread.start();
    }

    private void getAddress() {

    }

}
