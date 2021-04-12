package com.wearetogether.v2.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.user.UpdateLocation;
import com.wearetogether.v2.services.LocationService;
import com.wearetogether.v2.ui.activities.FormPlaceActivity;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.ToastUtils;

import java.util.ArrayList;

public class LocationReceiver extends BroadcastReceiver {

    public MainActivity mainActivity;
    public FormPlaceActivity formPlaceActivity;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(this);
        System.out.println(mainActivity);
        System.out.println(intent);
        System.out.println(intent.getAction());
        System.out.println(intent.getExtras());
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        if (action == null) return;
        if (bundle == null) return;
        if (action.equals(LocationService.ACTION_UPDATE_LOCATION) && mainActivity != null) {
            System.out.println(this);
            System.out.println("ACTION_UPDATE_LOCATION");
            long time = bundle.getLong("time", 0);
            String message = bundle.getString("message", "");
            Parcelable parcelable = bundle.getParcelable("location");
            String string_address = bundle.getString("string_address", "");
            String string_country = bundle.getString("string_country", "");
            String string_city = bundle.getString("string_city", "");
            if (!message.equals("")) {
                Toast.makeText(context, "message " + message, Toast.LENGTH_SHORT).show();
            } else if (parcelable != null) {
                Location location = (Location) parcelable;
                if (App.SUser != null) {
                    UpdateLocation.Start(mainActivity, location.getLatitude(), location.getLongitude(), string_address, string_country, string_city);
                }
            }
        } else if (action.equals(LocationService.ACTION_GET_LOCATION) && mainActivity != null) {
            System.out.println(this);
            System.out.println("ACTION_GET_LOCATION");
            long time = bundle.getLong("time", 0);
            String message = bundle.getString("message", "");
            Parcelable parcelable = bundle.getParcelable("location");
            String string_address = bundle.getString("string_address", "");
            String string_country = bundle.getString("string_country", "");
            String string_city = bundle.getString("string_city", "");
            if (!message.equals("")) {
                Toast.makeText(context, "message " + message, Toast.LENGTH_SHORT).show();
            } else if (parcelable != null) {
                Location location = (Location) parcelable;
                if (mainActivity.getViewModel() != null) {
                    mainActivity.getViewModel().setLocationRunServiceFindLocation(mainActivity, context, location, string_address, string_country, string_city);
                }
                if (App.SUser != null) {
                    ToastUtils.Short(context, String.format("UpdateLocation %s, %s", String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
                    UpdateLocation.Start(mainActivity, location.getLatitude(), location.getLongitude(), string_address, string_country, string_city);
                }
            }

        } else if (action.equals(LocationService.ACTION_GET_ADDRESS_BY_LOCATION) && formPlaceActivity != null) {
            long time = bundle.getLong("time");
            String string_address = bundle.getString("string_address", "");
            String string_country = bundle.getString("string_country", "");
            String string_city = bundle.getString("string_city", "");
            formPlaceActivity.getViewModel().setLocatin(string_address, 3);
        } else if (action.equals(LocationService.ACTION_GET_ADDRESSES_BY_STRING) && formPlaceActivity != null) {
            long time = bundle.getLong("time");
            ArrayList<Address> addresses = new ArrayList<>();
            ArrayList<Address> parcelableArrayList = bundle.getParcelableArrayList(Consts.LOCATION);
            if (parcelableArrayList != null) {
                addresses.addAll(parcelableArrayList);
            }
            formPlaceActivity.onAttachAddress(addresses);

        }
    }
}
