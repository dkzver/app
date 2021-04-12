package com.wearetogether.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.utils.ToastUtils;

public class PressingView extends View implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
    private int count = 0;
    private final long DELAY = 100;

    public PressingView(Context context) {
        super(context);
        init();
    }

    public PressingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnClickListener(this);
        setOnLongClickListener(this);
        setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        tickCounter();
    }

    @Override
    public boolean onLongClick(View v) {
        postDelayed(action, DELAY);
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                stopTimer();
                break;
            case MotionEvent.ACTION_UP:
                stopTimer();
                break;
        }
        return false;
    }

    private void stopTimer() {
        getHandler().removeCallbacks(action);
        setState(2);
    }

    private void tickCounter() {
        count++;
        setState(1);
    }

    private void setState(int state) {
        ToastUtils.Short(getContext(), "state " + state + " count " + count);
    }

    private final Runnable action = new Runnable() {
        @Override
        public void run() {
            tickCounter();
            postDelayed(action, DELAY);
        }
    };
}
