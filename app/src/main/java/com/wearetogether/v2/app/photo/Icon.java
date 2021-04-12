package com.wearetogether.v2.app.photo;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ui.activities.ProfileActivity;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.io.File;
import java.util.Calendar;

public class Icon {
    public static void Start(final FragmentActivity activity, String icon, Long item_unic, int type) {
    }

    private static void removeFile(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists()) file.delete();
        }
    }
}
