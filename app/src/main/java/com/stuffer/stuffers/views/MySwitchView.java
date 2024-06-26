package com.stuffer.stuffers.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.SwitchCompat;

public class MySwitchView extends SwitchCompat {
    public MySwitchView(Context context) {
        super(context);
        init();
    }

    public MySwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/MavenPro-Regular.ttf");
            setTypeface(tf);
        }
    }
}
