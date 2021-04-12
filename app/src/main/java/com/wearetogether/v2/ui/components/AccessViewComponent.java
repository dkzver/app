package com.wearetogether.v2.ui.components;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AccessViewComponent extends RelativeLayout implements ViewPager.OnPageChangeListener {
    public ViewPager view_pager;
    private TabLayout tab_layot;
    private AccessSlidePagerAdapter accessSlidePagerAdapter;
    public View buttons_access;
    public View button_access_cancel;
    public View button_access_agree;
    private MainActivity activity;

    public AccessViewComponent(Context context) {
        super(context);
        initComponent();
    }

    public AccessViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.access_view, this);
        view_pager = (ViewPager) view.findViewById(R.id.view_pager_access_slide);
        tab_layot = (TabLayout) view.findViewById(R.id.tab_layot_access_slide);
        view_pager.addOnPageChangeListener(this);
        buttons_access = view.findViewById(R.id.buttons_access);
        button_access_cancel = view.findViewById(R.id.button_access_cancel);
        button_access_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity != null) {
                    activity.finish();
                    System.exit(0);
                }
            }
        });
        button_access_agree = view.findViewById(R.id.button_access_agree);
        button_access_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions;
                int requestCode;
                if (view_pager.getCurrentItem() == 0) {
                    permissions = new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    };
                    requestCode = Consts.REQUEST_PERMISSIONS_LOCATION;
                } else {
                    permissions = new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    };
                    requestCode = Consts.REQUEST_PERMISSIONS_STORAGE;
                }
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }
        });
    }

    public void setup(MainActivity activity) {
        this.activity = activity;
        List<AccessFragment> fragmentList = new ArrayList<>();
        fragmentList.add(FragmentAccessLocation.newInstance());
//        fragmentList.add(FragmentAccessStorage.newInstance());
        accessSlidePagerAdapter = new AccessSlidePagerAdapter(activity.getSupportFragmentManager(), fragmentList);
        view_pager.setAdapter(accessSlidePagerAdapter);
        tab_layot.setupWithViewPager(view_pager, true);
        setButtonVisible(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setButtonVisible(position);
    }

    private void setButtonVisible(int position) {
        Boolean location = activity.getViewModel().accessLocationMutableLiveData.getValue();
        if(location == null) {
            location = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
//        Boolean storage = activity.getViewModel().accessStorageMutableLiveData.getValue();
//        if(storage == null) {
//            boolean WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//            boolean READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//            storage = WRITE_EXTERNAL_STORAGE && READ_EXTERNAL_STORAGE;
//        }
        if(position == 0) {
            button_access_agree.setVisibility(location ? View.GONE : View.VISIBLE);
        } else {
//            button_access_agree.setVisibility(storage ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
