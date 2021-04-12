package com.wearetogether.v2.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LocationService extends IntentService {
    private static final long INTERVAL_RUN_SERVICE = 120000;
    public static final String ACTION_GET_LOCATION = "com.simple.location.LocationService.ACTION_GET_LOCATION";
    public static final String ACTION_UPDATE_LOCATION = "com.simple.location.LocationService.ACTION_UPDATE_LOCATION";
    public static final String ACTION_GET_ADDRESS_BY_LOCATION = "com.simple.location.LocationService.ACTION_GET_ADDRESS_BY_LOCATION";
    public static final String ACTION_GET_ADDRESSES_BY_STRING = "com.simple.location.LocationService.ACTION_GET_ADDRESS_BY_STRING";

    private final LocationServiceBinder binder = new LocationServiceBinder();


    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    public LocationService() {
        super("LocationService");
    }

    public static void Stop(FragmentActivity activity) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println(this);
        System.out.println("onCreate");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(INTERVAL_RUN_SERVICE);
        locationRequest.setFastestInterval(INTERVAL_RUN_SERVICE);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println(this);
        System.out.println("onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println(this);
        System.out.println("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println(this);
        System.out.println("onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            System.out.println("action " + action);
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation(final boolean isUpdate) {
        System.out.println(this);
        System.out.println("getLocation");
        final LocationService locationService = this;
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                locationService.onLocationAvailability(locationAvailability);
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                locationService.onLocationResult(locationResult, isUpdate);
            }
        };
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                attachLocation(null, location, isUpdate);
            }
        });
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public void getAddresses(String location, int maxResult) {
        try {
            final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocationName(location, maxResult);
            if(addressList == null) {
                addressList = new ArrayList<>();
            }
            ArrayList<Address> addresses = new ArrayList<>();
            for(int i = 0; i < addressList.size(); i++) {
                addresses.add(addressList.get(i));
            }
            Intent i = new Intent(ACTION_GET_ADDRESSES_BY_STRING);
            i.putParcelableArrayListExtra(Consts.LOCATION, addresses);
            i.putExtra("time", Calendar.getInstance().getTimeInMillis());
            sendBroadcast(i);
        } catch (Exception e) {
            Log.e("TAG", "service_not_available", e);
        }
    }

    public void getAddress(Location location) {
        System.out.println(this);
        System.out.println("getAddress");
        String message = null;
        String string_address = "";
        String string_country = "";
        String string_city = "";
        Intent i = new Intent(ACTION_GET_ADDRESS_BY_LOCATION);
        if (location != null) {
            try {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;

                try {
                    if (location == null) {
                        throw new Exception("error location");
                    }
                    addresses = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            5);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (addresses == null || addresses.size() == 0) {
                    System.out.println("no_address_found");
                } else {
                    Address address = addresses.get(0);
                    System.out.println("address_found " + address);
                    string_address = address.getAddressLine(0);
                    string_country = addresses.get(0).getCountryName();
                    string_city = addresses.get(0).getLocality();
                }
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getMessage();
            }
        }
        i.putExtra("time", Calendar.getInstance().getTimeInMillis());
        if (message != null) {
            i.putExtra("message", message);
        }
        System.out.println("Location " + location);
        System.out.println("string_address " + string_address);
        System.out.println("string_country " + string_country);
        System.out.println("string_city " + string_city);
        if(location != null) {

            i.putExtra("location", location);
            i.putExtra("string_address", string_address);
            i.putExtra("string_country", string_country);
            i.putExtra("string_city", string_city);
        }
        sendBroadcast(i);
    }

    private void onLocationResult(LocationResult locationResult, final boolean isUpdate) {
        String message = null;
        Location location = null;
        if (locationResult == null) {
            message = "location result null";
        }
        if (message == null) {
            List<Location> locations = locationResult.getLocations();
            if (locations.size() == 0) {
                message = "location result 0";
            }
            if (message == null) {
                location = locations.get(0);
            }
        }
        attachLocation(message, location, isUpdate);
    }

    private void attachLocation(String message, Location location, final boolean isUpdate) {
        System.out.println("attachLocation "  + isUpdate);
        String string_address = "";
        String string_country = "";
        String string_city = "";
        Intent i = new Intent(isUpdate ? ACTION_UPDATE_LOCATION : ACTION_GET_LOCATION);
        System.out.println("attachLocation message "  + message);
        System.out.println("attachLocation location "  + location);
        if (location != null) {
            try {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;

                try {
                    if (location == null) {
                        throw new Exception("error location");
                    }
                    addresses = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (addresses == null || addresses.size() == 0) {
                    System.out.println("no_address_found");
                } else {
                    Address address = addresses.get(0);
                    System.out.println("address_found " + address);
                    string_address = address.getAddressLine(0);
                    string_country = addresses.get(0).getCountryName();
                    string_city = addresses.get(0).getLocality();
                }
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getMessage();
            }
        }
        i.putExtra("time", Calendar.getInstance().getTimeInMillis());
        if (message != null) {
            i.putExtra("message", message);
        }
        System.out.println("Location " + location);
        System.out.println("string_address " + string_address);
        System.out.println("string_country " + string_country);
        System.out.println("string_city " + string_city);
        if(location != null) {

            i.putExtra("location", location);
            i.putExtra("string_address", string_address);
            i.putExtra("string_country", string_country);
            i.putExtra("string_city", string_city);
            if(!isUpdate) {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        }
        sendBroadcast(i);
    }

    private void onLocationAvailability(LocationAvailability locationAvailability) {
        if (!locationAvailability.isLocationAvailable()) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            Intent i = new Intent(ACTION_GET_LOCATION);
            i.putExtra("message", "Location not availability");
            sendBroadcast(i);
        }
    }

    public class LocationServiceBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }
}
