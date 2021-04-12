package com.wearetogether.v2.ui.map;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private ClusterManagerMarker clusterManagerMarker;
    private MainActivity activity;

    public MarkerInfoWindowAdapter(ClusterManagerMarker clusterManagerMarker, MainActivity activity) {
        this.clusterManagerMarker = clusterManagerMarker;
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View v = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.cluster_view_item, null);

        View view_like = v.findViewById(R.id.view_like);
        View view_nearby = v.findViewById(R.id.view_nearby);
        view_like.setVisibility(View.GONE);
        view_nearby.setVisibility(View.GONE);
        TextView text_view_count = v.findViewById(R.id.text_view_count);
        TextView text_view_like = v.findViewById(R.id.text_view_like);
        TextView text_view_title = v.findViewById(R.id.text_view_title);
        TextView text_view_description = v.findViewById(R.id.text_view_description);
        ImageView image_view_icon = v.findViewById(R.id.image_view_icon);
        View image_view = v.findViewById(R.id.image_view);
        List<String> names = new ArrayList<>();

        if (clusterManagerMarker.chosenMarker != null) {
            Integer sizeIconMarkerWindow = activity.getViewModel().sizeIconMarkerWindowMutableLiveData.getValue();
            if(sizeIconMarkerWindow == null) {
                sizeIconMarkerWindow = DimensionUtils.Transform(50, activity.getApplicationContext());
            }
            view_like.setVisibility(View.VISIBLE);
            view_nearby.setVisibility(View.VISIBLE);
            if (clusterManagerMarker.chosenMarker instanceof PlaceMarker) {
                PlaceMarker placeMarker = (PlaceMarker) clusterManagerMarker.chosenMarker;
                names = placeMarker.getNames();
                text_view_title.setText(placeMarker.getTitle());//placeMarker.getUnic() + " " +
                if(placeMarker.date_begin != null && !placeMarker.date_begin.equals("")) {
                    v.findViewById(R.id.view_date_begin).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.text_view_date_begin)).setText(placeMarker.date_begin);
                }
                if(placeMarker.date_end != null && !placeMarker.date_end.equals("")) {
                    v.findViewById(R.id.view_date_end).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.text_view_date_end)).setText(placeMarker.date_end);
                }
                if(placeMarker.time_visit != null && !placeMarker.time_visit.equals("")) {
                    v.findViewById(R.id.view_time_visit).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.text_view_time_visit)).setText(placeMarker.time_visit);
                }
                if (placeMarker.getDescription() != null && !placeMarker.getDescription().equals("")) {
                    text_view_description.setText(placeMarker.getDescription());
                    text_view_description.setVisibility(View.VISIBLE);
                } else {
                    text_view_description.setVisibility(View.GONE);
                }
                if (placeMarker.getBitmap() != null) {
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(placeMarker.getBitmap(), sizeIconMarkerWindow, sizeIconMarkerWindow, false);
                    image_view.setVisibility(View.VISIBLE);
                    image_view_icon.setImageBitmap(scaledBitmap);
                } else {
                    image_view.setVisibility(View.GONE);
                }
                text_view_like.setText(String.valueOf(placeMarker.getRating()));
                marker.setTag(placeMarker.getTag(AbstractMarker.Marker.Place));
                marker.setZIndex(0);
            } else if (clusterManagerMarker.chosenMarker instanceof UserMarker) {
                UserMarker userMarker = (UserMarker) clusterManagerMarker.chosenMarker;
                names = userMarker.getNames();
                text_view_title.setText(userMarker.getTitle());
                if (userMarker.getDescription() != null && !userMarker.getDescription().equals("")) {
                    text_view_description.setText(userMarker.getDescription());
                    text_view_description.setVisibility(View.VISIBLE);
                } else {
                    text_view_description.setVisibility(View.GONE);
                }
                if (userMarker.getBitmap() != null) {
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(userMarker.getBitmap(), sizeIconMarkerWindow, sizeIconMarkerWindow, false);
                    image_view.setVisibility(View.VISIBLE);
                    image_view_icon.setImageBitmap(scaledBitmap);
                } else {
                    image_view.setVisibility(View.GONE);
                }
                text_view_like.setText(String.valueOf(userMarker.getRating()));
                marker.setTag(userMarker.getTag(AbstractMarker.Marker.User));
                marker.setZIndex(1);
            }
        }
        if(names.size() > 0) {
//            String text = String.format(clusterManagerMarker.activity.getString(R.string.nearby), nearby);
            System.out.println("names: " + names);
            Object string_names = StringUtils.GetNames(names);
            System.out.println("string_names: " + string_names);
            String text = String.format(activity.getString(R.string.nearby), string_names);
            text_view_count.setText(text);
            text_view_count.setVisibility(View.VISIBLE);
        } else {
            text_view_count.setVisibility(View.GONE);
        }


        return v;
    }
}
