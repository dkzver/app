package com.wearetogether.v2;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.location.Location;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.app.data.DataFromServer;
import com.wearetogether.v2.app.message.Create;
import com.wearetogether.v2.utils.MapBitmapCache;
import com.wearetogether.v2.utils.NotificationUtils;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wearetogether.v2.app.place.Remove;
import com.wearetogether.v2.app.user.Online;
import com.wearetogether.v2.ui.activities.*;
import com.wearetogether.v2.ui.map.MarkerItem;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;
import com.wearetogether.v2.database.AppDatabase;
import com.wearetogether.v2.smodel.SUser;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class App extends Application {
    public static SUser SUser = null;
    public static AppDatabase Database;
    public static List<MarkerItem> nearPlaces = new ArrayList<>();
    public static List<MarkerItem> nearUsers = new ArrayList<>();
    public static String[] Categories;
    public static String[] Statuses;
    public static String[] Interests;
    public static Charset UTF8 = Charset.forName("utf-8");
    public static ContentType ContentTypeUTF8 = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), UTF8);
    public static String UserName = "";
    public static String PlaceTitle = "";
    public static String FormPlaceTitle = "";
    public static boolean IsRun = false;
    public static NotificationUtils NotificationUtils;
    public static boolean IsUpdate = false;
    public static MapBitmapCache MapCache;
    public static Long RoomUnic = null;

    public static void GoToPlace(FragmentActivity activity, Long place_unic, Class<?> cls) {
        Intent intent = new Intent(activity.getApplicationContext(), PlaceActivity.class);
        if (place_unic != null) {
            intent.putExtra(Consts.UNIC, String.valueOf(place_unic));
        }
        intent.putExtra("cls", cls);
        activity.startActivity(intent);
    }

    public static void GoToReviews(FragmentActivity activity, Long item_unic, Class<?> cls, int type) {
        Intent intent = new Intent(activity.getApplicationContext(), ReviewsActivity.class);
        if (item_unic != null) {
            System.out.println("unic " + String.valueOf(item_unic));
            intent.putExtra(Consts.UNIC, String.valueOf(item_unic));
            intent.putExtra("type", String.valueOf(type));
        }
        intent.putExtra("cls", cls);
        activity.startActivity(intent);
    }

    public static void GoToUser(FragmentActivity activity, Long user_unic, Class<?> cls) {
        Intent intent = new Intent(activity.getApplicationContext(), UserActivity.class);
        if (user_unic != null) {
            intent.putExtra(Consts.UNIC, String.valueOf(user_unic));
        }
        intent.putExtra("cls", cls);
        activity.startActivity(intent);
    }

    public static void GoToUserPlaces(FragmentActivity activity, Long user_unic, Class<PlaceActivity> cls) {
        Intent intent = new Intent(activity.getApplicationContext(), PlacesActivity.class);
        if (user_unic != null) {
            intent.putExtra(Consts.USER_UNIC, String.valueOf(user_unic));
        }
        intent.putExtra("cls", cls);
        activity.startActivity(intent);
    }

    public static void GoToProfile(FragmentActivity activity) {
        activity.startActivity(new Intent(activity.getApplicationContext(), ProfileActivity.class));
    }

    public static String CapitalizeString(String value) {
        String retStr = value;
        try {
            retStr = value.substring(0, 1).toUpperCase() + value.substring(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
    }

    public static void GoToFormPlace(FragmentActivity activity, Long unic, Class<?> cls) {
        Intent intent = new Intent(activity.getApplicationContext(), FormPlaceActivity.class);
        if (unic != null) {
            intent.putExtra(Consts.UNIC, String.valueOf(unic));
        }
        intent.putExtra("cls", cls);
        activity.startActivity(intent);
    }

    public static void GoToVisited(PlaceActivity activity, long place_unic, Class<?> cls) {
        Intent intent = new Intent(activity.getApplicationContext(), VisitedPlaceActivity.class);
        intent.putExtra(Consts.UNIC, String.valueOf(place_unic));
        intent.putExtra("cls", cls);
        activity.startActivity(intent);
    }

    public static void GoToRoom(FragmentActivity activity, Long room_unic, Long user_unic, Class<?> cls) {
        if (room_unic == null && user_unic != null) {
            Create.Start(activity, user_unic, cls);
        } else {
            GoToRoom(activity, room_unic, cls);
        }
    }

    public static void GoToRoom(FragmentActivity activity, Long room_unic, Class<?> cls) {
        Intent intent = new Intent(activity.getApplicationContext(), RoomActivity.class);
        intent.putExtra(Consts.UNIC, String.valueOf(room_unic));
        intent.putExtra("cls", cls);
        activity.startActivity(intent);
    }

    public static void GoToRooms(FragmentActivity activity, Class<?> cls) {
        Intent intent = new Intent(activity.getApplicationContext(), RoomsActivity.class);
        intent.putExtra("cls", cls);
        activity.startActivity(intent);
    }

    public static void ShowDialogGuest(MainActivity activity, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(activity.getString(R.string.please_login))
                .setPositiveButton(R.string.button_login,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void GoToMessages(FragmentActivity activity, Long place_unic, Class<?> cls) {

    }

    public static ViewGroup.LayoutParams GetChildrenParams(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int px16 = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics()));
        int px8 = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics()));
        params.setMargins(px16, px8, 0, 0);
        return params;
    }

    public static String[] TransformDateTime(String date_string) {
        String time_string = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(date_string);
            if (date == null) return new String[]{date_string, ""};
            simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            date_string = simpleDateFormat.format(date);
            simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            time_string = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{date_string, time_string};
    }

    public static void HideKeyboard(Context context, View[] views) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            for (int x = 0; x < views.length; x++) {
                imm.hideSoftInputFromWindow(views[x].getWindowToken(), 0);
            }
        }
    }

    public static String GetAge(String date_birth) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Consts.Format_yyyy_MM_dd);
            Date date = simpleDateFormat.parse(date_birth);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                LocalDate birthDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                LocalDate currentDate = LocalDate.now();
                return String.valueOf(Period.between(birthDate, currentDate).getYears());
            } else {
                return "0";
            }

        } catch (Exception e) {
            return "0";
        }
    }

    public static void InitCache() {
        MapCache = new MapBitmapCache(Consts.CACHE_MAX_SIZE);
    }

    public static class Version {
        public String app;
        public String categories;
        public String interests;
        public String statuses;
    }

    public static void GetVersionApp(Context context, Version version) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.VERSION_PREFERENCES, Context.MODE_PRIVATE);
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            System.out.println("info.versionName: " + info.versionName);
            version.app = String.valueOf(PackageInfoCompat.getLongVersionCode(info));
            version.categories = sharedPreferences.getString("categories", "0");
            version.interests = sharedPreferences.getString("interests", "0");
            version.statuses = sharedPreferences.getString("statuses", "0");
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void SetVersionApp(DataFromServer data, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.VERSION_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("categories", data.version_categories);
        editor.putString("interests", data.version_interests);
        editor.putString("statuses", data.version_statuses);
        editor.commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VK.addTokenExpiredHandler(new VKTokenExpiredHandler() {
            @Override
            public void onTokenExpired() {

            }
        });
        Database = AppDatabase.getInstance(getApplicationContext());
        App.SUser = PreferenceUtils.GetUser(getApplicationContext());
        InitCache();
        NotificationUtils = new NotificationUtils(getApplicationContext());

        FirebaseMessaging.getInstance().getToken()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        PreferenceUtils.SaveCloudMessageToken(getApplicationContext(), token);

                        // Log and toast
                        Log.d("Token", token);
                    }
                });
    }

    public boolean isOnline = false;

    public void sendDataUserOnline(FragmentActivity activity, int is_online, Online.Listener listener) {
        Online.Start(activity, is_online, listener);
    }

    public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 360;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void ShowDialogRemovePlace(FragmentActivity activity, Context context, int is_remove, long place_unic, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String dialog_title = "";
        if (is_remove == 1) {
            dialog_title = activity.getString(R.string.dialog_title_restore_place);
        } else if (is_remove == 0) {
            dialog_title = activity.getString(R.string.dialog_title_remove_place);
        }
        builder.setTitle(dialog_title);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int new_is_remove = 0;
                        if (is_remove == 1) {
                            new_is_remove = 0;
                        } else if (is_remove == 0) {
                            new_is_remove = 1;
                        }
                        System.out.println("new remove " + new_is_remove);
                        Remove.Start(activity, place_unic, new_is_remove, position);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
        );
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
