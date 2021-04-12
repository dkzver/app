package com.wearetogether.v2.ui.components;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.*;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.InputTimerTask;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Media;
import com.wearetogether.v2.ui.activities.RoomActivity;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

public class WriteMessageViewComponent extends FrameLayout {
    private static final int STATE_PRESSING = 1;
    private static final int STATE_UNPRESSING = 2;
    private static final long MAX_RECORD_TIME_MILLS = 60 * 60 * 1000;
    public static final int ERROR_LEN_TEXT = 1;

    private final long DELAY_ANIMATION = 100;
    private final long DELAY = 100;

    public EditText edit_text;
    private FloatingActionButton view_voice;
    private View view_record;
    private View view_text;
    private TextView text_view_time;
    private View view_attach;
    private View view_photo;
    private CountDownTimer countDowTimer;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private File file;
    private RoomActivity activity;

    private Timer mTimer;
    private InputTimerTask inputTimerTask;
    public boolean isInput = false;

    public void setActivity(RoomActivity activity) {

        this.activity = activity;
    }

    public String getText() {
        if(edit_text == null) return null;
        if(edit_text.getText() == null) return null;
        return String.valueOf(edit_text.getText());
    }

    public void setText(String text) {
        if(edit_text != null) {
            edit_text.setText(text);
        }
    }


    public interface Listener {

        void onClickAttachListener(WriteMessageViewComponent component);

        void onClickPhotoListener(WriteMessageViewComponent component);

        void onClickWriteMessage(String text);

        void onError(int code);

        void onRecordStart(WriteMessageViewComponent component);

        void onRecordStop(WriteMessageViewComponent component, File file);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public WriteMessageViewComponent(Context context) {
        super(context);
        initComponent();
    }

    public WriteMessageViewComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    @Override
    public void setEnabled(boolean enabled) {
        view_voice.setEnabled(enabled);
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_view_write_message, this);

        view_record = findViewById(R.id.view_record);
        view_record.setVisibility(View.GONE);
        view_text = findViewById(R.id.view_text);
        view_text.setVisibility(View.VISIBLE);
        text_view_time = findViewById(R.id.text_view_time);
        edit_text = findViewById(R.id.edit_text);
        view_voice = findViewById(R.id.view_voice);
        view_attach = findViewById(R.id.view_attach);
        view_photo = findViewById(R.id.view_photo);
        view_attach.setOnClickListener(onClickAttachListener(this));
        view_photo.setOnClickListener(onClickPhotoListener(this));
        view_voice.setOnClickListener(onClickListener(this));
        view_voice.setOnLongClickListener(onLongClickListener(this));
        view_voice.setOnTouchListener(onTouchListener(this));
        setImage(getContext(), view_voice, R.drawable.baseline_keyboard_voice_black_18dp);
        edit_text.addTextChangedListener(onTextChangedListener(this));
    }

    private OnClickListener onClickAttachListener(WriteMessageViewComponent component) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Media.TakeGallery(activity);
                if (listener != null) listener.onClickAttachListener(component);
            }
        };
    }

    private OnClickListener onClickPhotoListener(WriteMessageViewComponent component) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Media.TakeCamera(activity);
                if (listener != null) listener.onClickPhotoListener(component);
            }
        };
    }

    private TextWatcher onTextChangedListener(WriteMessageViewComponent component) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = String.valueOf(edit_text.getText());
                if (str.length() == 0) {
                    setImage(getContext(), view_voice, R.drawable.baseline_keyboard_voice_black_18dp);
                } else {
                    setImage(getContext(), view_voice, R.drawable.baseline_send_black_18dp);
                }
                if(!isInput) {
                    activity.completeInput(false);
                    isInput = true;
                }
                if (mTimer != null) {
                    mTimer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTimer = new Timer();
                inputTimerTask = new InputTimerTask(activity);
                mTimer.schedule(inputTimerTask, 300);
            }
        };
    }

    private OnTouchListener onTouchListener(WriteMessageViewComponent component) {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_CANCEL:
                        stopTimer(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        stopTimer(false);
                        break;
                }
                return false;
            }
        };
    }

    private OnLongClickListener onLongClickListener(WriteMessageViewComponent component) {
        return new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    postDelayed(action, DELAY);
                }
                return false;
            }
        };
    }

    private OnClickListener onClickListener(WriteMessageViewComponent component) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = String.valueOf(edit_text.getText());
                int length = text.length();
                if (length == 0) {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, Consts.PERMISSION_RECORD_AUDIO);
                    } else {
                        tickCounter();
                    }
                } else if (length >= 3 && listener != null) {
                    listener.onClickWriteMessage(text);
                } else {
                    listener.onError(ERROR_LEN_TEXT);
                }
            }
        };
    }

    private void setState(int state, boolean isStopRecord) {
        if (state == STATE_PRESSING) {
            if (view_record.getVisibility() == View.GONE) {
                ToastUtils.Short(getContext(), "start");
                onRecordStart();
                ScaleAnimation anim = new ScaleAnimation(1, 1.2f, 1, 1.2f, 50, 50);
                anim.setFillBefore(true);
                anim.setFillAfter(true);
                anim.setFillEnabled(true);
                anim.setDuration(DELAY_ANIMATION);
                anim.setInterpolator(new OvershootInterpolator());
                view_voice.startAnimation(anim);
                hideView(view_text, Gravity.LEFT);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showView(view_record, Gravity.LEFT);
                    }
                }, DELAY_ANIMATION);
                if (countDowTimer == null) {
                    countDowTimer = new CountDownTimer(MAX_RECORD_TIME_MILLS, 1000) {

                        public void onTick(long millisUntilFinished) {
                            TimeVariables timeVariables = new TimeVariables();
                            CalcTimeFromMills(timeVariables, millisUntilFinished);
                            text_view_time.setText(GetFormatStringTime(timeVariables));
                        }

                        public void onFinish() {
                            stopTimer(false);
                        }

                    }.start();
                }
            }
        } else if (state == STATE_UNPRESSING) {
            if (view_record.getVisibility() == View.VISIBLE) {
                ToastUtils.Short(getContext(), "stop");
                onRecordStop();
                ScaleAnimation anim = new ScaleAnimation(1.2f, 1, 1.2f, 1, 50, 50);
                anim.setFillBefore(true);
                anim.setFillAfter(true);
                anim.setFillEnabled(true);
                anim.setDuration(DELAY_ANIMATION);
                anim.setInterpolator(new OvershootInterpolator());
                view_voice.startAnimation(anim);
                hideView(view_record, Gravity.LEFT);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showView(view_text, Gravity.LEFT);
                    }
                }, DELAY_ANIMATION);
                if (countDowTimer != null) {
                    countDowTimer.cancel();
                    countDowTimer = null;
//                    if (isStopRecord) {
//                    }
                }
            }
        }
    }

    private void onRecordStop() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            listener.onRecordStop(this, file);
            ToastUtils.Short(getContext(), file.getAbsolutePath());
