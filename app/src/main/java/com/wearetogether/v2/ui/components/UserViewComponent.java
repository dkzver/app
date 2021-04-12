package com.wearetogether.v2.ui.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.data.DataUser;

public class UserViewComponent extends FrameLayout {
    private ImageView image_view;
    private TextView text_view;
    private FragmentActivity activity;
    private DataUser dataUser;
    private Class<?> cls;
    private Long unic;

    public UserViewComponent(Context context) {
        super(context);
        initComponent();
    }

    public UserViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null) {
            inflater.inflate(R.layout.component_view_user, this);
        }

        image_view = findViewById(R.id.image_view);
        text_view = findViewById(R.id.text_view);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                App.GoToUser(activity, unic, cls);
            }
        });
    }

    public void setup(FragmentActivity activity, DataUser dataUser, Class<?> cls, int sizeAvatar) {
        this.activity = activity;
        this.dataUser = dataUser;
        this.cls = cls;
        this.unic = dataUser.unic;
        image_view.setImageBitmap(App.GetRoundedCornerBitmap(Bitmap.createScaledBitmap(dataUser.bitmap, sizeAvatar, sizeAvatar, false)));
        text_view.setText(dataUser.name);
    }
}
