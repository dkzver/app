package com.wearetogether.v2.ui.map;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class OwnIconRendered extends DefaultClusterRenderer<AbstractMarker> {

    public OwnIconRendered(Context context, GoogleMap map,
                           ClusterManager<AbstractMarker> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onClusterItemUpdated(AbstractMarker item, Marker marker) {
        boolean changed = false;
        if (item.getTitle() != null && item.getSnippet() != null) {
            if (!item.getTitle().equals(marker.getTitle())) {
                marker.setTitle(item.getTitle());
                changed = true;
            }
            if (!item.getSnippet().equals(marker.getSnippet())) {
                marker.setSnippet(item.getSnippet());
                changed = true;
            }
        } else if (item.getSnippet() != null && !item.getSnippet().equals(marker.getTitle())) {
            marker.setTitle(item.getSnippet());
            changed = true;
        } else if (item.getTitle() != null && !item.getTitle().equals(marker.getTitle())) {
            marker.setTitle(item.getTitle());
            changed = true;
        }
        if (!marker.getPosition().equals(item.getPosition())) {
            marker.setPosition(item.getPosition());
            changed = true;
        }
        if (changed && marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }
    }

    @Override
    protected void onClusterUpdated(@NonNull Cluster<AbstractMarker> cluster, Marker marker) {
    }

    @Override
    protected void onBeforeClusterItemRendered(AbstractMarker item,
                                               MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        BitmapDescriptor bitmapDescriptor = null;
        String title = "";
        String snippet = "";
        if(item != null) {
            if(item.getTitle() != null) {
                title = item.getTitle();
            }
            if(item.getSnippet() != null) {
                snippet = item.getSnippet();
            }
            if(item.getMarkerOptions() != null) {
                if(item.getMarkerOptions().getIcon() != null) {
                    bitmapDescriptor = item.getMarkerOptions().getIcon();
                }
                if(title.equals("") && item.getMarkerOptions().getTitle() != null) {
                    title = item.getMarkerOptions().getTitle();
                }
            }
        }
        if(bitmapDescriptor != null) {
            markerOptions.icon(bitmapDescriptor);
        }
        markerOptions.title(title);
        markerOptions.snippet(snippet);
    }

    @Override
    public Marker getMarker(Cluster<AbstractMarker> cluster) {
        return super.getMarker(cluster);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<AbstractMarker> cluster) {
        return cluster.getSize() > 1;
    }
}