//            try {
//                releasePlayer();
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setDataSource(file.getAbsolutePath());
//                mediaPlayer.prepare();
//                mediaPlayer.start();
//                if(file.exists()) file.delete();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    private void onRecordStart() {
        try {
            file = FileUtils.CreateRecordFile(getContext());
            releaseRecorder();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(file);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            ToastUtils.Short(getContext(), e);
        }
    }

    public void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    static class TimeVariables {
        public int hours;
        public int minutes;
        public int seconds;
    }

    private static void CalcTimeFromMills(TimeVariables timeVariables, long millisUntilFinished) {
        int seconds_passed = (int) ((MAX_RECORD_TIME_MILLS - millisUntilFinished) / 1000);
        timeVariables.hours = seconds_passed / 3600;
        timeVariables.minutes = (seconds_passed % 3600) / 60;
        timeVariables.seconds = seconds_passed % 60;
    }

    private static String GetFormatStringTime(TimeVariables timeVariables) {
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return String.format("%02d:%02d", timeVariables.minutes, timeVariables.seconds);
    }

    private final Runnable action = new Runnable() {
        @Override
        public void run() {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                tickCounter();
                postDelayed(action, DELAY);
            }
        }
    };

    private void stopTimer(boolean isStopRecord) {
        getHandler().removeCallbacks(action);
        setState(STATE_UNPRESSING, isStopRecord);
    }

    public void tickCounter() {
        setState(STATE_PRESSING, false);
    }


    public void showView(View view, int slideEdge) {
        if (view != null) {
            ViewGroup parent = findViewById(R.id.view_container);

            Transition transition = new Slide(slideEdge);
            transition.setDuration(DELAY_ANIMATION);
            transition.addTarget(view);

            TransitionManager.beginDelayedTransition(parent, transition);
            view.setVisibility(View.VISIBLE);
        }
    }

    public void hideView(View view, int slideEdge) {
        if (view != null) {
            ViewGroup parent = findViewById(R.id.view_container);

            Transition transition = new Slide(slideEdge);
            transition.setDuration(DELAY_ANIMATION);
            transition.addTarget(view);

            TransitionManager.beginDelayedTransition(parent, transition);
            view.setVisibility(View.GONE);
        }
    }

    private void setImage(final Context mContext, final FloatingActionButton imageView, int resource) {
        if (mContext != null && imageView != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setImageDrawable(mContext.getResources().getDrawable(resource, mContext.getApplicationContext().getTheme()));
                } else {
                    imageView.setImageDrawable(mContext.getResources().getDrawable(resource));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setImage(final Context mContext, final ImageView imageView, int resource) {
        if (mContext != null && imageView != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setImageDrawable(mContext.getResources().getDrawable(resource, mContext.getApplicationContext().getTheme()));
                } else {
                    imageView.setImageDrawable(mContext.getResources().getDrawable(resource));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
