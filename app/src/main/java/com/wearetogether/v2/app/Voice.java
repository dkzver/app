package com.wearetogether.v2.app;

import android.content.Intent;
import android.speech.RecognizerIntent;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.ui.activities.FormPlaceActivity;
import com.wearetogether.v2.ui.listeners.VoiceListener;

import java.util.List;

public class Voice {

    public static void Voice(FragmentActivity activity, int request) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        activity.startActivityForResult(intent, request);
    }
    public static void Result(VoiceListener listener, int requestCode, int resultCode, Intent data, int code) {
        if (requestCode == code && resultCode == FragmentActivity.RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (results != null) {
                if (results.size() > 0) {
                    String spokenText = results.get(0);
                    if (spokenText != null) {
                        if (listener != null) {
                            listener.OnSetVoice(code, spokenText);
                        }
                    }
                }
            }
        }
    }
}
