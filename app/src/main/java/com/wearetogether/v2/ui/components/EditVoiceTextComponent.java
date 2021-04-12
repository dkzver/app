package com.wearetogether.v2.ui.components;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Voice;
import com.wearetogether.v2.ui.listeners.EditVoiceListener;
import com.wearetogether.v2.utils.TouchUtils;

public class EditVoiceTextComponent extends LinearLayout {
    private ImageView image_view_voice;
    private EditText edit_text;

    public EditVoiceTextComponent(Context context) {
        super(context);
        initComponent();
    }

    public EditVoiceTextComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_view_edit_voice, this);

        image_view_voice = (ImageView) findViewById(R.id.image_view_voice);
        edit_text = (EditText) findViewById(R.id.edit_text);
        edit_text.setOnTouchListener(TouchUtils.touchListener());
    }

    public void setup(FragmentActivity activity, String hint, int code) {
        image_view_voice.setOnClickListener(voice(activity, code));
        edit_text.setHint(hint);
    }

    public void setText(String text) {
        edit_text.setText(text);
    }

    private OnClickListener voice(final FragmentActivity activity, final int code) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean has = false;
                if(activity instanceof EditVoiceListener) {
                    EditVoiceListener listener = (EditVoiceListener) activity;
                    has = listener.hasVoice();
                } else {
                    has = true;
                }
                if(has) {
                    Voice.Voice(activity, code);
                }
            }
        };
    }

    public boolean isEmpty(int minLen) {
        if(edit_text.getText() == null) return true;
        if(edit_text.getText().length() < minLen) return true;
        return false;
    }

    public String getText() {
        return String.valueOf(edit_text.getText());
    }
}
