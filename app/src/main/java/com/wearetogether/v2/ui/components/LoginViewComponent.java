package com.wearetogether.v2.ui.components;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.wearetogether.v2.utils.ToastUtils;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.FBHelper;
import com.wearetogether.v2.app.HelperListener;
import com.wearetogether.v2.app.VKHelper;
import com.wearetogether.v2.app.model.Account;
import com.wearetogether.v2.app.user.Login;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.ConnectUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;
import com.wearetogether.v2.utils.PreferenceUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class LoginViewComponent extends RelativeLayout implements VKAuthCallback, FacebookCallback<LoginResult> {
    private LoginButton sign_in_fb;
    public CallbackManager callbackManager;
    private com.google.android.gms.auth.api.signin.GoogleSignInClient GoogleSignInClient;
    private MainActivity activity;
    private Context context;

    public LoginViewComponent(Context context) {
        super(context);
        initComponent();
    }


    public LoginViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_view_login, this);

        findViewById(R.id.view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(this);
                System.out.println(this);
                System.out.println(this);
            }
        });
        findViewById(R.id.view_buttons_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = activity.getViewModel().locationMutableLiveData.getValue();
                LatLngBounds latLngBounds = activity.getViewModel().currentCameraBoundsMutableLiveData.getValue();
                String address = activity.getViewModel().addressMutableLiveData.getValue();
                if (location == null) return;
                if (latLngBounds == null) return;
                if (address == null) return;
                if (activity != null) {
                    Intent signInIntent = GoogleSignInClient.getSignInIntent();
                    activity.startActivityForResult(signInIntent, Consts.REQUEST_SIGN_IN_GOOGLE);
                }
            }
        });
        findViewById(R.id.view_button_sign_in_vk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = activity.getViewModel().locationMutableLiveData.getValue();
                if (location == null) return;
                LatLngBounds latLngBounds = activity.getViewModel().currentCameraBoundsMutableLiveData.getValue();
                if (latLngBounds == null) return;
                String address = activity.getViewModel().addressMutableLiveData.getValue();
                if (address == null) return;
                Collection<VKScope> collections = new ArrayList<>();
                collections.add(VKScope.EMAIL);
                collections.add(VKScope.PHOTOS);
                collections.add(VKScope.PHONE);
                VK.login(activity, collections);
            }
        });
        findViewById(R.id.view_button_sign_in_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = activity.getViewModel().locationMutableLiveData.getValue();
                if (location == null) return;
                LatLngBounds latLngBounds = activity.getViewModel().currentCameraBoundsMutableLiveData.getValue();
                if (latLngBounds == null) return;
                String address = activity.getViewModel().addressMutableLiveData.getValue();
                if (address == null) return;
                sign_in_fb.callOnClick();
            }
        });
        findViewById(R.id.view_button_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cellContinue();
            }
        });
    }

    public void setup(MainActivity activity, Context context) {

        this.activity = activity;
        this.context = context;


        sign_in_fb = (LoginButton) activity.findViewById(R.id.sign_in_fb);

        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        GoogleSignInClient.silentSignIn()
                .addOnCompleteListener(
                        activity,
                        new OnCompleteListener<GoogleSignInAccount>() {
                            @Override
                            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                GoogleSignInClient.signOut();
                            }
                        });
        sign_in_fb.setReadPermissions(Arrays.asList("public_profile",
                "email",
                "user_location",
                "user_birthday",
                "user_friends"));
        sign_in_fb.registerCallback(callbackManager, this);
    }

    public void attemptGoogleLogin(GoogleSignInAccount account) {
        saveAccount(getContext(), new Account(account));
        if (GoogleSignInClient != null) {
            GoogleSignInClient.signOut();
        }
        sendDataForServer();
    }

    private void attemptFBLogin(String email, String id, String avatar) {
        String name = email;
        try {
            String[] temp = email.split("@");
            name = temp.length > 1 ? temp[0] : email;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        saveAccount(getContext(), new Account(id, email, avatar, name));
        sendDataForServer();
    }

    private void attemptVkLogin(String email, String id, String avatar, String first_name, String last_name) {
        saveAccount(getContext(), new Account(id, email, avatar, first_name + " " + last_name));
        sendDataForServer();
    }

    public void saveAccount(Context context, Account account) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.UNIC, account.getUnic());
        editor.putString(Consts.SOCIAL_ID, account.getSocialId());
        editor.putString(Consts.EMAIL, account.getEmail());
        editor.putString(Consts.AVATAR, account.getAvatar());
        editor.putString(Consts.NAME, account.getDisplayName());
        editor.apply();
    }

    private void sendDataForServer() {
        if (ConnectUtils.IsOnline(getContext())) {
            String token = PreferenceUtils.GetCloudMessageToken(activity.getApplicationContext());
            if(token == null || token.equals("")) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                prepareStart("");
                            }
                        })
                        .addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                prepareStart("");
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    ToastUtils.Short(activity.getApplicationContext(), "Token fetching FCM registration token failed " + task.getException());
                                    return;
                                }

                                String token = task.getResult();
                                PreferenceUtils.SaveCloudMessageToken(activity.getApplicationContext(), token);
                                prepareStart(token);
                            }
                        });
            } else {
                prepareStart(token);
            }
        } else {
            ToastUtils.Long(getContext(), getContext().getString(R.string.message_enable_network));
        }
    }

    private void prepareStart(String token) {
        Location location = activity.getViewModel().locationMutableLiveData.getValue();
        if (location == null) return;
        LatLngBounds latLngBounds = activity.getViewModel().currentCameraBoundsMutableLiveData.getValue();
        if (latLngBounds == null) return;
        String address = activity.getViewModel().addressMutableLiveData.getValue();
        if (address == null) return;
        String country = activity.getViewModel().countryMutableLiveData.getValue();
        if (country == null) return;
        String city = activity.getViewModel().cityMutableLiveData.getValue();
        if (city == null) return;
        activity.hideLoginView();
        activity.showProgressBar(true);
        Login.Start(activity,
                token,
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()),
                String.valueOf(latLngBounds.southwest.latitude),
                String.valueOf(latLngBounds.northeast.latitude),
                String.valueOf(latLngBounds.southwest.longitude),
                String.valueOf(latLngBounds.northeast.longitude),
                address, country, city);
    }

    @Override
    public void onLogin(@NotNull VKAccessToken vkAccessToken) {
        new VKHelper(vkAccessToken, new HelperListener() {
            @Override
            public void OnResult(String email, String social_id, String avatar) {

            }

            @Override
            public void OnResult(String email, String id, String first_name, String last_name, int sex, String date_birth, String avatar) {
                attemptVkLogin(email, id, avatar, first_name, last_name);
            }
        }).execute();
    }

    @Override
    public void onLoginFailed(int i) {

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        fbLogin(loginResult.getAccessToken());
    }

    private void fbLogin(AccessToken token) {
        new FBHelper(token, new HelperListener() {
            @Override
            public void OnResult(String email, String social_id, String avatar) {
                attemptFBLogin(email, social_id, avatar);
            }

            @Override
            public void OnResult(String email, String id, String first_name, String last_name, int sex, String date_birth, String avatar) {

            }
        }).execute();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    public void cellContinue() {
        if(activity != null) {
            if(activity.component_view_login != null) {
                activity.component_view_login.setVisibility(View.GONE);
            }
        }
    }
}
