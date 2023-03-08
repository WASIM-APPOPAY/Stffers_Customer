package com.stuffer.stuffers.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.zhpan.bannerview.provider.ViewStyleSetter;

public class CornerImageView extends AppCompatImageView {
    public CornerImageView(@NonNull Context context) {
        super(context);
    }

    public CornerImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRoundCornet(int radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewStyleSetter.applyRoundCorner(this, radius);
        }
    }
}
