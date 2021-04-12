package com.wearetogether.v2.app;

import android.content.Intent;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.ToastUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class Google {
    public static void Result(MainActivity activity, int requestCode, int resultCode, Intent data) {

        if (requestCode == Consts.REQUEST_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null) {
                    activity.component_view_login.attemptGoogleLogin(account);
                }
            } catch (ApiException e) {
                e.printStackTrace();
                ToastUtils.Short(activity.getApplicationContext(), "Error sign in google "+e.getMessage());
            }
        }


    }
}
