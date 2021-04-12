package com.wearetogether.v2.ui.components;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.activities.MainActivity;

public class FragmentAccessLocation extends AccessFragment {

    public static FragmentAccessLocation newInstance() {
        Bundle args = new Bundle();
        FragmentAccessLocation fragment = new FragmentAccessLocation();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_access_location_view, container, false);
        return rootView;
    }
}